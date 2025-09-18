package com.geek.back.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {

    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;

}
