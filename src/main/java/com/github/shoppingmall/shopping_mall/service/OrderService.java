package com.github.shoppingmall.shopping_mall.service;

import com.github.shoppingmall.shopping_mall.repository.Cart.CartItemRepository;
import com.github.shoppingmall.shopping_mall.repository.Order.*;
import com.github.shoppingmall.shopping_mall.repository.user_pay.UserPay;
import com.github.shoppingmall.shopping_mall.repository.user_pay.UserPayHistory;
import com.github.shoppingmall.shopping_mall.repository.user_pay.UserPayHistoryRepository;
import com.github.shoppingmall.shopping_mall.repository.user_pay.UserPayRepository;
import com.github.shoppingmall.shopping_mall.repository.users.User;
import com.github.shoppingmall.shopping_mall.repository.users.UserRepository;
import com.github.shoppingmall.shopping_mall.service.exceptions.NotFoundException;
import com.github.shoppingmall.shopping_mall.web.dto.order.OrderDetailDto;
import com.github.shoppingmall.shopping_mall.web.dto.order.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserPayRepository userPayRepository;
    private final UserPayHistoryRepository userPayHistoryRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional("tmJpa1")
    public void createOrder(OrderDto orderDto, List<OrderDetailDto> orderDetails, Integer userId) {
        // 사용자 가져오기
        User user = userRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("사용자 정보를 찾을 수 없습니다."));
        // 주문 생성
        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setZipCode(orderDto.getZipCode());
        order.setAddress(orderDto.getAddress());
        order.setAddressDetail(orderDto.getAddressDetail());
        order.setReqUponDelivery(orderDto.getReqUponDelivery());
        order.setReceiverName(orderDto.getReceiverName());
        order.setReceiverPhone(orderDto.getReceiverPhone());
        orderRepository.save(order);
        // 주문 상세 생성
        for (OrderDetailDto detailDto : orderDetails) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getOrderId());
            orderDetail.setItemId(detailDto.getItemId());
            orderDetail.setOptionId(detailDto.getOptionId());
            orderDetail.setItemCnt(detailDto.getItemCnt());
            orderDetail.setItemPrice(detailDto.getItemPrice());
            orderDetail.setOrderStatus(OrderStatus.APPROVED);
            orderDetail.setCreateDate(new Timestamp(System.currentTimeMillis()));


            // 사용자 예산 감소
            UserPay userPay = userPayRepository.findByUserUserId(user.getUserId());
            int price = detailDto.getItemPrice() * detailDto.getItemCnt();
            userPay.setAccount(userPay.getAccount() - price);
            userPayRepository.save(userPay);

            // 결제 내역 저장
            UserPayHistory payHistory = new UserPayHistory();
            payHistory.setUser(user);
            payHistory.setUsageDate(new Timestamp(System.currentTimeMillis()));
            payHistory.setAccountCharge(price);
            userPayHistoryRepository.save(payHistory);
        }

        // 장바구니 비우기
        cartItemRepository.deleteByCartUserUserId(user.getUserId());

    }

}
