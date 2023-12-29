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
import com.github.shoppingmall.shopping_mall.repository.SellerItem.StockItem;
import com.github.shoppingmall.shopping_mall.repository.SellerItem.StockItemRepository;
import com.github.shoppingmall.shopping_mall.repository.users.User;
import com.github.shoppingmall.shopping_mall.repository.users.UserRepository;
import com.github.shoppingmall.shopping_mall.service.exceptions.NotFoundException;
import com.github.shoppingmall.shopping_mall.service.exceptions.ResourceNotFoundException;
import com.github.shoppingmall.shopping_mall.web.dto.post.PostCreationDto;
import com.github.shoppingmall.shopping_mall.web.dto.post.PostUpdataionDto;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.ItemCreationDto;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.ItemDto;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.ItemOptionDto;
import com.github.shoppingmall.shopping_mall.web.dto.seller_item.StockItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

  //  private static final String UPLOAD_DIR = "/Users/peten/test/img/";
    @org.springframework.beans.factory.annotation.Value("${post.file_path}")
    private String UPLOAD_DIR;

    @Transactional("tmJpa1")
    public List<Post> allPost() {
        logger.info("all post");
        return postRepository.findAll();
    }

    @Transactional("tmJpa1")
    public Boolean updatePost(PostUpdataionDto postUpdataionDto) throws IOException  {

        logger.info("update post");
        Integer postId = postUpdataionDto.getPostId();

        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        ItemCreationDto updateItemDto = objectMapper.readValue(postUpdataionDto.getUpdataionDtoJson(), ItemCreationDto.class);

        Item item = covertToEntity( updateItemDto.getItem() );
        logger.info("email : " + updateItemDto.getItem().getEmail() );
        User user = userRepository.findByEmailFetchJoin(updateItemDto.getItem().getEmail()).orElseThrow(() -> new NotFoundException("email에 해당하는 유저가 없습니다."));
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
        if( postUpdataionDto.getThumbNailImgFile().isEmpty() ){
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

        if( postUpdataionDto.getFiles().size() == 0 ){
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
    public boolean deletePost(Integer postId){
        logger.info("delete post");
        postRepository.deleteById(postId);
        return true;
    }

    @Transactional("tmJpa1")
    public Boolean addPost(PostCreationDto postCreationDto) throws IOException  {
        // Item 저장 로직
        // ItemOption, StockItem 등 관련 엔티티 처리
        ItemCreationDto creationDto = objectMapper.readValue(postCreationDto.getCreationDtoJson(), ItemCreationDto.class);

        Item item = covertToEntity( creationDto.getItem() );
        User user = userRepository.findByEmailFetchJoin(creationDto.getItem().getEmail()).orElseThrow(() -> new NotFoundException("email에 해당하는 유저가 없습니다."));
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

    public Page<Post> getPostsByUserId(Integer userId, Pageable pageable) {
        return postRepository.findByUserId(userId, pageable);
    }
}
