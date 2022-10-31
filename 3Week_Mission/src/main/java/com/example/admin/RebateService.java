package com.example.admin;

import com.example.order.OrderItem;
import com.example.order.OrderService;
import com.example.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RebateService {
    private final RebateOrderItemRepository rebateOrderItemRepository;
    private final OrderService orderService;

    public void makeDate(String yearMonth) {
        int monthEndDay = Ut.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.date.parse(fromDateStr);
        LocalDateTime toDate = Ut.date.parse(toDateStr);

        //데이터 가져오기
        List<OrderItem> orderItemList = orderService.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);

        //변환
        List<RebateOrderItem> rebateOrderItems = orderItemList
                .stream()
                .map(this::toRebateOrderItem)
                .collect(Collectors.toList());
        //저장
        rebateOrderItems.forEach(this::makeRebateOrderItem);
    }

    private void makeRebateOrderItem(RebateOrderItem rebateOrderItem) {
        RebateOrderItem oldRebateOrderItem = rebateOrderItemRepository.findByOrderItemId(rebateOrderItem.getOrderItem().getId()).orElse(null);
    }

    private RebateOrderItem toRebateOrderItem(OrderItem orderItem) {
        return new RebateOrderItem(orderItem);
    }

    public List<RebateOrderItem> findRebateOrderItemsByPayDateIn(String yearMonth) {
        int monthEndDay = Ut.date.getEndDayOf(yearMonth);

        String fromDateStr = yearMonth + "-01 00:00:00.000000";
        String toDateStr = yearMonth + "-%02d 23:59:59.999999".formatted(monthEndDay);
        LocalDateTime fromDate = Ut.date.parse(fromDateStr);
        LocalDateTime toDate = Ut.date.parse(toDateStr);

        return rebateOrderItemRepository.findAllByPayDateBetweenOrderByIdAsc(fromDate, toDate);
    }
}
