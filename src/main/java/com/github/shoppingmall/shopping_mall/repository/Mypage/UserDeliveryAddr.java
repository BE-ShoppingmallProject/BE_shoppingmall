package com.github.shoppingmall.shopping_mall.repository.Mypage;

import com.github.shoppingmall.shopping_mall.repository.users.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "USER_DELIVERY_ADDR")
public class UserDeliveryAddr {
    @Id
    @Column(name = "delivery_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer deliveryId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "zipcode")
    private String zipCode;

    @Column(name = "address")
    private String address;

    @Column(name = "address_detail")
    private String addressDetail;
}
