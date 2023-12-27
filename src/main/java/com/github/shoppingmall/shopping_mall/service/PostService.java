package com.github.shoppingmall.shopping_mall.service;

import com.github.shoppingmall.shopping_mall.repository.Post.Post;
import com.github.shoppingmall.shopping_mall.repository.Post.PostRepository;
import com.github.shoppingmall.shopping_mall.service.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Transactional("tmJpa1")
    public Post savePost(/*AuthInfo authInfo, */ Post post){
        logger.info("save Post");
        return postRepository.save(post);
    }

    @Transactional("tmJpa1")
    public List<Post> allPost() {
        logger.info("all post");
        //List<Post> postList = postRepository.findAll();
        //return postList.stream().map(Post::new).collect(Collectors.toList());
        return postRepository.findAll();
    }
    @Transactional("tmJpa1")
    public Post updatePost(Integer postId, Post postRequest){
        logger.info("update post");
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        existingPost.setPost(postRequest);
        return postRepository.save(existingPost);
    }

    @Transactional("tmJpa1")
    public boolean deletePost(Integer postId){
        logger.info("delete post");
        postRepository.deleteById(postId);
        return true;
    }
}
