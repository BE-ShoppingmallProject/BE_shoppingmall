package com.github.shoppingmall.shopping_mall.repository.user_pay;


import com.github.shoppingmall.shopping_mall.repository.users.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "user_pay")
public class UserPay {
    @Id
    @Column(name = "user_id", nullable = false, unique = true)
    private Integer userId;

    @Column(name = "account", nullable = false, columnDefinition = "bigint default 0")
    private Integer account;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
