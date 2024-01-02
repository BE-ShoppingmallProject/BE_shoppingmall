package com.github.shoppingmall.shopping_mall.web.dto.user;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String name; // 유저 이름
    private String email; // 유저 이메일
    private String nickname; // 유저 닉네임
    private String phoneNumber; // 유저 폰번호
    private String imgUrl; // 썸네일 이미지 파일 경로

}
