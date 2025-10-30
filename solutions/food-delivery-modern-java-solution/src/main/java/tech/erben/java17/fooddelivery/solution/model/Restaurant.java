package tech.erben.java17.fooddelivery.solution.model;

public record Restaurant(
        String name,
        String cityDistrict,
        boolean offersExpressPickup
) {
}
