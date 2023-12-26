package com.github.shoppingmall.shopping_mall.service;

import com.github.shoppingmall.shopping_mall.repository.Item.*;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.*;
import com.github.shoppingmall.shopping_mall.web.dto.seller.SellerItemRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Service
@Slf4j
public class SellerItemService {
    private final ItemRepository itemRepository;
    private final StockItemRepository stockItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(SellerItemService.class);
    @Transactional("tmJpa1")
    public Item addItem(SellerItemRequestDto requestDto) {

        logger.error("test");
        logger.error("userid : "+requestDto.getUserId());
        logger.error("categoryId : " + requestDto.getCategoryId());
        logger.error("unitPrice : " + requestDto.getUnitPrice());
        logger.error("quntity : " + requestDto.getQuantity());
        logger.error("itemName : " + requestDto.getItemName());
        logger.error("itemExplain : " + requestDto.getItemExplain());
        logger.error("startDate : " + requestDto.getStartDate() );
        logger.error("endDate : " + requestDto.getEndDate() );

        // Item 생성 및 저장
        Item item = new Item();

        // itemDto로부터 item 필드 설정
        item.setUserId(requestDto.getUserId());
        item.setCategoryId(requestDto.getCategoryId());
        item.setUnitPrice(requestDto.getUnitPrice());
        item.setItemName(requestDto.getItemName());
        item.setItemExplain(requestDto.getItemExplain());
        itemRepository.save(item);

        // StockItem 생성 및 저장
        StockItem stockItem = new StockItem();
        stockItem.setUserId(requestDto.getUserId());
        stockItem.setItem(item);
        stockItem.setQuantity(requestDto.getQuantity());
        Timestamp start_ts = Timestamp.valueOf(requestDto.getStartDate());
        Timestamp end_ts = Timestamp.valueOf(requestDto.getEndDate());
        stockItem.setStartDate(start_ts);
        stockItem.setEndDate(end_ts);

        logger.error("startDate(timestamp) : " + stockItem.getStartDate() );
        logger.error("endDate(timestamp) : " + stockItem.getEndDate() );
        stockItemRepository.save(stockItem);

        return item;
    }

}
