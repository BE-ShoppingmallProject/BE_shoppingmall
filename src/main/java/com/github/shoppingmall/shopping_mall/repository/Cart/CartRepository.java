package com.github.shoppingmall.shopping_mall.repository.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Cart findByUserUserId(Integer userId); // 현재 로그인한 회원 장바구니 찾기
}
