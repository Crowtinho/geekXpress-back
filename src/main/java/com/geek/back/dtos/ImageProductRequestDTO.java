package com.geek.back.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageProductRequestDTO {

    @NotBlank(message = "Image URL is required")
    private String url;

    private boolean mainImage;
}
