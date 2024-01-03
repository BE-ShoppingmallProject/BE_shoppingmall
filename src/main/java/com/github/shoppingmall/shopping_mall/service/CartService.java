package com.github.shoppingmall.shopping_mall.service;

import com.github.shoppingmall.shopping_mall.repository.Cart.Cart;
import com.github.shoppingmall.shopping_mall.repository.Cart.CartItem;
import com.github.shoppingmall.shopping_mall.repository.Cart.CartItemRepository;
import com.github.shoppingmall.shopping_mall.repository.Cart.CartRepository;
import com.github.shoppingmall.shopping_mall.repository.Item.Item;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemOption;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemOptionRepository;
import com.github.shoppingmall.shopping_mall.repository.Item.ItemRepository;
import com.github.shoppingmall.shopping_mall.repository.users.User;
import com.github.shoppingmall.shopping_mall.repository.users.UserRepository;
import com.github.shoppingmall.shopping_mall.service.exceptions.NotFoundException;
import com.github.shoppingmall.shopping_mall.web.dto.cart.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemOptionRepository itemOptionRepository;

    @Transactional("tmJpa1")
    public Integer addCart(CartItemDto cartItemDto, Integer userId){
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findByUserId(userId).orElseThrow(()->new NotFoundException("해당하는 유저가 없습니다."));
        ItemOption itemOption = itemOptionRepository.findById(cartItemDto.getOptionId()).orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByUser_UserId(user.getUserId());
        if(cart == null){
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartCartIdAndItemItemId(cart.getCartId(), item.getItemId());

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getCartItemId();
        }else {
            CartItem cartItem = CartItem.createCartItem(cart, item, itemOption, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getCartItemId();
        }
    }


    @Transactional("tmJpa1")
    public List<CartDetailDto> getCartList(Integer userId) { // 장바구니에 들어있는 상품 조회
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        User user = userRepository.findByUserId(userId).orElseThrow(()->new NotFoundException("해당하는 유저가 없습니다.")); // 회원 조회
        Cart cart = cartRepository.findByUser_UserId(user.getUserId());

        if (cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getCartId());
        return cartDetailDtoList;
    }


    @Transactional("tmJpa1")
    public boolean validateCartItem(Integer cartItemId, Integer UserId){
        User user = userRepository.findByUserId(UserId).orElseThrow(()->new NotFoundException("해당하는 유저가 없습니다.")); // 회원 조회
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        User savedUser = cartItem.getCart().getUser();

        if(!StringUtils.equals(user.getEmail(),savedUser.getEmail())){
            return false;
        }
        return true;
    }

    @Transactional("tmJpa1")
    public void updateCartItemCount(Integer cartItemId, Integer count){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
        cartItemRepository.save(cartItem);
    }

    @Transactional("tmJpa1")
    public void deleteCartItem(Integer cartItemId){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

}
