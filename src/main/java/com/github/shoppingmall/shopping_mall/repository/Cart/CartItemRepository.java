package com.github.shoppingmall.shopping_mall.repository.Cart;

import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.web.dto.cart.CartDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    CartItem findByCartCartIdAndItemItemId(Integer cartId, Integer itemId); // 상품이 장바구니에 들어있는지 조회

    @Query("SELECT new com.github.shoppingmall.shopping_mall.web.dto.cart.CartDetailDto(ci.cartItemId, i.itemName, i.unitPrice, ci.quantity, pf.filePath) " +
            "FROM CartItem ci, PostFile pf " +
            "JOIN ci.item i " +
            "WHERE ci.cart.cartId = :cartId " +
            "AND pf.itemId = ci.item.itemId " +
            "AND pf.delegateThumbNail = 'Y'")
    List<CartDetailDto> findCartDetailDtoList(Integer cartId);

    void deleteByCartUserUserId(Integer userId);

    @Query("SELECT ci.item FROM CartItem ci where ci.cartItemId = :cartItemId and ci.cart.cartId = :cartId")
    Item findItem(@Param("cartItemId") Integer cartItemId, @Param("cartId") Integer cartId);
}
