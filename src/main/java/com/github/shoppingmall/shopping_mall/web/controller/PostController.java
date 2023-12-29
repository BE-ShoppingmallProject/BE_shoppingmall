package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.Post.Post;
import com.github.shoppingmall.shopping_mall.service.PostService;
import com.github.shoppingmall.shopping_mall.web.dto.post.PostCreationDto;
import com.github.shoppingmall.shopping_mall.web.dto.post.PostUpdataionDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

/*
    @GetMapping("/posts")
    @Operation(summary = "모든 판매 상품 페이지 조회")
    public ResponseEntity<Map<String, List<Post>>> allPost(){
        logger.info("/api/posts");
        Map<String, List<Post>> response = new HashMap<>();
        List<Post> posts = postService.allPost();
        response.put("posts", posts);
        return ResponseEntity.ok(response);
    }
*/

    @GetMapping("/post/seller/userid")
    @Operation(summary = "모든 판매 상품 페이지 조회(판매자 ID)")
    public ResponseEntity<Page<Post>> allPostByUserId(
            @RequestParam Integer userId,
            Pageable pageable
    ) {
        Page<Post> posts = postService.getPostsByUserId(userId, pageable);
        return ResponseEntity.ok(posts);
    }

    @PostMapping(value = "/post/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "판매 상품 페이지 등록")
    public ResponseEntity<Map<String, String>> addPost(@ModelAttribute @Valid PostCreationDto postCreationDto) throws Exception{
        logger.info("/api/post/add");
        Map<String, String> response = new HashMap<>();
        Boolean isSave = postService.addPost(postCreationDto);
        if( isSave == true ) {
            response.put("message", "게시물이 성공적으로 작성되었습니다.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            response.put("message", "게시물 작성이 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping(value="/post/update/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "판매 상품 페이지 수정")
    public ResponseEntity<Map<String, String>> updatePost(
            @ModelAttribute @Valid PostUpdataionDto postUpdataionDto) throws IOException {

        logger.info("/api/post/update");
        Map<String, String> response = new HashMap<>();
        Boolean isUpdate = postService.updatePost(postUpdataionDto);
        if( isUpdate == true ) {
            response.put("message", "게시물이 성공적으로 수정되었습니다.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("message", "게시물 수정이 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

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
