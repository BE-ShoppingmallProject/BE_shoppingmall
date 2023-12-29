package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.Post.Post;
import com.github.shoppingmall.shopping_mall.repository.userDetails.CustomUserDetails;
import com.github.shoppingmall.shopping_mall.repository.user_roles.UserRoles;
import com.github.shoppingmall.shopping_mall.service.PostService;
import com.github.shoppingmall.shopping_mall.service.exceptions.NotFoundException;
import com.github.shoppingmall.shopping_mall.service.exceptions.ResourceNotFoundException;
import com.github.shoppingmall.shopping_mall.web.dto.post.*;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
/*
    @GetMapping("/post/seller/userid")
    @Operation(summary = "모든 판매 상품 페이지 조회(판매자 ID)")
    public ResponseEntity<Page<Post>> allPostByUserId(
            @RequestParam Integer userId,
            c
    ) {
        Page<Post> posts = postService.getPostsByUserId(userId, pageable);
        return ResponseEntity.ok(posts);
    }
*/
    @PostMapping(value = "/post/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "판매 상품 페이지 등록")
    public ResponseEntity<?> addPost(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                     @ModelAttribute @Valid PostCreationDto postCreationDto) {

        logger.info("/api/post/add");

        try {
            Boolean isSave = postService.addPost(customUserDetails, postCreationDto);
            if( isSave == true ) {
                return ResponseEntity.status(HttpStatus.CREATED).body("게시물이 성공적으로 작성되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 작성이 실패했습니다.");
            }
        } catch ( IOException e ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (ResourceNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(value="/post/update/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "판매 상품 페이지 수정")
    public ResponseEntity<?> updatePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @ModelAttribute @Valid PostUpdataionDto postUpdataionDto) {

        logger.info("/api/post/update");

        try {
            Boolean isUpdate = postService.updatePost(customUserDetails, postUpdataionDto);
            if( isUpdate == true ) {
                return ResponseEntity.status(HttpStatus.CREATED).body("게시물이 성공적으로 수정되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 수정이 실패했습니다.");
            }
        } catch ( IOException e ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (NotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (ResourceNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @GetMapping(value="/post/seller/")
    @Operation(summary = "모든 판매 상품 페이지 조회(판매자 ID)")
    public ResponseEntity<?> getPostBySellerUser( @RequestParam Integer userId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        logger.info("/api/post/seller");

        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<PostResponseBySeller> postResponseBySellers = postService.getPostBySellerUser(userId, pageable);
            return ResponseEntity.ok(postResponseBySellers);
        } catch ( IllegalArgumentException e ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch ( IllegalAccessException e ){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value="/post/normal/")
    @Operation(summary = "모든 판매 상품 페이지 조회(일반 유저)")
    public ResponseEntity<?> getPostByNormalUser( @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        logger.info("/api/post/normal");

        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<PostResponseByNormal> postResponse = postService.getPostByNormalUser(pageable);
            return ResponseEntity.ok(postResponse);
        } catch ( IllegalArgumentException e ){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value="/posts/{postId}")
    @Operation(summary = "단일 판매 상품 페이지 조회")
    public ResponseEntity<?> getPost( @PathVariable Integer postId) {
        logger.info("/api/posts/" + postId );

        try {
            Optional<PostResponse> postResponse = postService.getPostById(postId);
            if (postResponse.isPresent()) {
                return ResponseEntity.ok(postResponse.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post Not Found");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    @DeleteMapping(value = "/post/{postId}")
    @Operation(summary = "판매 상품 페이지 삭제")
    public ResponseEntity<?> deletePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Integer postId ) {
        logger.info("/api/post/" + postId);

        try {
            postService.deletePost(customUserDetails, postId);
            return ResponseEntity.ok().build();
        } catch ( NotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

}
