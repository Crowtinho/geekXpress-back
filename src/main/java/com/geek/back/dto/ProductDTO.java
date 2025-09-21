package com.geek.back.dto;

import java.math.BigDecimal;
import java.util.Set;

public class ProductDTO {

    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private Set<Long> categoryIds; // 👈 Solo enviamos los IDs
    private Set<ProductImageDTO> imagesToAdd;  // nuevas imágenes a crear
    private Set<Long> imageIdsToRemove;

    // Getters y setters
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public Set<Long> getCategoryIds() {
        return categoryIds;
    }
    public void setCategoryIds(Set<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Set<ProductImageDTO> getImagesToAdd() {
        return imagesToAdd;
    }

    public void setImagesToAdd(Set<ProductImageDTO> imagesToAdd) {
        this.imagesToAdd = imagesToAdd;
    }

    public Set<Long> getImageIdsToRemove() {
        return imageIdsToRemove;
    }

    public void setImageIdsToRemove(Set<Long> imageIdsToRemove) {
        this.imageIdsToRemove = imageIdsToRemove;
    }
}
