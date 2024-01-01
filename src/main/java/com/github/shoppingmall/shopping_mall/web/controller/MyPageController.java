package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.Order.Order;
import com.github.shoppingmall.shopping_mall.repository.userDetails.CustomUserDetails;
import com.github.shoppingmall.shopping_mall.service.MyPageService;
import com.github.shoppingmall.shopping_mall.service.OrderService;
import com.github.shoppingmall.shopping_mall.service.auth.AuthService;
import com.github.shoppingmall.shopping_mall.web.dto.user.UserInfo;
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
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPageController {
    private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);

    private final MyPageService myPageService;
    private final OrderService orderService;

//    @GetMapping("/myPage/{user_id}")
//    public String myPage(@PathVariable("user_id") Integer userId, Model model,
//                         @AuthenticationPrincipal CustomUserDetails customUserDetails){
//        logger.info("mypage 조회요청");
//
//        List<Order> orders = orderService.findByUserId(userId);
//
//        model.addAttribute("orders", orders);
//        return "/myPage";
//    }

    @GetMapping("/myPage/userInfo")
    public ResponseEntity<UserInfo> userInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        UserInfo userInfo = myPageService.getUserInfo(customUserDetails.getUserId());

        return ResponseEntity.ok().body(userInfo);
    }

}
