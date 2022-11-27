package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @PersistenceContext EntityManager em;
    
    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("name");
        
        //when
        Long savedId = memberService.join(member);

        //then
        em.flush(); // flush: db에 쿼리가 나가서 insert 볼수는 있음 => Transactional 타고 Rolled back transaction for test
        assertEquals(member, memberRepository.findOne(savedId)); // 새로넣은 member == memberRepository에 저장된 member ?
    }

    @Test//(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("name");

        Member member2 = new Member();
        member2.setName("name");

        //when
        memberService.join(member1);
        memberService.join(member2); // 예외 발생

        //then
        fail("예외가 발생해야 한다.");
    }

}