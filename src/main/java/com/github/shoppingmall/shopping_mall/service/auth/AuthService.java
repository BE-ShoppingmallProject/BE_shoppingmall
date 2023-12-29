package com.github.shoppingmall.shopping_mall.service.auth;

import com.github.shoppingmall.shopping_mall.config.security.JwtTokenProvider;
import com.github.shoppingmall.shopping_mall.repository.user_roles.Roles;
import com.github.shoppingmall.shopping_mall.repository.user_roles.RolesRepository;
import com.github.shoppingmall.shopping_mall.repository.user_roles.UserRoles;
import com.github.shoppingmall.shopping_mall.repository.user_roles.UserRolesRepository;
import com.github.shoppingmall.shopping_mall.repository.users.User;
import com.github.shoppingmall.shopping_mall.repository.users.UserRepository;
import com.github.shoppingmall.shopping_mall.repository.users.UserStatus;
import com.github.shoppingmall.shopping_mall.service.exceptions.NotFoundException;
import com.github.shoppingmall.shopping_mall.service.exceptions.RecordExistsException;
import com.github.shoppingmall.shopping_mall.service.exceptions.ValidationException;
import com.github.shoppingmall.shopping_mall.service.mapper.UserMapper;
import com.github.shoppingmall.shopping_mall.web.dto.auth.LoginDto;
import com.github.shoppingmall.shopping_mall.web.dto.auth.SignUp;
import com.github.shoppingmall.shopping_mall.web.dto.auth.SignUpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserRolesRepository userRolesRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    public String signUp(SignUp signUpRequest) {
        String email = signUpRequest.getEmail();
        String password = signUpRequest.getPassword();
        String name = signUpRequest.getName();
        String nickname = signUpRequest.getNickname();
        String phoneNumber = signUpRequest.getPhoneNumber();
        String gender = signUpRequest.getGender();
        String address = signUpRequest.getAddress();
        String profileImagePath = signUpRequest.getProfileImagePath();

        if (!email.matches(".+@.+\\..+")) {
            throw new ValidationException("이메일을 양식을 지켜주세요.");
        } else if (!phoneNumber.matches("010\\d{8}")) {
            throw new ValidationException("핸드폰 번호를 정활히 기입해주세요.");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RecordExistsException("해당 이메일로 가입한 게정이 존재합니다.");
        } else if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$")
                || !(password.length() >= 8 && password.length() <= 20)
        ) {
            throw new ValidationException("비밀번호는 8자 이상 20자 이하 숫자와 영문자 조합 이어야 합니다.");
        }

        signUpRequest.setPassword(passwordEncoder.encode(password));

        Roles roles = rolesRepository.findByName("ROLE_NORMAL")
                .orElseThrow(() -> new NotFoundException("ROLE_NORMAL 을 찾을 수 없습니다."));

        User user = UserMapper.INSTANCE.signUpRequestToUserEntity(signUpRequest);

        String saveProfileImage = saveProfileImage(profileImagePath);
        user.setProfileImagePath(saveProfileImage);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        userRolesRepository.save(UserRoles.builder()
                .user(user)
                .roles(roles)
                .build());

        SignUpResponse signUpResponse = UserMapper.INSTANCE.userEntityToSignUpResponse(user);

        return signUpResponse.getName() + "님 환영합니다!";

    }

    public List<String> login(LoginDto loginRequest) {

        String email = loginRequest.getEmail();
        User user = new User();

        if (email.matches(".+@.+\\..+")) {
            user = userRepository.findByEmailFetchJoin(email).orElseThrow(() -> new NotFoundException("해당 이메일로 가입된 회원이 없습니다."));
        }

        if ("DELETED".equals(user.getStatus())) {
            throw new RuntimeException("탈퇴한 회원입니다.");
        }


        try {
            String userEmail = user.getEmail();
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);

            List<String> roles = user.getUserRoles().stream()
                    .map((userRoles -> userRoles.getRoles()))
                    .map(role -> role.getName())
                    .collect(Collectors.toList());
            List<String> token = Arrays.asList(jwtTokenProvider.createToken(userEmail, roles), user.getName());
            return token;
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }

    }

    public void deactivateAccount(Integer userId) {
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("사용자를 찾을 수 없습니다."));
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    // 프로필 이미지를 저장하는 메서드
    private String saveProfileImage(String profileImagePath) {
        if (profileImagePath == null || profileImagePath.isEmpty()) {
            String defaultImagePath = "/static/images/default_profile_img.png";
            return defaultImagePath;
        }

        // 프로필 이미지가 제공되면 해당 이미지의 경로를 그대로 사용한다.
        return profileImagePath;
    }
}
