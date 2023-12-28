package com.github.shoppingmall.shopping_mall.repository.Cart;

import com.github.shoppingmall.shopping_mall.repository.users.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "SHOPPING_CART")
public class Cart {
    @Id
    @Column(name = "cart_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId; // 장바구니 아이디

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user; // 유저 아이디

//    private int count; // 카트에 담긴 물품갯수

//    @OneToMany(fetch = FetchType.EAGER)
//    private List<CartItemEntity> cartItemEntities = new ArrayList<>();

    public static Cart createCart(User user){ // 카트 생성
        Cart cart = new Cart();
        cart.setUser(user);
        return cart;
    }

}
