package com.github.shoppingmall.shopping_mall.repository.Post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Integer> {
    List<PostFile> findByPost_PostIdAndDelegateThumbNailAndIsDeleted(Integer postId, char delegateThumbNail, char isDeleted);

    List<PostFile> findByPost(Post post);
}
