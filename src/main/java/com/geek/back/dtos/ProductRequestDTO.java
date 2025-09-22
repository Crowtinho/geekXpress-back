package com.geek.back.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @Positive(message = "Stock must be greater or equal to 0")
    private int stock;

    @NotNull(message = "At least one category must be assigned")
    private Set<Long> categoryIds;

    @NotEmpty(message = "At least one image is required")
    List<ImageProductRequestDTO> images;


}
