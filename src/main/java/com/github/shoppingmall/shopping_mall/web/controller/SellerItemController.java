package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.service.SellerItemService;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.ItemCreationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class SellerItemController {
    private final SellerItemService sellerItemService;
    private static final Logger logger = LoggerFactory.getLogger(SellerItemController.class);

    @PostMapping("/seller/add")
    public ResponseEntity<Map<String,String>> addItem(@RequestBody ItemCreationDto itemCreationDto ) {
        logger.info("/api/seller/add");
        Map<String, String> response = new HashMap<>();
        logger.error("err" + itemCreationDto.toString() );

        boolean isSave = sellerItemService.addItemWithDetails(itemCreationDto);
        if( isSave == false ){
            response.put("message", "추가 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        else {
            response.put("message", "추가 완료");
            return ResponseEntity.ok(response);
        }

        /*

{
    "item": {
        "userId": 1,
        "categoryId": 1,
        "itemName": "Example Item",
        "description": "This is an example item.",
        "unitPrice": 25000
    },
    "itemOptions": [
        {
            "content": "대",
            "addPrice": 2000
        },
        {
            "content": "중",
            "addPrice": 1000
        },
        {
            "content": "소",
            "addPrice": 0
        }
    ],
    "stockItems": [
        {
            "quantity": 1000,
            "itemStatus": "open",
            "startDate": "2023-12-29 01:24:23",
            "endDate": "2023-12-31 23:00:00"
        },
        {
            "quantity": 2000,
            "itemStatus": "open",
            "startDate": "2023-12-29 01:24:23",
            "endDate": "2023-12-31 23:00:00"
        },
        {
            "quantity": 2000,
            "itemStatus": "open",
            "startDate": "2023-12-29 01:24:23",
            "endDate": "2023-12-31 23:00:00"
        }
    ]
}
         */
    }



    @PutMapping("/seller/update/")
    public ResponseEntity<Map<String,String>> updateItem(@RequestBody ItemCreationDto itemCreationDto ) {
        logger.info("/api/seller/update");
        Map<String, String> response = new HashMap<>();

        boolean isUpdate = sellerItemService.updateItemWithDetails(itemCreationDto);
        if (isUpdate == false) {
            response.put("message", "수정 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else {
            response.put("message", "수정 완료");
            return ResponseEntity.ok(response);
        }
    /*
    {
    "item": {
        "itemId": 5,
        "email": "peten@kakao.com",
        "categoryId": 1,
        "itemName": "테스트 아이템 명",
        "description": "수정 테스트 중 입니다.",
        "unitPrice": 40000
    },
    "itemOptions": [
        {
            "optionId": 7,
            "content": "대",
            "addPrice": 1999
        },
        {
            "optionId": 8,
            "content": "중",
            "addPrice": 999
        },
        {
            "optionId": 9,
            "content": "소",
            "addPrice": 242
        }
    ],
    "stockItems": [
        {
            "stockId": 2,
            "quantity": 1111,
            "itemStatus": "close",
            "startDate": "2023-12-29 01:24:23",
            "endDate": "2023-12-31 23:00:00"
        },
        {
            "stockId": 3,
            "quantity": 2222,
            "itemStatus": "close",
            "startDate": "2023-12-29 01:24:23",
            "endDate": "2023-12-31 23:00:00"
        },
        {
            "stockId": 4,
            "quantity": 3333,
            "itemStatus": "close",
            "startDate": "2023-12-29 01:24:23",
            "endDate": "2023-12-31 23:00:00"
        }
    ]
}
    */
    }




    @GetMapping("/seller/search")
    public ResponseEntity<Page<Item>> searchItems(@RequestParam(value="type", required = false) String type,
                                                  @RequestParam(value="keyword", required = false) String keyword,
                                                  @RequestParam(value="page", defaultValue = "0") int page,
                                                  @RequestParam(value="size", defaultValue = "30") int size) {
        logger.info("/api/seller/search");

        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = sellerItemService.searchItems(type, keyword, pageable);
        return ResponseEntity.ok(items);
    }

}