package com.btcturk.apitest.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@Jacksonized
@EqualsAndHashCode
public class Product {
    @NonNull
    private Integer id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private BigDecimal price;
    @NonNull
    private Boolean isBasketDiscount;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer basketDiscountPercentage;
    @NonNull
    private Double rating;
    @NonNull
    private Integer stock;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isActive;
    @NonNull
    private String brand;
    @NonNull
    private String category;
    @NonNull
    private List<String> images;

}
