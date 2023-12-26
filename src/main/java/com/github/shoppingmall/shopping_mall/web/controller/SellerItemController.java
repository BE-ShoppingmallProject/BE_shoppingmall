package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.service.SellerItemService;
import com.github.shoppingmall.shopping_mall.web.dto.seller.SellerItemRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class SellerItemController {
    private final SellerItemService sellerItemService;
    private static final Logger logger = LoggerFactory.getLogger(SellerItemController.class);

    @PostMapping("/seller/add")
    public ResponseEntity<Item> addSellerItem(@RequestBody SellerItemRequestDto requestDto){
        Item addItem = sellerItemService.addItem( requestDto );
        return new ResponseEntity<>(addItem, HttpStatus.CREATED);
        /*
        {
            "userId": 1,
            "categoryId": 1,
            "unitPrice": 1000,
            "quantity": 100,
            "itemName": "test",
            "itemExplain": "테스트중입니다. - 정재화 -",
            "startDate": "2023-12-27 01:24:23",
            "endDate": "2023-12-31 23:00:00"
        }
         */
    }
/*
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Item>> getItemBySeller(@PathVariable int sellerId ){
    //    List<Item> sellerItems = sellerItemService.getSellerItemsBySeller(sellerId);
    //    return new ResponseEntity<>(sellerItems, HttpStatus.OK);
        List<Item> sellerItems = new ArrayList<>();
        return new ResponseEntity<>(sellerItems, HttpStatus.OK);
    }

    @PutMapping("/seller/{itemId}")
    public ResponseEntity<Item> updateItem(@PathVariable int itemId, @RequestBody Item sellerItem){
    //    Item updatedItem = sellerItemService.updateItem(itemId, sellerItem);
    //    return new ResponseEntity<>(updatedItem, HttpStatus.OK);

        return new ResponseEntity<>(new Item(), HttpStatus.OK);
    }
*/
}