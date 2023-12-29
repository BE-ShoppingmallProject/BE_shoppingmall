package com.github.shoppingmall.shopping_mall.web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String name;
    private String email;
    private String nickname;
    private String phoneNumber;

}
