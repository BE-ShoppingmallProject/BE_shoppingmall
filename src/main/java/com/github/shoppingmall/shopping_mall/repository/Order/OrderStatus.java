package com.github.shoppingmall.shopping_mall.repository.Order;

public enum OrderStatus {
    PENDING, // 주문 대기
    APPROVED, // 주문 승인
    SHIPPED, // 발송 완료
    IN_TRANSIT, // 배송중
    DELIVERED, // 배송완료
    CANCELED, // 주문 취소
    REFUND_REQUESTED, // 환불 신청
    REFUNDED; // 환불 완료
}