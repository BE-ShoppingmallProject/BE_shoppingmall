package com.github.shoppingmall.shopping_mall.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
@Schema(title="판매 상품 페이지 등록 DTO")
public class PostCreationDto {
    @Schema(description = "제목")
    private String title;
    @Schema(description = "내용")
    private String content;
    @Schema(description = "입력할 판매 물품 정보(JSON)", defaultValue = "" +
            "{     \"item\": {         \"email\": \"peten@kakao.com\",         \"categoryId\": 1,         \"itemName\": \"Example Item\",         \"description\": \"This is an example item.\",         \"unitPrice\": 25000     },     \"itemOptions\": [         {             \"content\": \"대\",             \"addPrice\": 2000         },         {             \"content\": \"중\",             \"addPrice\": 1000         },         {             \"content\": \"소\",             \"addPrice\": 0         }     ],     \"stockItems\": [         {             \"quantity\": 1000,             \"itemStatus\": \"Sale\",             \"startDate\": \"2023-12-29 01:24:23\",             \"endDate\": \"2023-12-31 23:00:00\"         },         {             \"quantity\": 2000,             \"itemStatus\": \"Sale\",             \"startDate\": \"2023-12-29 01:24:23\",             \"endDate\": \"2023-12-31 23:00:00\"         },         {             \"quantity\": 2000,             \"itemStatus\": \"Sale\",             \"startDate\": \"2023-12-29 01:24:23\",             \"endDate\": \"2023-12-31 23:00:00\"         }     ] }\n")
    private String creationDtoJson;
    @Schema(description = "썸네일 이미지 파일")
    private MultipartFile thumbNailImgFile;
    @Schema(description = "등록할 이미지 파일 목록")
    private ArrayList<MultipartFile> files;
}
