package com.github.shoppingmall.shopping_mall.web.controller;

import com.github.shoppingmall.shopping_mall.repository.Post.Post;
import com.github.shoppingmall.shopping_mall.repository.userDetails.CustomUserDetails;
import com.github.shoppingmall.shopping_mall.repository.user_roles.UserRoles;
import com.github.shoppingmall.shopping_mall.service.PostService;
import com.github.shoppingmall.shopping_mall.service.exceptions.NotFoundException;
import com.github.shoppingmall.shopping_mall.service.exceptions.ResourceNotFoundException;
import com.github.shoppingmall.shopping_mall.web.dto.post.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name="posts", description = "판매 상품 페이지 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;

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
        /*
        {     "item": {         "email": "peten@kakao.com",         "categoryId": 1,         "itemName": "Example Item",         "description": "This is an example item.",         "unitPrice": 25000     },     "itemOptions": [         {             "content": "대",             "addPrice": 2000         },         {             "content": "중",             "addPrice": 1000         },         {             "content": "소",             "addPrice": 0         }     ],     "stockItems": [         {             "quantity": 1000,             "itemStatus": "Sale",             "startDate": "2023-12-29 01:24:23",             "endDate": "2023-12-31 23:00:00"         },         {             "quantity": 2000,             "itemStatus": "Sale",             "startDate": "2023-12-29 01:24:23",             "endDate": "2023-12-31 23:00:00"         },         {             "quantity": 2000,             "itemStatus": "Sale",             "startDate": "2023-12-29 01:24:23",             "endDate": "2023-12-31 23:00:00"         }     ] }
         */
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

        /*

        {     "item": {         "itemId": 9,         "email": "peten@kakao.com",         "categoryId": 1,         "itemName": "테스트 아이템 명",         "description": "수정 테스트 중 입니다.",         "unitPrice": 40000     },     "itemOptions": [         {             "optionId": 16,             "content": "대",             "addPrice": 1999         },         {             "optionId": 17,             "content": "중",             "addPrice": 999         },         {             "optionId": 18,             "content": "소",             "addPrice": 242         }     ],     "stockItems": [         {             "stockId": 20,             "quantity": 1111,             "itemStatus": "Sale",             "startDate": "2023-12-29 01:24:23",             "endDate": "2023-12-31 23:00:00"         },         {             "stockId": 21,             "quantity": 0,             "itemStatus": "SoldOut",             "startDate": "2023-12-29 01:24:23",             "endDate": "2023-12-31 23:00:00"         },         {             "stockId": 22,             "quantity": 3333,             "itemStatus": "Sale",             "startDate": "2023-12-29 01:24:23",             "endDate": "2023-12-31 23:00:00"         }     ] }

         */
    }


    @GetMapping(value="/post/seller/")
    @Operation(summary = "모든 판매 상품 페이지 조회(판매자 ID로 조회)")
    public ResponseEntity<?> getPostBySellerUser( @Parameter(name="userId", description = "판매자 User ID") @RequestParam Integer userId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        logger.info("/api/post/seller");

        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<PostResponseByNormal> postResponseBySellers = postService.getPostBySellerUser(userId, pageable);
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


    @GetMapping(value="/post/seller-page/")
    @Operation(summary = "해당 판매자가 판매 중인 모든 판매 상품 페이지 조회(판매자 페이지)")
    public ResponseEntity<?> getPost4SellerMenu( @AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                 @RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "30") int size){
        logger.info("/api/post/seller-page");

        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<PostResponse> postResponse = postService.getPost4SellerUser(customUserDetails, pageable);
            return ResponseEntity.ok(postResponse);
        } catch ( NotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch ( IllegalAccessException e ){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch ( Exception e ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping(value="/post/{postId}")
    @Operation(summary = "단일 판매 상품 페이지 조회")
    public ResponseEntity<?> getPost( @PathVariable Integer postId) {
        logger.info("/api/posts/" + postId );

        try {
            Optional<PostResponse> postResponse = postService.getPostById(postId);
            if (postResponse.isPresent()) {
                postService.updatePostViewCnt(postId);
                return ResponseEntity.ok(postResponse.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post Not Found");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @GetMapping("/post/search")
    @Operation(summary = "판매 상품 페이지 키워드 검색")
    public ResponseEntity<?> searchPost( @Parameter(name="type", description = "'name' 혹은 'content'" ) @RequestParam(value="type", required = false) String type,
                                         @RequestParam(value="keyword", required = false) String keyword,
                                         @RequestParam(value="page", defaultValue = "0") int page,
                                         @RequestParam(value="size", defaultValue = "30") int size ) {
        logger.info("/api/post/search");

        // type : name / content

        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<PostResponseByNormal> postResponse = postService.searchPosts(type, keyword, pageable);
            return ResponseEntity.ok(postResponse);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @DeleteMapping(value = "/post/{postId}")
    @Operation(summary = "판매 상품 페이지 삭제")
    public ResponseEntity<?> deletePost(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @PathVariable Integer postId ) {
        logger.info("/api/post/" + postId);

        try {
            Boolean isDelete = postService.deletePost(customUserDetails, postId);
            if( isDelete == true ) {
                return ResponseEntity.status(HttpStatus.OK).body("게시물이 성공적으로 삭제되었습니다.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물 삭제가 실패했습니다.");
            }
        } catch ( NotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }




    @GetMapping("/post/category/")
    @Operation(summary = "판매 상품 페이지 조회(카테고리별)")
    public ResponseEntity<?> searchPost( @Parameter(name="categoryId", description = "Category ID") @RequestParam Integer categoryId,
                                         @RequestParam(value="page", defaultValue = "0") int page,
                                         @RequestParam(value="size", defaultValue = "30") int size ) {
        logger.info("/api/post/category");

        Pageable pageable = PageRequest.of(page, size);
        try {
            //logger.info("categoryId : " + categoryId);
            Page<PostResponseByNormal> postResponse = postService.getPostByCategoryId(categoryId, pageable);
            return ResponseEntity.ok(postResponse);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }}
