package com.github.shoppingmall.shopping_mall.repository.Post;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "POST_FILE")
public class PostFile {
    @Id
    @Column(name = "post_file_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postFileId;

    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "item_id")
    private Integer itemId;

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

    @Column(name = "delegate_thumb_nail")
    private String delegateThumbNail;

    @Column(name = "is_deleted")
    private Character isDeleted;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "delete_date")
    private Timestamp deleteDate;
}
