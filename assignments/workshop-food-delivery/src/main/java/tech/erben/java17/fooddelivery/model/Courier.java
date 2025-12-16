package tech.erben.java17.fooddelivery.model;

public record Courier(
        String name,
        String vehicleType,
        boolean onShift,
        double rating
) {
}
