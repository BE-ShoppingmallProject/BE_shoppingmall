package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.SellerItem.SellerItemRequest;
import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.service.SellerItemService;
import jakarta.websocket.server.PathParam;
import org.apache.tomcat.util.http.parser.HttpParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SellerItemController {
    @Autowired
    private SellerItemService sellerItemService;

    @PostMapping("/seller/add")
    public ResponseEntity<Item> addSellerItem(@RequestBody SellerItemRequest request){
    /*
        Item addItem = sellerItemService.addItem(
                request.getSellerItem(),
                request.getOptions(),
                request.getStockItem());
        return new ResponseEntity<>(addItem, HttpStatus.CREATED);
     */
        return new ResponseEntity<>(new Item(), HttpStatus.CREATED);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<Item>> getItemBySeller(@PathVariable int sellerId ){
    //    List<Item> sellerItems = sellerItemService.getSellerItemsBySeller(sellerId);
    //    return new ResponseEntity<>(sellerItems, HttpStatus.OK);
        List<Item> sellerItems = new ArrayList<>();
        return new ResponseEntity<>(sellerItems, HttpStatus.OK);
    }

    @PutMapping("/seller/{itemId}")
    public ResponseEntity<Item> updateItem(@PathVariable int itemId, @RequestBody Item sellerItem){
    /*    Item updatedItem = sellerItemService.updateItem(itemId, sellerItem);
        return new ResponseEntity<>(updatedItem, HttpStatus.OK);
    */
        return new ResponseEntity<>(new Item(), HttpStatus.OK);
    }

}