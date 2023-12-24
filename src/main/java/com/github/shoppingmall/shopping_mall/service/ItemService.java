package com.github.shoppingmall.shopping_mall.service;

import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemService {
    private final ItemRepository itemRepository;

    // 쇼핑몰 전체 물품조회
    public List<Item> itemList(){
        return itemRepository.findAll();
    }

    // 쇼핑몰 상세 물품 조회
    public Item itemDetailView(int itemId){
        return itemRepository.findById(itemId).get();
    }
}
