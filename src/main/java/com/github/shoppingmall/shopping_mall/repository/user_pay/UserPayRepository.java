package com.github.shoppingmall.shopping_mall.repository.user_pay;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPayRepository extends JpaRepository<UserPay, Integer> {

    UserPay findByUserUserId(Integer userId);
}
