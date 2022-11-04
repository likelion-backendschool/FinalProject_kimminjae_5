package com.example.order;


import com.example.cart.CartService;
import com.example.member.Member;
import com.example.member.MemberDto;
import com.example.member.MemberService;
import com.example.mybook.MyBook;
import com.example.mybook.MyBookService;
import com.example.product.Product;
import com.example.util.Ut;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import com.example.order.Order;

import javax.annotation.PostConstruct;
import java.security.Principal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.example.AppConfig.cancelAvailableMinutes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final CartService cartService;
    private final MyBookService myBookService;

    //환불
    @GetMapping("/{id}/refund")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String refundOrder(Principal principal, @PathVariable long id) {
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());
        Order order = orderService.findForPrintById(id).orElse(null);

        LocalDateTime now = LocalDateTime.now(); //날짜1
        LocalDateTime createDate = order.getCreateDate();

        //두 시간 차이를 분으로 환산
        LocalTime start = createDate.toLocalTime();
        LocalTime end = now.toLocalTime();
        Duration diff = Duration.between(start, end);
        long diffMin = diff.toMinutes();


        if(diffMin > cancelAvailableMinutes) {
            return "<script>alert('구매한지 10분이 지나 환불이 불가합니다.'); location.href='/order/%d';</script>".formatted(order.getId());
        }
        List<OrderItem> orderItems = order.getOrderItems();
        List<Product> productList = new ArrayList<>();
        for(OrderItem orderItem : orderItems) {
            productList.add(orderItem.getProduct());
        }
        myBookService.removeMyBook(memberDto, productList);
        orderService.refund(order);

        return "<script>alert('환불 완료!'); location.href='/order/%d';</script>".formatted(order.getId());
    }

    //구매 취소
    @GetMapping("/{id}/cancel")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String cancelOrder(Principal principal, @PathVariable long id) {
        Order order = orderService.findForPrintById(id).orElse(null);
        MemberDto memberDto = memberService.getMemberByUsername(principal.getName());

//        for(int i = 0; i < order.getOrderItems().size(); i++) {
//            Product product = order.getOrderItems().get(i).getProduct();
//            cartService.addItem(memberDto.toEntity(), product);
//        }

        orderService.cancel(id);

        return "<script>alert('주문이 취소되었습니다.'); location.href='/member?listType=orderList';</script>";
    }

    //예치금으로만 결제
    @PostMapping("/{id}/payByRestCashOnly")
    @PreAuthorize("isAuthenticated()")
    public String payByRestCashOnly(Principal principal, @PathVariable long id) {
        Order order = orderService.findForPrintById(id).get();

        Member actor = memberService.getMemberByUsername(principal.getName()).toEntity();

        long restCash = memberService.getRestCash(actor);

        if(orderService.actorCanPayment(actor, order) == false) {
            throw new ActorCanNotPayOrderException();
        }
        orderService.payByRestCashOnly(order); //결제

        //결제시 MyBook에 도서들을 등록해 볼 수 있도록
        List<OrderItem> orderItems = order.getOrderItems();

        for(OrderItem orderItem : orderItems) {
            myBookService.create(actor, orderItem.getProduct());
        }
        return "redirect:/order/%d?msg=%s".formatted(order.getId(), Ut.url.encode("예치금으로 결제했습니다."));
    }

    //주문 상세
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showDetail(Principal principal, @PathVariable long id, Model model) {
        Order order = orderService.findForPrintById(id).get();

        Member actor = memberService.getMemberByUsername(principal.getName()).toEntity();

        long restCash = memberService.getRestCash(actor);

        if (orderService.actorCanSee(actor, order) == false) {
            throw new ActorCanNotSeeOrderException();
        }

        model.addAttribute("actorRestCash", restCash);
        model.addAttribute("order", order);

        return "order/detail";
    }
    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    private final String SECRET_KEY = "test_sk_7XZYkKL4MrjE261OM7AV0zJwlEWR";

    @RequestMapping("/{id}/success")
    public String confirmPayment(@PathVariable long id,
                                 @RequestParam String paymentKey,
                                 @RequestParam String orderId,
                                 @RequestParam Long amount,
                                 Model model, Principal principal) throws Exception {

        Order order = orderService.findForPrintById(id).get();

        long orderIdInputed = Long.parseLong(orderId.split("__")[1]);

        if ( id != orderIdInputed ) {
            throw new OrderIdNotMatchedException();
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
//        payloadMap.put("amount", String.valueOf(order.calculatePayPrice()));
        payloadMap.put("amount", String.valueOf(amount));

        Member actor = memberService.getMemberByUsername(principal.getName()).toEntity();
        long restCash = memberService.getRestCash(actor);
        long payPriceRestCash = order.calculatePayPrice() - amount;

        if (payPriceRestCash > restCash) {
            throw new OrderNotEnoughRestCashException();
        }

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            JsonNode successNode = responseEntity.getBody();
//            model.addAttribute("orderId", successNode.get("orderId").asText());
//            String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함
//            return "order/success";
            orderService.payByTossPayments(order, payPriceRestCash);

            //구매후 mybook에 추가
            List<OrderItem> orderItems = order.getOrderItems();

            for(OrderItem orderItem : orderItems) {
                myBookService.create(actor, orderItem.getProduct());
            }

            return "redirect:/order/%d?msg=%s".formatted(order.getId(), Ut.url.encode("결제가 완료되었습니다."));
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "order/fail";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "order/fail";
    }

    //주문 생성
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String makeOrder(Principal principal) {
        Member member = memberService.getMemberByUsername(principal.getName()).toEntity();
        Order order = orderService.createFromCart(member);

        //장바구니가 비어있을 경우, 주문 생성 불가
        if(order == null) {
            return "<script>alert('장바구니 목록이 비어있습니다.'); location.href='/cart/list';</script>";
        }
//        String redirect = "/order/%d".formatted(order.getId()) + "?msg=" + Ut.url.encode("%d번 주문이 생성되었습니다.".formatted(order.getId()));
        String redirect = "<script>location.href='/order/%d?msg=%s';</script>".formatted(order.getId(), Ut.url.encode("%d번 주문이 생성되었습니다.".formatted(order.getId())));
        return redirect;
    }
}