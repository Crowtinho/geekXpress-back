package com.geek.back.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequestDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

//    @Positive(message = "Quantity must be greater than 0")
    private int quantity;
}
