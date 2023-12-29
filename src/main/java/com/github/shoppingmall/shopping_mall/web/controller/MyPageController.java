package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.Order.Order;
import com.github.shoppingmall.shopping_mall.service.MyPageService;
import com.github.shoppingmall.shopping_mall.service.auth.AuthService;
import com.github.shoppingmall.shopping_mall.web.dto.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;
    private final AuthService authService;
    // Todo. private final OrderService orderService;

//    @GetMapping("/myPage/{user_id}")
//    public String myPage(@PathVariable("user_id") Integer userId, Model model){
//        List<Order> orders = orderService.findByUserId(userId);
//
//        model.addAttribute("orders", orders);
//        return "/myPage";
//    }

//    @GetMapping("/myPage/userInfo")
//    public ResponseEntity<UserInfo> userInfo(){
//        UserInfo userInfo = myPageService.getUserInfo(authInfo.getUserId());
//
//        return ResponseEntity.ok().body(userInfo);
//    }

}
