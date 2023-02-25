package com.jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    // * 관심사의 분리 *
    // OrderQueryRepository: 특정 화면에 최적화된 쿼리는 여기에 작성
    // OrderRepository: 핵심 비즈니스 로직에 관련

    /**
     * 컬렉션은 별도로 조회
     * Query: 루트 1번, 컬렉션 N 번 * 단건 조회에서 많이 사용하는 방식
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        // ToOne 관계를 먼저 조회하고,
        List<OrderQueryDto> result =  findOrders();
        // ToMany(1:N) 관계는  loop 돌려서 각각 별도로 컬렉션 추가한다.
        // -> ToMany 는 조인하면 row 수가 증가하기 때문에 최적화 하기 어려워서 findOrderItems() 같은 별도의 메서드로 조회한다.
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new com.jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new com.jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

}
