package com.geek.back.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDTO {

    @NotBlank(message = "Name is required")
    @Size(min = 2,max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

}
