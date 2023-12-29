package com.github.shoppingmall.shopping_mall.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
@Schema(title="판매 상품 페이지 등록 VO")
public class PostCreationDto {
    private String title;
    private String content;
    @Schema(title = "Item Info", description = "입력할 판매 물품 정보")
    private String creationDtoJson;
    @Schema(title = "ThumbNail Img File", description = "썸네일 이미지 파일")
    private MultipartFile thumbNailImgFile;
    @Schema(title = "File Array", description = "파일 목록")
    private ArrayList<MultipartFile> files;
}
