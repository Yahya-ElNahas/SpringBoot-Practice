package com.practice.main.receipt.application;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.practice.main.receipt.application.exception.ReceiptNotFoundException;
import com.practice.main.receipt.application.exception.ReceiptStorageException;
import com.practice.main.receipt.domain.ReceiptDocument;
import com.practice.main.security.principal.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiptService {

    private final Firestore firestore;

    private final String COLLECTION = "order_receipts";

    public void saveReceipt(ReceiptDocument receiptDocument) {
        try {
            firestore.collection(COLLECTION)
                    .document(receiptDocument.getOrderId().toString())
                    .set(receiptDocument)
                    .get();

            log.info("Receipt document saved to Firebase: orderId={}", receiptDocument.getOrderId());
        } catch (Exception e) {
            log.error("Failed to save receipt document to firebase: orderId={}", receiptDocument.getOrderId(), e);

            throw new ReceiptStorageException();
        }
    }

    public ReceiptDocument getReceipt(UUID orderId) {
        try {
            DocumentSnapshot documentSnapshot = firestore.collection(COLLECTION)
                    .document(orderId.toString())
                    .get()
                    .get();

            if(!documentSnapshot.exists()) {
                throw new ReceiptNotFoundException();
            }

            return documentSnapshot.toObject(ReceiptDocument.class);
        } catch (Exception e) {
            log.error("Failed to fetch receipt document: orderId={}", orderId);

            throw new ReceiptNotFoundException();
        }
    }

    public List<ReceiptDocument> getUserReceipts(UserPrincipal userPrincipal) {
        UUID userId = userPrincipal.userId();

        try {
            List<QueryDocumentSnapshot> documentSnapshots = firestore.collection(COLLECTION)
                    .whereEqualTo("userId", userId.toString())
                    .get().get()
                    .getDocuments();

            return documentSnapshots.stream().map(
                    snapshot -> snapshot.toObject(ReceiptDocument.class)
            ).toList();
        } catch (Exception e) {
            log.error("Failed to fetch receipt documents: userId={}", userId);

            throw new RuntimeException(e);
        }
    }
}
