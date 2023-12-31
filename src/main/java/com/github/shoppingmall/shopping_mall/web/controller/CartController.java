package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.userDetails.CustomUserDetails;
import com.github.shoppingmall.shopping_mall.service.CartService;
import com.github.shoppingmall.shopping_mall.service.OrderService;
import com.github.shoppingmall.shopping_mall.web.dto.cart.CartDetailDto;
import com.github.shoppingmall.shopping_mall.web.dto.cart.CartItemDto;
import com.github.shoppingmall.shopping_mall.web.dto.order.OrderDetailDto;
import com.github.shoppingmall.shopping_mall.web.dto.order.OrderDto;
import com.github.shoppingmall.shopping_mall.web.dto.order.OrderRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    private final OrderService orderService;

    @PostMapping("/cart") // 상품 장바구니 담기
    public @ResponseBody ResponseEntity cart(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult,
                                             @AuthenticationPrincipal CustomUserDetails customUserDetails){
        if (bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Integer cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, customUserDetails);
        }catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Integer>(cartItemId, HttpStatus.OK);
    }

//    @GetMapping("/cart")
//    public String orderHistory(Principal principal, Model model){
//        List<CartDetailDto> cartDetailList = cartService.getCartList(principal.getName());
//        model.addAttribute("cartItems", cartDetailList);
//        return "cart/cartList";
//    }

    @PatchMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Integer cartItemId, Integer count, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        if(count <= 0){
            return new ResponseEntity<String >("최소 1개 이상 담아주세요", HttpStatus.BAD_REQUEST);
        } else if (!cartService.validateCartItem(cartItemId, customUserDetails)) {
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Integer>(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping("/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Integer cartItemId, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        if(!cartService.validateCartItem(cartItemId, customUserDetails)){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Integer>(cartItemId, HttpStatus.OK);
    }

    @PostMapping("/cart/order")
    public ResponseEntity<String> createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails, @RequestBody OrderRequestDto orderRequestDto) {
        Integer userId = customUserDetails.getUserId();
        List<OrderDetailDto> orderDetails = orderRequestDto.getOrderDetails();
        orderService.createOrder(orderRequestDto.getOrderDto(), orderDetails, userId);

        return ResponseEntity.ok("주문 및 결제가 처리되었습니다.");
    }
}
