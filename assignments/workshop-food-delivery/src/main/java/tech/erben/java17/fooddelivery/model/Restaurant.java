package tech.erben.java17.fooddelivery.model;

public record Restaurant(
        String name,
        String cityDistrict,
        boolean offersExpressPickup
) {
}
