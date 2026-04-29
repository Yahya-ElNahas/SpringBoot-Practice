package com.practice.main.order.application.scheduler;

import com.practice.main.order.domain.Order;
import com.practice.main.order.infrastructure.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SalesReportScheduler {

    private final OrderRepository orderRepository;

    @Scheduled(cron = "0 0 * * * *")
    public void generateSalesReport() {
        Instant now = Instant.now();
        Instant lastHour = now.minus(1, ChronoUnit.HOURS);

        List<Order> orders = orderRepository.findAllByCreatedAtBetween(lastHour, now);

        if(orders.isEmpty()) {
            log.info("No orders in the last hour");
            return;
        }

        try {
            generateCsv(orders, now);
            log.info("Order report for last hour has been generated");
        } catch (IOException e) {
            log.error("Error while generating order report for last hour", e);
        }
    }

    private void generateCsv(List<Order> orders, Instant date) throws IOException {
        Path dir = Paths.get("C:\\Users\\Karas\\Downloads\\test\\SpringBoot-Practice\\sales_reports");
        Files.createDirectories(dir);
        Path path = dir.resolve(date.toString().replace(":", "-") + ".csv");

        try (Writer writer = Files.newBufferedWriter(path)) {
            writer.write("Order ID, User ID, Total Items, Total Price, Created At");
            writer.write(System.lineSeparator());

            for (Order order : orders) {
                writer.write(String.join(",",
                        order.getId().toString(),
                        order.getUserId().toString(),
                        order.getTotalItems() + "",
                        order.getTotalPrice().toString(),
                        order.getCreatedAt().toString()
                ));
                writer.write(System.lineSeparator());
            }
        }
    }
}