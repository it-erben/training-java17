package tech.erben.assignments.fulfillment.rest;

public record ApiError(String message, String path, int status) {
}
