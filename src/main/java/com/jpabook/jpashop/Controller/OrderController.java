package com.jpabook.jpashop.Controller;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderSearch;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.service.ItemService;
import com.jpabook.jpashop.service.MemberService;
import com.jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping(value ="/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items= itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping (value ="/order")
    public String order(@RequestParam("memberId") Long memberId, // @RequestParam..!!
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count); // controller에서 직접 다 찾지 마세용.. 식별자만 받아오시오.. service 들어가서 안에서 entity를 찾아야 더 할 수 있는게 많아오
        return "redirect:/orders";
    }

    @GetMapping(value ="/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) { // ModelAttribute : 사용자가 요청하면서 전달하는 값 오브젝트로 매핑해줌 (검색조건 넘어옴)
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId")Long orderId) {
        orderService.cancelOrder(orderId);
        return "/redirect:/orders";
    }
}
