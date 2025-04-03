package com.banking.model;

public record UnifiedProduct(int productUid,
                             String productType,
                             String name,
                             String fullUrl,
                             double unitPrice,
                             String unitPriceMeasure,
                             int unitPriceMeasureAmount) {
}
