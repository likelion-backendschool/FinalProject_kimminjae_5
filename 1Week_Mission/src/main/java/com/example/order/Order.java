package com.example.order;

import com.example.member.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "product_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @ManyToOne(fetch = LAZY)
    private Member buyer;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);

        orderItems.add(orderItem);
    }

    public int calculatePayPrice() {
        int payPrice = 0;

        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getPayPrice();
        }

        return payPrice;
    }

    public void setPaymentDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setPaymentDone();
        }
    }

    public void setRefundDone() {
        for (OrderItem orderItem : orderItems) {
            orderItem.setRefundDone();
        }
    }

    public int getPayPrice() {
        int payPrice = 0;
        for (OrderItem orderItem : orderItems) {
            payPrice += orderItem.getPayPrice();
        }

        return payPrice;
    }
}
