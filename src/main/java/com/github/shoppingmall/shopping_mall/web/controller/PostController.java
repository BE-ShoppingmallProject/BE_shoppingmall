package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.Post.Post;
import com.github.shoppingmall.shopping_mall.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

    //@ApiOperation("새 포스트 입력")
    @PostMapping("/post/add")
    public ResponseEntity<Map<String,String>> savePost(/* AuthInfo authInfo ,*/ @RequestBody Post post){
        logger.info("/api/post/add");
        Map<String, String> response = new HashMap<>();

        Post newPost = postService.savePost(post);
        if( newPost == null ){
            response.put("message", "게시물 작성이 실패했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        else{
            response.put("message", "게시물이 성공적으로 작성되었습니다.");
            return ResponseEntity.ok(response);
        }
    }

    //@ApiOperation("모든 포스트 조회")
    @GetMapping("/posts")
    public ResponseEntity<Map<String, List<Post>>> allPost(){
        logger.info("/api/posts");
        Map<String, List<Post>> response = new HashMap<>();
        List<Post> posts = postService.allPost();
        response.put("posts", posts);
        return ResponseEntity.ok(response);
    }

    //@ApiOperation("포스트 수정")
    @PutMapping("/post/update/{post_id}")
    public ResponseEntity<Map<String, String>> updatePost(
            @PathVariable("post_id") Integer postId,
            @RequestBody Post postRequest) {
        logger.info("/api/post/update");

        Post updatedPost = postService.updatePost(postId, postRequest);

        Map<String, String> response = new HashMap<>();
        response.put("message", "게시물이 수정되었습니다.");
        response.put("postId", updatedPost.getPostId().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //@ApiOperation("포스트 삭제")
    @DeleteMapping("/delete/{post_id}")
    public ResponseEntity<Map<String, String>> deletePost(@PathVariable("post_id") Integer postId){
        logger.info("/api/post/delete");
        Map<String, String> response = new HashMap<>();

        boolean isDelete = postService.deletePost(postId);
        if( isDelete == true ){
            response.put("message", "게시물이 성공적으로 삭제되었습니다.");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "게시물 삭제가 실패되었습니다.");
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(response);
        }
    }


}
