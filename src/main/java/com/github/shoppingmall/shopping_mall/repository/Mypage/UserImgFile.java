package com.github.shoppingmall.shopping_mall.repository.Mypage;

import com.github.shoppingmall.shopping_mall.repository.users.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "USER_IMG_FILE")
public class UserImgFile {
    @Id
    @Column(name = "user_img_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userImgId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "origin_file_name")
    private String originFileName;

    @Column(name = "stored_file_name")
    private String storedFileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_size")
    private String fileSize;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "is_deleted")
    private Character isDeleted;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "delete_date")
    private Timestamp deleteDate;
}
