package com.jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // single table이라서 구분할 수 있는 컬럼 주는
@Getter @Setter
public class Book extends Item {
    private String author;
    private String isbn;
}
