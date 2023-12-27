package com.github.shoppingmall.shopping_mall.service;

import com.github.shoppingmall.shopping_mall.repository.Item.*;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.*;
import com.github.shoppingmall.shopping_mall.repository.users.User;
import com.github.shoppingmall.shopping_mall.repository.users.UserRepository;
import com.github.shoppingmall.shopping_mall.service.exceptions.NotFoundException;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class SellerItemService {
    private final ItemRepository itemRepository;
    private final StockItemRepository stockItemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(SellerItemService.class);

    @Transactional("tmJpa1")
    public Boolean addItemWithDetails(ItemCreationDto newItemDto) {
        Item item = convertToEntity( newItemDto.getItem() );
        logger.info("email : " + newItemDto.getItem().getEmail() );
        User user = userRepository.findByEmailFetchJoin(newItemDto.getItem().getEmail()).orElseThrow(() -> new NotFoundException("email에 해당하는 유저가 없습니다."));
        item.setUserId(user.getUserId());
        Item saveItem = itemRepository.save(item);
        if( saveItem == null ){
            logger.error("item save fail");
            return false;
        }

        List<ItemOption> optionList = new ArrayList<>();

        for(ItemOptionDto optionDto : newItemDto.getItemOptions()){
            ItemOption option = convertToEntity(optionDto);
            option.setItem(item);
            ItemOption saveOption = itemOptionRepository.save(option);
            if( saveOption == null ){
                logger.error("item option[" + optionDto.getContent() +"] save fail");
                return false;
            }
            optionList.add(saveOption);
        }

        for( int i = 0; i < newItemDto.getStockItems().size(); i++ ){

            StockItemDto stockItemDto = newItemDto.getStockItems().get(i);
            ItemOption itemOption = optionList.get(i);

            StockItem stockItem = convertToEntity( stockItemDto );
            stockItem.setItem(item);
            stockItem.setOption(itemOption);
            stockItem.setUser(user);

            StockItem saveStockItem = stockItemRepository.save(stockItem);
            if( saveStockItem == null ){
                logger.error("stock_item save fail");
                return false;
            }
        }

        return true;
    }

    private Item convertToEntity(ItemDto item) {
        Item newItem = new Item();
        newItem.setItemName(item.getItemName());
        newItem.setItemExplain(item.getDescription());
        newItem.setCategoryId(item.getCategoryId());
        newItem.setUnitPrice(item.getUnitPrice());
        return newItem;
    }

    private StockItem convertToEntity(StockItemDto stockItem) {
        StockItem newStockItem = new StockItem();
        newStockItem.setItemStatus(stockItem.getItemStatus());
        newStockItem.setQuantity(stockItem.getQuantity());
        Timestamp start_ts = Timestamp.valueOf(stockItem.getStartDate());
        Timestamp end_ts = Timestamp.valueOf(stockItem.getEndDate());
        newStockItem.setStartDate(start_ts);
        newStockItem.setEndDate(end_ts);
        return newStockItem;
    }

    private ItemOption convertToEntity(ItemOptionDto itemOption){
        ItemOption newItemOption = new ItemOption();
        newItemOption.setOptionContent(itemOption.getContent());
        newItemOption.setAdditionalPrice(itemOption.getAddPrice());
        return newItemOption;
    }

    @Transactional("tmJpa1")
    public boolean updateItemWithDetails(ItemCreationDto updateItemDto) {
        Item item = convertToEntity( updateItemDto.getItem() );
        logger.info("email : " + updateItemDto.getItem().getEmail() );
        User user = userRepository.findByEmailFetchJoin(updateItemDto.getItem().getEmail()).orElseThrow(() -> new NotFoundException("email에 해당하는 유저가 없습니다."));
        item.setUserId(user.getUserId());
        item.setItemId(updateItemDto.getItem().getItemId());
        Item saveItem = itemRepository.save(item);
        if( saveItem == null ){
            logger.error("item update fail");
            return false;
        }

        List<ItemOption> optionList = new ArrayList<>();

        for(ItemOptionDto optionDto : updateItemDto.getItemOptions()){
            ItemOption option = convertToEntity(optionDto);
            option.setItem(item);
            option.setOptionId(optionDto.getOptionId());
            ItemOption saveOption = itemOptionRepository.save(option);
            if( saveOption == null ){
                logger.error("item option[" + optionDto.getContent() +"] update fail");
                return false;
            }
            optionList.add(saveOption);
        }

        for( int i = 0; i < updateItemDto.getStockItems().size(); i++ ){

            StockItemDto stockItemDto = updateItemDto.getStockItems().get(i);
            ItemOption itemOption = optionList.get(i);

            StockItem stockItem = convertToEntity( stockItemDto );
            stockItem.setItem(item);
            stockItem.setOption(itemOption);
            stockItem.setUser(user);
            stockItem.setStockId(stockItemDto.getStockId());

            StockItem saveStockItem = stockItemRepository.save(stockItem);
            if( saveStockItem == null ){
                logger.error("stock_item update fail");
                return false;
            }
        }

        return true;
    }
    @Transactional("tmJpa1")
    public Page<Item> searchItems(String type, String keyword, Pageable pageable) {
        if( type == null || type.isEmpty() || keyword == null || keyword.isEmpty() ) {
            return itemRepository.findAll(pageable);
        }
        return itemRepository.searchByTypeWithDetails(type, keyword, pageable);
    }
}
