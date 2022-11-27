package com.jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

// @Embeddable : 애노테이션을 설정한 밸류 클래스는 다른 엔티티에 포함될 수 있다 (컬럼을 하나의 객체로 사용하기 위해 붙이는 어노테이션)
@Embeddable
@Getter // 값 타입은 변경 불가능하게 설계
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // jpa 스펙상 만들어놓는 생성자
   protected Address() {}

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
