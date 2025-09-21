package com.geek.back.dto;

public class ProductImageDTO {

    private Long id;         // opcional: para update/removal
    private String url;
    private Boolean mainImage;

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Boolean getMainImage() { return mainImage; }
    public void setMainImage(Boolean mainImage) { this.mainImage = mainImage; }
}
