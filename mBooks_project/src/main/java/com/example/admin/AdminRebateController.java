package com.example.admin;

import com.example.base.dto.RsData;
import com.example.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
public class AdminRebateController {
    private final RebateService rebateService;


    //문제 해결 전까지 정산 데이터 수동 생성 주석처리
    //정산 데이터 생성 폼
//    @GetMapping("/makeData")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public String showMakeData() {
//        return "adm/rebate/makeData";
//    }

    //정산 데이터 생성 처리
//    @PostMapping("/makeData")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public String makeData(String yearMonth) {
//        System.out.println(yearMonth);
//        RsData makeDateRsData = rebateService.makeDate(yearMonth);
//
//        String redirect = makeDateRsData.addMsgToUrl("redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth);
//
//        return redirect;
//    }

    //정산 데이터 목록
    @GetMapping("/rebateOrderItemList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRebateOrderItemList(String yearMonth, Model model) {
        if (StringUtils.hasText(yearMonth) == false) {
            LocalDateTime now = LocalDateTime.now();
            int year = now.getYear();
            int month = now.getMonthValue();
            yearMonth = year + "-" + month;
        }
        List<RebateOrderItem> itemList = rebateService.findRebateOrderItemsByPayDateIn(yearMonth);
        model.addAttribute("items", itemList);

        return "adm/rebate/rebateOrderItemList";
    }

    //건별 정산
    @PostMapping("/rebateOne/{orderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebateOne(@PathVariable long orderItemId, HttpServletRequest req) {
        RsData rebateRsData = rebateService.rebate(orderItemId);

        String referer = req.getHeader("Referer");

        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        String redirect = "redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth;

        redirect = rebateRsData.addMsgToUrl(redirect);

        return redirect;
    }

    //선택 정산
    @PostMapping("/rebate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebate(String ids, HttpServletRequest req) {

        String[] idsArr = ids.split(",");

        Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .forEach(id -> {
                    rebateService.rebate(id);
                });

        String referer = req.getHeader("Referer");
        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        String redirect = "redirect:/adm/rebate/rebateOrderItemList?yearMonth=" + yearMonth;
        redirect += "&msg=" + Ut.url.encode("%d건의 정산품목을 정산처리하였습니다.".formatted(idsArr.length));

        return redirect;
    }
}
