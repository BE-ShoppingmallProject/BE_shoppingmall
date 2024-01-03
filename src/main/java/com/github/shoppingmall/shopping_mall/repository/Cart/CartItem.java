package com.github.shoppingmall.shopping_mall.repository.Cart;

import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemOption;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.StockItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SHOPPING_CART_ITEM")
public class CartItem {
    @Id
    @Column(name = "cart_item_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId; // 장바구니에 들어있는 상품 ID

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart; // 장바구니 ID

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item; // 아이템 ID

    @ManyToOne
    @JoinColumn(name = "option_id")
    private ItemOption itemOption; // 옵션 ID

    @Column(name = "quantity")
    private Integer quantity; // 수량

//    private Integer price; // 가격
//
//    private Integer count;

    public static CartItem createCartItem(Cart cart, Item item, ItemOption itemOption, Integer quantity){ // 카트 아이템 생성
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setItemOption(itemOption);
        cartItem.setQuantity(quantity);

        return cartItem;
    }

//    public void setPrice(Item item){
//        price = quantity * item.getUnitPrice();
//    }

    public void updateCount(Integer quantity){ // 장바구니 상품 수량 변경
        this.quantity = quantity;
    }

    public void addCount(Integer quantity){
        this.quantity += quantity;
    }
}
