package com.github.shoppingmall.shopping_mall.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemOption;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemOptionRepository;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemRepository;
import com.github.shoppingmall.shopping_mall.repository.Post.Post;
import com.github.shoppingmall.shopping_mall.repository.Post.PostFile;
import com.github.shoppingmall.shopping_mall.repository.Post.PostFileRepository;
import com.github.shoppingmall.shopping_mall.repository.Post.PostRepository;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.StockAdjustment;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.StockAdjustmentRepository;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.StockItem;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.StockItemRepository;
import com.github.shoppingmall.shopping_mall.repository.userDetails.CustomUserDetails;
import com.github.shoppingmall.shopping_mall.repository.user_roles.UserRoles;
import com.github.shoppingmall.shopping_mall.repository.user_roles.UserRolesRepository;
import com.github.shoppingmall.shopping_mall.repository.users.User;
import com.github.shoppingmall.shopping_mall.repository.users.UserRepository;
import com.github.shoppingmall.shopping_mall.service.exceptions.NotFoundException;
import com.github.shoppingmall.shopping_mall.service.exceptions.ResourceNotFoundException;
import com.github.shoppingmall.shopping_mall.web.dto.post.*;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.ItemCreationDto;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.ItemDto;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.ItemOptionDto;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.StockItemDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Not;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final StockItemRepository stockItemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final PostFileRepository postFileRepository;
    private final ObjectMapper objectMapper;
    private final UserRolesRepository userRolesRepository;
    private final StockAdjustmentRepository stockAdjustmentRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

  //  private static final String UPLOAD_DIR = "/Users/peten/test/img/";
    @org.springframework.beans.factory.annotation.Value("${post.file_path}")
    private String UPLOAD_DIR;


    @Transactional("tmJpa1")
    public Boolean updatePost(CustomUserDetails customUserDetails, PostUpdataionDto postUpdataionDto) throws IOException  {

        logger.info("update post");
        User user = userRepository.findByEmailFetchJoin(customUserDetails.getUsername()).orElseThrow(() -> new NotFoundException("email에 해당하는 유저가 없습니다."));

        Integer postId = postUpdataionDto.getPostId();

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        ItemCreationDto updateItemDto = objectMapper.readValue(postUpdataionDto.getUpdataionDtoJson(), ItemCreationDto.class);

        //User user = userRepository.findByEmail(updateItemDto.getItem().getEmail());

        Item item = covertToEntity( updateItemDto.getItem() );
        logger.info("email : " + updateItemDto.getItem().getEmail() );
        item.setUserId(user.getUserId());
        item.setItemId(updateItemDto.getItem().getItemId());
        Item saveItem = itemRepository.save(item);
        if( saveItem == null ){
            logger.error("item update fail");
            return false;
        }

        List<ItemOption> optionList = new ArrayList<>();

        for(ItemOptionDto optionDto : updateItemDto.getItemOptions()){
            ItemOption option = convertToEntity(optionDto);
            option.setItem(item);
            option.setOptionId(optionDto.getOptionId());
            ItemOption saveOption = itemOptionRepository.save(option);
            if( saveOption == null ){
                logger.error("item option[" + optionDto.getContent() +"] update fail");
                return false;
            }
            optionList.add(saveOption);
        }

        if( optionList.size() > 0 ){
            for( int i = 0; i < updateItemDto.getStockItems().size(); i++ ){

                StockItemDto stockItemDto = updateItemDto.getStockItems().get(i);
                ItemOption itemOption = optionList.get(i);

                StockItem stockItem = convertToEntity( stockItemDto );
                stockItem.setItem(item);
                stockItem.setOption(itemOption);
                stockItem.setUser(user);
                stockItem.setStockId(stockItemDto.getStockId());

                StockItem saveStockItem = stockItemRepository.save(stockItem);
                if( saveStockItem == null ){
                    logger.error("stock_item update fail");
                    return false;
                }
            }
        } else {
            StockItemDto stockItemDto = updateItemDto.getStockItems().get(0);
            StockItem stockItem = convertToEntity(stockItemDto);
            stockItem.setItem(item);
            stockItem.setUser(user);
            stockItem.setStockId(stockItemDto.getStockId());

            StockItem saveStockItem = stockItemRepository.save(stockItem);
            if (saveStockItem == null) {
                logger.error("stock_item update fail");
                return false;
            }
        }


        // Post 객체 수정
        existingPost.setTitle(postUpdataionDto.getTitle());
        existingPost.setContent(postUpdataionDto.getContent());
        existingPost.setItem(item);
        existingPost.setUserId(user.getUserId());
        Post savePost = postRepository.save(existingPost);
        if( savePost == null ){
            logger.error("post update fail");
            return false;
        }

        // 썸네일 이미지 저장( 무조건 있어야 함 )
        if( postUpdataionDto.getThumbNailImgFile() == null || postUpdataionDto.getThumbNailImgFile().isEmpty() == true ){
            logger.info("thumnail Img file Empty");
            // 기존 정보 그대로 유지.
        } else {
            List<PostFile> thumbNailFileList = postFileRepository.findByPost_PostIdAndDelegateThumbNailAndIsDeleted(postId, 'Y', 'N');
            if( thumbNailFileList.size() == 0 ){
                new ResourceNotFoundException("PostFile not found with id: " + postId);
                return false;
            }

            thumbNailFileList.get(0).setIsDeleted('Y');
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            thumbNailFileList.get(0).setDeleteDate(timestamp);
            postFileRepository.save(thumbNailFileList.get(0));


            MultipartFile file = postUpdataionDto.getThumbNailImgFile();
            String originalFilename = file.getOriginalFilename();
            String storedFilename = createStoredFileName(originalFilename);
            String filePath = UPLOAD_DIR + storedFilename;

            saveFile(file, filePath);

            // DB에 파일 정보 저장
            PostFile postFile = new PostFile();
            postFile.setPost(existingPost);
            postFile.setUserId(user.getUserId());
            //postFile.setPostId(existingPost.getPostId());
            postFile.setOriginFileName(originalFilename);
            postFile.setStoredFileName(storedFilename);
            postFile.setFilePath(filePath);
            postFile.setFileSize((int) file.getSize());
            postFile.setFileType(file.getContentType());
            postFile.setItemId(existingPost.getItem().getItemId());
            postFile.setDelegateThumbNail('Y');
            postFile.setIsDeleted('N');

            postFileRepository.save(postFile);
        }


        // 파일 수정

        if( postUpdataionDto.getFiles() == null || postUpdataionDto.getFiles().size() == 0 || postUpdataionDto.getFiles().isEmpty() == true ){
            logger.info("Img file Empty");
            // 기존 정보 그대로 유지.
        } else {

            List<PostFile> existingFiles = postFileRepository.findByPost_PostIdAndDelegateThumbNailAndIsDeleted(postId, 'N', 'N');

            for (PostFile existingFile : existingFiles) {
                boolean isFileUpdated = false;
                for (MultipartFile newFile : postUpdataionDto.getFiles()) {
                    if (existingFile.getStoredFileName().equals(newFile.getOriginalFilename())) {
                        isFileUpdated = true;
                        break;
                    }
                }
                if (!isFileUpdated) {
                    existingFile.setIsDeleted('Y');
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    existingFile.setDeleteDate(timestamp);
                    postFileRepository.save(existingFile);


                    // 기존 파일 시스템에서 파일 삭제
                    File fileToDelete = new File(existingFile.getFilePath());
                    if (fileToDelete.exists()) {
                        fileToDelete.delete();
                    }
                }
            }

            for (MultipartFile newFile : postUpdataionDto.getFiles()) {

                MultipartFile file = postUpdataionDto.getThumbNailImgFile();
                String originalFilename = file.getOriginalFilename();
                String storedFilename = createStoredFileName(originalFilename);
                String filePath = UPLOAD_DIR + storedFilename;

                saveFile(file, filePath);


                PostFile postFile = new PostFile();
                //postFile.setPostId(existingPost.getPostId());
                postFile.setUserId(user.getUserId());
                postFile.setPost(existingPost);
                postFile.setOriginFileName(originalFilename);
                postFile.setStoredFileName(storedFilename);
                postFile.setFilePath(filePath);
                postFile.setFileSize((int) file.getSize());
                postFile.setFileType(file.getContentType());
                postFile.setItemId(existingPost.getItem().getItemId());
                postFile.setDelegateThumbNail('N');
                postFile.setIsDeleted('N');

                postFileRepository.save(postFile);
            }
        }

        return true;
    }

    @Transactional("tmJpa1")
    public boolean deletePost(CustomUserDetails customUserDetails, Integer postId) throws NotFoundException, EntityNotFoundException{
        logger.info("delete post");
        User user = userRepository.findByEmailFetchJoin(customUserDetails.getUsername()).orElseThrow(() -> new NotFoundException("email에 해당하는 유저가 없습니다."));;
        Post post = postRepository.findById(postId).orElseThrow(()->new EntityNotFoundException("삭제할 판매상품 페이지가 없습니다."));

        // Update is_deleted in POST and POST_FILE
        post.setIsDeleted('Y');
        post.setDeleteDate(new Timestamp(System.currentTimeMillis()));
        postRepository.save(post);

        List<PostFile> postFiles = postFileRepository.findByPost(post);
        for( PostFile postFile : postFiles){
            postFile.setIsDeleted('Y');
            postFile.setDeleteDate(new Timestamp(System.currentTimeMillis()));
            postFileRepository.save(postFile);
        }

        // Update STOCK_ADJUSTMENT Table
        if( post.getItem().getItemOptions().size() == 0 ){
            StockAdjustment stockAdjustment = new StockAdjustment();
            stockAdjustment.setUser(user);
            stockAdjustment.setItem(post.getItem());
            stockAdjustment.setQuantity(0);
            stockAdjustment.setReason("DELETE POST");
            stockAdjustmentRepository.save(stockAdjustment);
        } else {
            for (ItemOption itemOption : post.getItem().getItemOptions()) {
                StockAdjustment stockAdjustment = new StockAdjustment();
                stockAdjustment.setUser(user);
                stockAdjustment.setItem(post.getItem());
                stockAdjustment.setOption(itemOption);
                stockAdjustment.setQuantity(0);
                stockAdjustment.setReason("DELETE POST");
                stockAdjustmentRepository.save(stockAdjustment);
            }
        }

        return true;
    }

    @Transactional("tmJpa1")
    public Boolean addPost(CustomUserDetails customUserDetails, PostCreationDto postCreationDto) throws IOException, NotFoundException {

        User user = userRepository.findByEmailFetchJoin(customUserDetails.getUsername()).orElseThrow(() -> new NotFoundException("email에 해당하는 유저가 없습니다."));;

        // Item 저장 로직
        // ItemOption, StockItem 등 관련 엔티티 처리
        ItemCreationDto creationDto = objectMapper.readValue(postCreationDto.getCreationDtoJson(), ItemCreationDto.class);
   //     User user  = userRepository.findByEmail(creationDto.getItem().getEmail());

        Item item = covertToEntity( creationDto.getItem() );
        item.setUserId(user.getUserId());
        Item saveItem = itemRepository.save(item);
        if( saveItem == null ){
            logger.error("item save fail");
            return false;
        }

        List<ItemOption> optionList = new ArrayList<>();

        for(ItemOptionDto optionDto : creationDto.getItemOptions()){
            ItemOption option = convertToEntity(optionDto);
            option.setItem(item);
            ItemOption saveOption = itemOptionRepository.save(option);
            if( saveOption == null ){
                logger.error("item option[" + optionDto.getContent() +"] save fail");
                return false;
            }
            optionList.add(saveOption);
        }

        if( optionList.size() == 0 ){
            StockItemDto stockItemDto = creationDto.getStockItems().get(0);
            StockItem stockItem = convertToEntity(stockItemDto);
            stockItem.setItem(item);
            stockItem.setUser(user);

            StockItem saveStockItem = stockItemRepository.save(stockItem);
            if (saveStockItem == null) {
                logger.error("stock_item save fail");
                return false;
            }

        } else {
            for (int i = 0; i < creationDto.getStockItems().size(); i++) {
                StockItemDto stockItemDto = creationDto.getStockItems().get(i);
                ItemOption itemOption = optionList.get(i);

                StockItem stockItem = convertToEntity(stockItemDto);
                stockItem.setItem(item);
                stockItem.setOption(itemOption);
                stockItem.setUser(user);

                StockItem saveStockItem = stockItemRepository.save(stockItem);
                if (saveStockItem == null) {
                    logger.error("stock_item save fail");
                    return false;
                }
            }
        }

        // Post 객체 생성 및 저장
        Post post = new Post();
        post.setTitle(postCreationDto.getTitle());
        post.setContent(postCreationDto.getContent());
        post.setItem(item);
        post.setViewCnt(0);
        post.setUserId(user.getUserId());
        post.setIsDeleted('N');
        Post savePost = postRepository.save(post);
        if( savePost == null ){
            logger.error("post save fail");
            return false;
        }

        // 썸네일 이미지 저장( 무조건 있어야 함 )
        if( postCreationDto.getThumbNailImgFile().isEmpty() ){
            logger.error("thumnail Img file Empty");
            return false;
        } else {
            MultipartFile file = postCreationDto.getThumbNailImgFile();
            String originalFilename = file.getOriginalFilename();
            String storedFilename = createStoredFileName(originalFilename);
            String filePath = UPLOAD_DIR + storedFilename;

            saveFile(file, filePath);

            // DB에 파일 정보 저장
            PostFile postFile = new PostFile();
            //postFile.setPost(post);
            postFile.setUserId(user.getUserId());
            //postFile.setPostId(post.getPostId());
            postFile.setPost(post);
            postFile.setOriginFileName(originalFilename);
            postFile.setStoredFileName(storedFilename);
            postFile.setFilePath(filePath);
            postFile.setFileSize((int) file.getSize());
            postFile.setFileType(file.getContentType());
            postFile.setItemId(post.getItem().getItemId());
            postFile.setDelegateThumbNail('Y');
            postFile.setIsDeleted('N');

            postFileRepository.save(postFile);
        }



        // 파일 처리 및 저장
        if( postCreationDto.getFiles() == null ||
                postCreationDto.getFiles().isEmpty() == true ||
                postCreationDto.getFiles().size() == 0 ){
            // 없을 땐 등록 하지 않음.
        }else {
            for (MultipartFile file : postCreationDto.getFiles()) {
                if (!file.isEmpty()) {
                    String originalFilename = file.getOriginalFilename();
                    String storedFilename = createStoredFileName(originalFilename);
                    String filePath = UPLOAD_DIR + storedFilename;

                    // 파일 시스템에 파일 저장
                    saveFile(file, filePath);


                    // DB에 파일 정보 저장
                    PostFile postFile = new PostFile();
                    //postFile.setPost(post);
                    postFile.setUserId(user.getUserId());
                    //postFile.setPostId(post.getPostId());
                    postFile.setPost(post);
                    postFile.setOriginFileName(originalFilename);
                    postFile.setStoredFileName(storedFilename);
                    postFile.setFilePath(filePath);
                    postFile.setFileSize((int) file.getSize());
                    postFile.setFileType(file.getContentType());
                    postFile.setItemId(post.getItem().getItemId());
                    postFile.setDelegateThumbNail('N');
                    postFile.setIsDeleted('N');

                    postFileRepository.save(postFile);
                }
            }
        }

        return true;
    }

    private String createStoredFileName(String originalFilename){
        String fileExtension = Objects.requireNonNull(originalFilename)
                    .substring(originalFilename.lastIndexOf("."));
        return UUID.randomUUID().toString() + fileExtension;
    }

    private void saveFile(MultipartFile file, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }

    private Item covertToEntity(ItemDto item) {
        Item newItem = new Item();
        newItem.setItemName(item.getItemName());
        newItem.setItemExplain(item.getDescription());
        newItem.setCategoryId(item.getCategoryId());
        newItem.setUnitPrice(item.getUnitPrice());
        return newItem;
    }

    private StockItem convertToEntity(StockItemDto stockItem) {
        StockItem newStockItem = new StockItem();
        newStockItem.setItemStatus(stockItem.getItemStatus());
        newStockItem.setQuantity(stockItem.getQuantity());
        Timestamp start_ts = Timestamp.valueOf(stockItem.getStartDate());
        Timestamp end_ts = Timestamp.valueOf(stockItem.getEndDate());
        newStockItem.setStartDate(start_ts);
        newStockItem.setEndDate(end_ts);
        return newStockItem;
    }

    private ItemOption convertToEntity(ItemOptionDto itemOption){
        ItemOption newItemOption = new ItemOption();
        newItemOption.setOptionContent(itemOption.getContent());
        newItemOption.setAdditionalPrice(itemOption.getAddPrice());
        return newItemOption;
    }

    public Page<PostResponseByNormal> getPostBySellerUser(Integer userId, Pageable pageable) throws IllegalAccessException {
        // 1-1. Check If the user is a seller
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("Invalid user ID"));
        UserRoles userRoles = userRolesRepository.findByUser_UserId(userId);
        if(!"ROLE_SELLER".equals(userRoles.getRoles().getName())) {
            throw new IllegalAccessException("No Permission");
        }

        // 1-2. Fetch posts and related data
        Page<Post> posts = postRepository.findByUserIdAndIsDeleted(userId, 'N', pageable);
        return posts.map(this::covertToPostResponseByNormal);
    }


    private PostResponseBySeller covertToPostResponseBySeller(Post post){
        PostResponseBySeller response = new PostResponseBySeller();
        response.setPostId(post.getPostId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setViewCnt(post.getViewCnt());
        response.setCreateDate(post.getCreateDate());
        response.setUpdateDate(post.getUpdateDate());
        response.setDeleteDate(post.getDeleteDate());

        // Item 정보 설정
        Item item = post.getItem();
        if( item != null ){
            response.setItemId(item.getItemId());
            response.setItemName(item.getItemName());
            response.setItemExplain(item.getItemExplain());
            response.setUnitPrice(item.getUnitPrice());
        }

        // StockItem 정보 설정
        int cur_quantity = 0;
        String cur_itemStatus = "Sale";
        boolean isSale = false;
        Set<StockItem> stockItems = item.getStockItems();
        for( StockItem curItem : stockItems){
            cur_quantity += curItem.getQuantity();
            if( cur_itemStatus.equals(curItem.getItemStatus()) ){
                isSale = true;
            }
        }

        if( isSale == true ) {
            response.setItemStatus("Sale");
        } else {
            response.setItemStatus("SoldOut");
        }
        response.setQuantity(cur_quantity);

        // PostFile 설정
        Optional<PostFile> thumbNailFile = post.getPostFiles().stream()
                .filter(pf -> pf.getDelegateThumbNail() == 'Y' && pf.getIsDeleted() != 'Y')
                .findFirst();
        response.setThumbNailImgPath(thumbNailFile.map(pf -> pf.getFilePath()).orElse(null));

        return response;
    }

    public Page<PostResponseByNormal> getPostByNormalUser(Pageable pageable) {
        Page<Post> posts = postRepository.findByIsDeleted('N',pageable);
        return posts.map(this::covertToPostResponseByNormal);
    }

    public PostResponseByNormal covertToPostResponseByNormal(Post post){
        PostResponseByNormal response = new PostResponseByNormal();
        response.setPostId(post.getPostId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setViewCnt(post.getViewCnt());
        response.setCreateDate(post.getCreateDate());
        response.setUpdateDate(post.getUpdateDate());
        response.setSellerUserId(post.getUserId());

        // Item 정보 설정
        Item item = post.getItem();
        if( item != null ){
            response.setItemId(item.getItemId());
            response.setItemName(item.getItemName());
            response.setItemExplain(item.getItemExplain());
            response.setUnitPrice(item.getUnitPrice());
        }

        // PostFile 설정
        Optional<PostFile> thumbNailFile = post.getPostFiles().stream()
                .filter(pf -> pf.getDelegateThumbNail() == 'Y' && pf.getIsDeleted() != 'Y')
                .findFirst();
        response.setThumbNailImgPath(thumbNailFile.map(pf -> pf.getFilePath()).orElse(null));

        return response;
    }

    public Optional<PostResponse> getPostById(Integer postId) {
        Optional<PostResponse> response =  postRepository.findByPostIdAndIsDeleted(postId, 'N')
                .filter(post -> post.getItem().getStockItems().stream()
                        .anyMatch(stockItem -> "Sale".equals(stockItem.getItemStatus())))
                .map(this::convertToPostResponse);

        return response;
    }


    public PostResponse convertToPostResponse(Post post){
        PostResponse response = new PostResponse();
        response.setPostId(post.getPostId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setViewCnt(post.getViewCnt());
        response.setCreateDate(post.getCreateDate());
        response.setUpdateDate(post.getUpdateDate());
        response.setSellerUserId(post.getUserId());

        // Item 정보 설정
        Item item = post.getItem();
        if( item != null ){
            response.setItemId(item.getItemId());
            response.setItemName(item.getItemName());
            response.setItemExplain(item.getItemExplain());
            response.setUnitPrice(item.getUnitPrice());
        }

        // Item Option 설정
        // itemId에 해당하는 ItemOption 조회
        List<ItemOption> itemOptions = itemOptionRepository.findByItem_ItemId(post.getItem().getItemId());

        if (itemOptions != null && !itemOptions.isEmpty()) {
            // itemOptionList 설정
            List<PostResponse_ItemOption> itemOptionList = new ArrayList<>();
            for (ItemOption option : itemOptions) {
                // StockItem의 quantity 확인
                StockItem curStockItem = stockItemRepository.findByItem_ItemIdAndOption_OptionId(post.getItem().getItemId(), option.getOptionId());

                if (curStockItem.getQuantity() > 0) {
                    PostResponse_ItemOption responseOption = new PostResponse_ItemOption();
                    responseOption.setOptionId(option.getOptionId());
                    responseOption.setStockId(curStockItem.getStockId());
                    responseOption.setQuantity(curStockItem.getQuantity());
                    responseOption.setOptionContent(option.getOptionContent());
                    responseOption.setAddPrice(option.getAdditionalPrice());
                    itemOptionList.add(responseOption);
                }
            }

            response.setItemOptionList(itemOptionList);
        }


        // PostFile 설정
        response.setThumbNailImgPath(post.getPostFiles().stream()
                .filter(pf -> pf.getDelegateThumbNail() == 'Y' && pf.getIsDeleted() != 'Y')
                .findFirst()
                .map(pf -> pf.getFilePath())
                .orElse(null));

        response.setOtherImgPathList(post.getPostFiles().stream()
                .filter(pf -> pf.getDelegateThumbNail() == 'N' && pf.getIsDeleted() != 'Y')
                .map(pf -> pf.getFilePath())
                .collect(Collectors.toList()));

        return response;
    }

    @Transactional("tmJpa1")
    public Page<PostResponseByNormal> searchPosts(String type, String keyword, Pageable pageable) {
        if( type == null || type.isEmpty() || keyword == null || keyword.isEmpty() ) {
            Page<Post> posts = postRepository.findByIsDeleted('N',pageable);
            return posts.map(this::covertToPostResponseByNormal);
        }

        Page<Post> posts = postRepository.searchByTypeWithDetails(type, keyword, pageable);
        return posts.map(this::covertToPostResponseByNormal);
    }

    public Page<PostResponse> getPost4SellerUser(CustomUserDetails customUserDetails, Pageable pageable) throws IllegalAccessException, NotFoundException {
        User user = userRepository.findByEmailFetchJoin(customUserDetails.getUsername()).orElseThrow(() -> new NotFoundException("email에 해당하는 유저가 없습니다."));

        // 1-1. Check If the user is a seller
        UserRoles userRoles = userRolesRepository.findByUser_UserId(user.getUserId());
        if(!"ROLE_SELLER".equals(userRoles.getRoles().getName())) {
            throw new IllegalAccessException("No Permission");
        }

        // 1-2. Fetch posts and related data
        Page<Post> posts = postRepository.findByUserIdAndIsDeleted(user.getUserId(), 'N', pageable);
        return posts.map(this::convertToPostResponse);
    }


}
