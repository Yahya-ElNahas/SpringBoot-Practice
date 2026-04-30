package com.practice.main.receipt.controller;

import com.practice.main.common.response.ApiResponse;
import com.practice.main.receipt.application.ReceiptService;
import com.practice.main.receipt.domain.ReceiptDocument;
import com.practice.main.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/receipts")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ReceiptDocument>>> getReceipts(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<ReceiptDocument> result = receiptService.getUserReceipts(userPrincipal);

        return ResponseEntity.ok(ApiResponse.success(result));
    }
}
