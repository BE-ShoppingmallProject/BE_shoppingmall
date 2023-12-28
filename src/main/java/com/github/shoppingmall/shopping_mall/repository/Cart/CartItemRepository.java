package com.github.shoppingmall.shopping_mall.repository.Cart;

import com.github.shoppingmall.shopping_mall.web.dto.cart.CartDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    CartItem findByCartCartIdAndItemItemId(Integer cartId, Integer itemId); // 상품이 장바구니에 들어있는지 조회

    // TODO. SQL문 수정!!! (상품이미지 가져와야함...)
//    @Query("select new com.github.shoppingmall.shopping_mall.web.dto.cart.CartDetailDto(ci.id, i.itemName, i.price, ci.count, im.imgUrl)" +
//            "from CartItem ci, ItemImg im " + // TODO.아이템 이미지 가져오기!
//            "join ci.item i " +
//            "where ci.cart.cartId = :cartId " +
//            "and im.item.id = ci.item.itemId " +
//            "and im.repimgYn = 'Y' " +
//            "order by ci.regTime desc"
//    )
//    List<CartDetailDto> findCartDetailDtoList(Integer cartId);
}
