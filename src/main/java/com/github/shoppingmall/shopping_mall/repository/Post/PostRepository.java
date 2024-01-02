package com.github.shoppingmall.shopping_mall.repository.Post;

import io.github.classgraph.AnnotationInfoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    Page<Post> findByUserId(Integer userId, Pageable pageable);

    Page<Post> findByIsDeleted(char isDeleted, Pageable pageable);

    Page<Post> findByUserIdAndIsDeleted(Integer userId, char isDeleted, Pageable pageable);

    Optional<Post> findByPostIdAndIsDeleted(Integer postId, char isDeleted);

    @Query("SELECT i FROM Post i LEFT JOIN FETCH i.item LEFT JOIN FETCH i.postFiles " +
            "WHERE ( (:type = 'name' AND i.title LIKE %:keyword%) " +
            "OR (:type = 'content' AND i.content LIKE %:keyword%) ) " +
            "AND i.isDeleted = 'N' ")
    Page<Post> searchByTypeWithDetails(@Param("type") String type,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.item i WHERE i.categoryId = :categoryId AND p.isDeleted = 'N'")
    Page<Post> findByCategoryId(@Param("categoryId") Integer categoryId, Pageable pageable);

}
