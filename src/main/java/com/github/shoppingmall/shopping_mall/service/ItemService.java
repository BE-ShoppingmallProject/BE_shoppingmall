package com.github.shoppingmall.shopping_mall.service;

import com.github.shoppingmall.shopping_mall.repository.Item.*;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.StockItem;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.StockItemRepository;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.ItemCreationDto;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.ItemDto;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.ItemOptionDto;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.StockItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
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
