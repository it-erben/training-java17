package tech.erben.java17.httpclient;

public record OrderRequest(String sku, int quantity, boolean express) {
}
