package com.github.shoppingmall.shopping_mall.web.controller.auth;


import com.github.shoppingmall.shopping_mall.service.auth.AuthService;
import com.github.shoppingmall.shopping_mall.web.dto.auth.LoginDto;
import com.github.shoppingmall.shopping_mall.web.dto.auth.SignUp;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/sign")
public class SignUpController {

    private final AuthService authService;

    @PostMapping(value = "/register")
    public String register(@RequestBody SignUp signUpRequest) {
        return authService.signUp(signUpRequest);
    }

    @PostMapping(value = "/login")
    public String login(@RequestBody LoginDto loginRequest, HttpServletResponse httpServletResponse) {
        List<String> login = authService.login(loginRequest);
        httpServletResponse.setHeader("TOKEN", login.get(0));
        return "로그인이 성공했습니다. " + login.get(1) + "님, 즐거운 시간 되세요!";
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deactivateAccount(@PathVariable Integer userId) {
        authService.deactivateAccount(userId);
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }
}
