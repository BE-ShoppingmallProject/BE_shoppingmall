package com.github.shoppingmall.shopping_mall.repository.users;


import com.github.shoppingmall.shopping_mall.repository.user_roles.UserRoles;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EqualsAndHashCode(of = "userId")
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "gender", nullable = false)
    private String gender;
/* -> USER_DELIVERY_ADDR 테이블로 이동
    @Column(name = "address", nullable = false)
    private String address;
*/
    @Column(name = "login_failure_cnt", nullable = false, columnDefinition = "int DEFAULT 0")
    private Integer loginFailureCnt;

    @Column(name = "is_locked", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String isLocked;

    @Column(name = "remaining_lock_time", nullable = false)
    private Integer remainingLockTime;

    @Column(name = "sell_role", nullable = false, columnDefinition = "CHAR(1) DEFAULT 'N'")
    private String sellRole;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE; // 회원 상태 (ACTIVE, DELETED)

    @Column(name = "profile_image_path")
    private String profileImagePath;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @OneToMany(mappedBy = "user")
    private Collection<UserRoles> userRoles;

}
