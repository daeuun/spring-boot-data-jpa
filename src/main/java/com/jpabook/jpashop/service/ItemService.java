package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.item.Book;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, UpdateItemDto dto) {
        Item findItem = itemRepository.findOne(itemId); // id를 기반으로 실제 DB에 있는 영속상태 Item 찾아옴
        //findItem.change(price, name, stockQuantity); // 의미있는 메서드를 entity에 만들어서 사용하기 (추적하기 쉽게..)
        findItem.setName(dto.getName());
        findItem.setPrice(dto.getPrice());
        findItem.setStockQuantity(dto.getStockQuantity());
        //itemRepository.save(findItem);
        // * 변경감지
        // 여기서 findItem은 영속상태라서 값 세팅한 다음에 @Transactional에 의해 커밋 => flush() 날려서 영속성 entity 중에 변경된 사항 모두 찾아냄 => update
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
