package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    // @Embedded : 내장타입을 사용했다는 표시
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // mappedBy : 연관관계 거울 (orders table 에 있는 필드에 의해 연관관계 된것이당)
    private List<Order> orders = new ArrayList<>(); // collection 은 바로 초기화해놓고 바꾸지말기.. => 하이버네이트가 제공하는 내장 컬렉션으로 변경해서
}
