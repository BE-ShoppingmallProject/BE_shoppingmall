package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.Order.Order;
import com.github.shoppingmall.shopping_mall.repository.userDetails.CustomUserDetails;
import com.github.shoppingmall.shopping_mall.repository.users.User;
import com.github.shoppingmall.shopping_mall.service.CartService;
import com.github.shoppingmall.shopping_mall.service.MyPageService;
import com.github.shoppingmall.shopping_mall.service.OrderService;
import com.github.shoppingmall.shopping_mall.service.auth.AuthService;
import com.github.shoppingmall.shopping_mall.service.exceptions.NotFoundException;
import com.github.shoppingmall.shopping_mall.web.dto.cart.CartDetailDto;
import com.github.shoppingmall.shopping_mall.web.dto.user.UserInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name="Mypages", description = "마이페이지 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPageController {
    private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);

    private final MyPageService myPageService;
    private final OrderService orderService;
    private final CartService cartService;

    @GetMapping("/myPage/cart")
    @Operation(summary = "장바구니 상품 조회")
    public String orderHistory(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model){
        List<CartDetailDto> cartDetailList = cartService.getCartList(customUserDetails.getUserId());
        model.addAttribute("cartItems", cartDetailList);
        return "cart/cartList";
    }

    @Operation(summary = "유저 정보 조회")
    @GetMapping("/myPage/userInfo")
    public ResponseEntity<UserInfo> userInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        UserInfo userInfo = myPageService.getUserInfo(customUserDetails.getUserId());

        return ResponseEntity.ok().body(userInfo);
    }

}
