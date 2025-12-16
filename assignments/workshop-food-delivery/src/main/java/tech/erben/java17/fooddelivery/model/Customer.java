package tech.erben.java17.fooddelivery.model;

public record Customer(
        String name,
        String phoneNumber,
        String loyaltyTier
) {
}
