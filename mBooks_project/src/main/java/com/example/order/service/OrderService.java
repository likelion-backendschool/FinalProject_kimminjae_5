package com.example.order.service;

import com.example.cart.entity.CartItem;
import com.example.cart.service.CartService;
import com.example.member.enitty.Member;
import com.example.member.dto.MemberDto;
import com.example.member.service.MemberService;
import com.example.order.entity.Order;
import com.example.order.entity.OrderItem;
import com.example.order.repository.OrderItemRepository;
import com.example.order.repository.OrderRepository;
import com.example.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class OrderService {
    private final MemberService memberService;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    //장바구니 목록들로 주문 생성
    @Transactional
    public Order createFromCart(Member buyer) {
        // 입력된 회원의 장바구니 아이템들을 전부 가져온다.

        // 만약에 특정 장바구니의 상품옵션이 판매불능이면 삭제
        // 만약에 특정 장바구니의 상품옵션이 판매가능이면 주문품목으로 옮긴 후 삭제

        List<CartItem> cartItems = cartService.getItemsByBuyer(buyer);

        if(cartItems.size() == 0) {
            return null;
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.isOrderable()) {
                orderItems.add(new OrderItem(product));
            }

            cartService.removeItem(cartItem);
        }

        return create(buyer, orderItems);
    }

    //주문 생성
    @Transactional
    public Order create(Member buyer, List<OrderItem> orderItems) {
        Order order = Order
                .builder()
                .buyer(buyer)
                .createDate(LocalDateTime.now())
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        //주문 품목으로부터 이름을 만든다.
        order.makeName();;

        orderRepository.save(order);

        return order;
    }

    //예치금으로만 결제
    @Transactional
    public void payByRestCashOnly(Order order) {
        Member buyer = order.getBuyer();

        long restCash = buyer.getRestCash();

        int payPrice = order.calculatePayPrice();

        if (payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }

        memberService.addCash(buyer, payPrice * -1, "주문__%d__사용__예치금".formatted(order.getId()));

        order.setPaymentDone();
        orderRepository.save(order);
    }

    @Transactional
    public void refund(Order order) {
        int payPrice = order.getPayPrice();
        memberService.addCash(order.getBuyer(), payPrice, "주문__%d__환불__예치금".formatted(order.getId()));

        order.setRefundDone();
        orderRepository.save(order);
    }
    public Optional<Order> findForPrintById(long id) {
        return findById(id);
    }

    private Optional<Order> findById(long id) {
        return orderRepository.findById(id);
    }

    public boolean actorCanSee(Member actor, Order order) {
        return actor.getId().equals(order.getBuyer().getId());
    }

    //결제
    @Transactional
    public void payByTossPayments(Order order, long useRestCash) {
        Member buyer = order.getBuyer();
        int payPrice = order.calculatePayPrice();

//        memberService.addCash(buyer, payPrice, "주문결제충전__토스페이먼츠");
//        memberService.addCash(buyer, payPrice * -1, "주문결제__토스페이먼츠");
        long pgPayPrice = payPrice - useRestCash;
        memberService.addCash(buyer, pgPayPrice, "주문__%d__충전__토스페이먼츠".formatted(order.getId()));
        memberService.addCash(buyer, pgPayPrice * -1, "주문__%d__사용__토스페이먼츠".formatted(order.getId()));

        if(useRestCash > 0) {
            memberService.addCash(buyer, useRestCash * -1, "주문__%d__사용__예치금".formatted(order.getId()));
        }

        order.setPaymentDone();
        orderRepository.save(order);
    }
    public boolean actorCanPayment(Member actor, Order order) {
        return actorCanSee(actor, order);
    }


    //주문 취소
    public void cancel(long id) {
        Order order = orderRepository.findById(id).orElse(null);
        order.setCanceledDone();
        order.setCanceled(true);

        orderRepository.save(order);

//        orderRepository.delete(order);
    }

    //회원정보로 주문목록 가져오기
    public List<Order> getAllByMember(MemberDto memberDto) {
        List<Order> orderList = orderRepository.findAllByBuyer(memberDto.toEntity());
        return orderList;
    }

    public List<OrderItem> findAllByPayDateBetweenOrderByIdAsc(LocalDateTime fromDate, LocalDateTime toDate) {
        return orderItemRepository.findAllByPayDateBetween(fromDate, toDate);
    }
    public List<OrderItem> findAllByRebateAndPayDateBetweenOrderByIdAsc(boolean rebate, LocalDateTime fromDate, LocalDateTime toDate) {
        List<OrderItem> orderItems = findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);
        List<OrderItem> result = orderItems.stream().filter(i -> i.isRebate() == rebate).toList();
        for(OrderItem orderItem : result) {
            orderItem.setRebate(true);
            orderItemRepository.save(orderItem);
        }
        return result;
    }
}