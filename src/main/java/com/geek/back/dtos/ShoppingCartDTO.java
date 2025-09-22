package com.geek.back.dtos;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartDTO {

//    private Long id;
    private Long userId;
    private List<CartItemDTO> items;
    private BigDecimal total;
}
