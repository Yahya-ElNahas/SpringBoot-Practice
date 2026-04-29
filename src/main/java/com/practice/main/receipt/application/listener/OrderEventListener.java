package com.practice.main.receipt.application.listener;

import com.practice.main.order.application.dto.internal.OrderPlacedEvent;
import com.practice.main.order.domain.OrderStatus;
import com.practice.main.receipt.application.ReceiptService;
import com.practice.main.receipt.application.dto.ReceiptMapper;
import com.practice.main.receipt.domain.ReceiptDocument;
import com.practice.main.receipt.domain.ReceiptItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventListener {

    private final ReceiptService receiptService;

    private final ReceiptMapper mapper;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderPlaced(OrderPlacedEvent event) {
        List<ReceiptItem> receiptItems = event.items().stream().map(
                mapper::toReceiptItem
        ).toList();

        ReceiptDocument receipt = ReceiptDocument.builder()
                .orderId(event.orderId().toString())
                .userId(event.userId().toString())
                .items(receiptItems)
                .totalPrice(event.totalPrice().doubleValue())
                .status(OrderStatus.PENDING.name())
                .createdAt(Instant.now())
                .build();

        receiptService.saveReceipt(receipt);
    }
}