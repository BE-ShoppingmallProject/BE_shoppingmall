package com.github.shoppingmall.shopping_mall.service;

import com.github.shoppingmall.shopping_mall.repository.users.User;
import com.github.shoppingmall.shopping_mall.repository.users.UserRepository;
import com.github.shoppingmall.shopping_mall.web.dto.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;

    public UserInfo getUserInfo(Integer userId){
        Optional<User> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(()->new RuntimeException("유저 없음")));

        if(user.isPresent()){
            String name = user.get().getName();
            String email = user.get().getEmail();
            String nickname = user.get().getNickname();
            String phoneNumber = user.get().getPhoneNumber();

            return new UserInfo(name, email, nickname, phoneNumber);
        }else throw new RuntimeException("신규 회원이면 장바구니 조회 안 됨.");
    }
}

