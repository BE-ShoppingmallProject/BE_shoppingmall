package com.github.shoppingmall.shopping_mall.web.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Data
@Schema(title="판매 상품 페이지 수정 DTO")
public class PostUpdataionDto {
    @Schema(defaultValue = "Post Id")
    private Integer postId;
    @Schema(description = "제목")
    private String title;
    @Schema(description = "내용")
    private String content;
    @Schema(description = "수정할 판매 물품 정보(JSON)", defaultValue = "{     \"item\": {         \"itemId\": 9,         \"email\": \"peten@kakao.com\",         \"categoryId\": 1,         \"itemName\": \"테스트 아이템 명\",         \"description\": \"수정 테스트 중 입니다.\",         \"unitPrice\": 40000     },     \"itemOptions\": [         {             \"optionId\": 16,             \"content\": \"대\",             \"addPrice\": 1999         },         {             \"optionId\": 17,             \"content\": \"중\",             \"addPrice\": 999         },         {             \"optionId\": 18,             \"content\": \"소\",             \"addPrice\": 242         }     ],     \"stockItems\": [         {             \"stockId\": 20,             \"quantity\": 1111,             \"itemStatus\": \"Sale\",             \"startDate\": \"2023-12-29 01:24:23\",             \"endDate\": \"2023-12-31 23:00:00\"         },         {             \"stockId\": 21,             \"quantity\": 0,             \"itemStatus\": \"SoldOut\",             \"startDate\": \"2023-12-29 01:24:23\",             \"endDate\": \"2023-12-31 23:00:00\"         },         {             \"stockId\": 22,             \"quantity\": 3333,             \"itemStatus\": \"Sale\",             \"startDate\": \"2023-12-29 01:24:23\",             \"endDate\": \"2023-12-31 23:00:00\"         }     ] }\n")
    private String updataionDtoJson;
    @Schema(description = "썸네일 이미지 파일")
    private MultipartFile thumbNailImgFile;
    @Schema(description = "파일 목록")
    private ArrayList<MultipartFile> files;
}
