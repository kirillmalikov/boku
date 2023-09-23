package com.boku.boku.service.merchant.model;

public record MerchantRequest(
        String shortcode,
        String keyword,
        String message,
        String operator,
        String sender,
        String transaction_id
) {}
