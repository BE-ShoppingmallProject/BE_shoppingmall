package com.github.shoppingmall.shopping_mall.repository.Mypage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserImgFileRepository extends JpaRepository<UserImgFile, Integer> {
}
