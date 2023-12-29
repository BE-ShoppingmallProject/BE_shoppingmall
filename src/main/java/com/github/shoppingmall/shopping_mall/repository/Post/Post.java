package com.github.shoppingmall.shopping_mall.repository.Post;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Table(name = "POST")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId; // 게시글 ID

    @Column(name = "title", nullable = false)
    private String title; // 게시글 제목

    @Column(name = "content", nullable = false)
    private String content; // 게시글 내용

    @Column(name = "user_id", nullable = false)
    private Integer userId; // 회원 ID
/*
    @Column(name = "item_id", nullable = false)
    private Integer itemId;
*/
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="item_id", nullable = false)
    private Item item;

    @Column(name = "view_cnt")
    private Integer viewCnt;

    @Column(name = "is_deleted", nullable = true)
    private Character isDeleted;

    @Column(name = "create_date", updatable = false)
    @CreationTimestamp
    private Timestamp createDate;

    @Column(name = "update_date", insertable = false)
    @UpdateTimestamp
    private Timestamp updateDate;

    @Column(name = "delete_date", insertable = false)
    private Timestamp deleteDate;

    @OneToMany(mappedBy = "post")
    private Set<PostFile> postFiles;

    public Post(Post post) {
        this.setPostId(post.getPostId());
        this.setTitle(post.getTitle());
        this.setContent(post.getContent());
        this.setUserId(post.getUserId());
       // this.setItemId(post.getItemId());
        this.setViewCnt(post.getViewCnt());
        this.setIsDeleted(post.getIsDeleted());
        this.setCreateDate(post.getCreateDate());
        this.setUpdateDate(post.getUpdateDate());
        this.setDeleteDate(post.getDeleteDate());
    }

    public void setPost(Post post) {
        this.setPostId(post.getPostId());
        this.setTitle(post.getTitle());
        this.setContent(post.getContent());
        this.setUserId(post.getUserId());
       // this.setItemId(post.getItemId());
        this.setViewCnt(post.getViewCnt());
        this.setIsDeleted(post.getIsDeleted());
        this.setCreateDate(post.getCreateDate());
        this.setUpdateDate(post.getUpdateDate());
        this.setDeleteDate(post.getDeleteDate());
    }
}
