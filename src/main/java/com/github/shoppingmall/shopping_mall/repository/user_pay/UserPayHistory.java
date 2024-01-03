package com.github.shoppingmall.shopping_mall.repository.user_pay;


import com.github.shoppingmall.shopping_mall.repository.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "USER_PAY_HISTORY")
public class UserPayHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_pay_history_id")
    private Integer userPayHistoryId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "usage_date")
    private Timestamp usageDate;

    @Column(name = "recharge_date")
    private Timestamp rechargeDate;

    @Column(name = "account_charge")
    private Integer accountCharge;
}
