package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemRepository;
import com.github.shoppingmall.shopping_mall.repository.userDetails.CustomUserDetails;
import com.github.shoppingmall.shopping_mall.service.CartService;
import com.github.shoppingmall.shopping_mall.service.OrderService;
import com.github.shoppingmall.shopping_mall.web.dto.cart.*;
import com.github.shoppingmall.shopping_mall.web.dto.order.OrderDetailDto;
import com.github.shoppingmall.shopping_mall.web.dto.order.OrderRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name="Carts", description = "장바구니 페이지 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final OrderService orderService;
    private final ItemRepository itemRepository;

    @PostMapping("/cart/add")
    @Operation(summary = "장바구니")
    public ResponseEntity<Integer> addCart(@RequestBody CartItemDto cartItemDto,
                                        @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        Integer cartItemId = cartService.addCart(cartItemDto, customUserDetails.getUserId());
        return ResponseEntity.ok(cartItemId);
    }


    @GetMapping("/cart/validate/{cartItemId}")
    public ResponseEntity<Boolean> validateCartProduct(@PathVariable Integer cartItemId,
                                                       @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        boolean isValid = cartService.validateCartItem(cartItemId, customUserDetails.getUserId());
        return ResponseEntity.ok(isValid);
    }

    @PutMapping("/cart/updateCount/{cartItemId}")
    public ResponseEntity<String> updateCartProductCount(@PathVariable Integer cartItemId,
                                                       @RequestParam(value = "count") Integer count) {
        cartService.updateCartItemCount(cartItemId, count);
        return ResponseEntity.ok("물품 개수가" + count + "개로 변경되었습니다.");
    }

    @DeleteMapping("/cart/delete/{cartItemId}")
    public ResponseEntity<String> deleteCartProduct(@PathVariable Integer cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }


    @PostMapping("/cart/order")
    public ResponseEntity<String> createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody OrderRequestDto orderRequestDto) {
        Integer userId = customUserDetails.getUserId();
        List<OrderDetailDto> orderDetails = orderRequestDto.getOrderDetails();
        orderService.createOrder(orderRequestDto.getOrderDto(), orderDetails, userId);

        return ResponseEntity.ok("주문 및 결제가 처리되었습니다.");
    }
}
