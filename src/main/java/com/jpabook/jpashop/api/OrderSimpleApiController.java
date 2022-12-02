package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderSearch;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
        return all; // member -> orders -> member -> ... infinity loop
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        // ORDER 2개
        // N + 1 문제 => 1 + 회원 N(2) + 배송 N
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // 2개
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        return orderSimpleQueryRepository.findOrderDtos(); // dto 조회는 재활용 불가능
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // getMember : LAZY 초기화 = 영속성 컨텍스트가 id로 찾아봄 => select
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}
