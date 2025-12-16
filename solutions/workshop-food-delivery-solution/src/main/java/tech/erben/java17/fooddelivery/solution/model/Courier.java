package tech.erben.java17.fooddelivery.solution.model;

public record Courier(
        String name,
        String vehicleType,
        boolean onShift,
        double rating
) {
}
