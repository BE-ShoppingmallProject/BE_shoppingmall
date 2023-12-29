package com.github.shoppingmall.shopping_mall.web.dto.item;

import com.github.shoppingmall.shopping_mall.repository.Post.PostFile;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class PostFileDto {
    private Integer postId;
    private String originFileName;
    private String storedFileName;
    private String filePath;
    private Character delegateThumbNail;

    private static ModelMapper modelMapper = new ModelMapper();

    public static PostFileDto of(PostFile postFile){
        return modelMapper.map(postFile, PostFileDto.class);
    }
}
