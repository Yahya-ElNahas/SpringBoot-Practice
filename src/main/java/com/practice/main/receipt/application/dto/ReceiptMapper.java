package com.practice.main.receipt.application.dto;

import com.practice.main.order.application.dto.internal.OrderItemDto;
import com.practice.main.receipt.domain.ReceiptItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReceiptMapper {

    ReceiptItem toReceiptItem(OrderItemDto item);
}
