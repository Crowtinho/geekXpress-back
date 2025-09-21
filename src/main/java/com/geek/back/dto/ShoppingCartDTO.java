package com.geek.back.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartDTO {
    private Long userId;
    private List<CartItemDTO> items;

}
