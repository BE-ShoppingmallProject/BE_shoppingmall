package com.github.shoppingmall.shopping_mall.repository.Post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "post_file")
public class PostFile {
    @Id
    @Column(name = "post_file_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postFileId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private Post post;

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
    private Integer fileSize;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "delegate_thumb_nail")
    private Character delegateThumbNail;

    @Column(name = "is_deleted")
    private Character isDeleted;

    @Column(name = "create_date", updatable = false)
    @CreationTimestamp
    private Timestamp createDate;

    @Column(name = "delete_date", insertable = false)
    private Timestamp deleteDate;
}
