package tech.erben.java17.fooddelivery.solution.model;

import java.time.LocalDateTime;
import java.util.List;

public record Order(
        String orderNumber,
        Customer customer,
        Restaurant restaurant,
        List<String> items,
        double totalAmount,
        LocalDateTime placedAt
) {
}
