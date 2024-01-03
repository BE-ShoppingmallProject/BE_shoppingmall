package com.github.shoppingmall.shopping_mall.repository.Cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUser_UserId(Integer userId);


}
