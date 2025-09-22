package com.geek.back.services;

import com.geek.back.dto.ProductDTO;
import com.geek.back.dto.ProductImageDTO;
import com.geek.back.models.Category;
import com.geek.back.models.Product;
import com.geek.back.models.ImageProduct;
import com.geek.back.repositories.CategoryRepository;
import com.geek.back.repositories.ProductImageRepository;
import com.geek.back.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ProductImageRepository productImageRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.categoryRepository = categoryRepository;
    }

    // Mapea Product a ProductDTO
    private ProductDTO toDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());

        if (product.getCategories() != null) {
            dto.setCategoryIds(product.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet()));
        }

        if (product.getImages() != null) {
            dto.setImagesToAdd(product.getImages().stream().map(img -> {
                ProductImageDTO imgDto = new ProductImageDTO();
                imgDto.setId(img.getId());
                imgDto.setUrl(img.getUrl());
                imgDto.setMainImage(img.isMainImage());
                return imgDto;
            }).collect(Collectors.toSet()));
        }

        return dto;
    }

    // --------------------- CRUD ---------------------

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findById(Long id) {
        return productRepository.findById(id)
                .map(this::toDTO);
    }

    @Override
    @Transactional
    public ProductDTO save(ProductDTO dto) {
        return saveOrUpdate(dto, null)
                .orElseThrow(() -> new RuntimeException("Error al guardar el producto"));
    }

    @Transactional
    public Optional<ProductDTO> saveOrUpdate(ProductDTO dto, Long id) {
        Product product = id == null
                ? new Product()
                : productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        // Categorías
        if (dto.getCategoryIds() != null) {
            Set<Category> categories = dto.getCategoryIds().stream()
                    .map(cid -> categoryRepository.findById(cid)
                            .orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada: " + cid)))
                    .collect(Collectors.toSet());
            product.setCategories(categories);
        }

        product = productRepository.save(product);

        // Imágenes a agregar
        if (dto.getImagesToAdd() != null && !dto.getImagesToAdd().isEmpty()) {
            final Product productFinal = product;

            Set<ImageProduct> newImages = dto.getImagesToAdd().stream()
                    .map(imgDto -> {
                        ImageProduct img = new ImageProduct();
                        img.setUrl(imgDto.getUrl());
                        img.setMainImage(Boolean.TRUE.equals(imgDto.getMainImage()));
                        img.setProduct(productFinal);
                        return img;
                    }).collect(Collectors.toSet());

            productImageRepository.saveAll(newImages);
            product.getImages().addAll(newImages);
        }

        // Imágenes a eliminar
        if (dto.getImageIdsToRemove() != null && !dto.getImageIdsToRemove().isEmpty()) {
            product.getImages().removeIf(img -> dto.getImageIdsToRemove().contains(img.getId()));
            dto.getImageIdsToRemove().forEach(imgId ->
                    productImageRepository.findById(imgId).ifPresent(productImageRepository::delete));
        }

        return Optional.of(toDTO(productRepository.save(product)));
    }

    @Transactional
    public Optional<ProductDTO> deleteById(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return toDTO(product);
                });
    }

    // --------------------- Manejo de imágenes ---------------------

    @Override
    @Transactional
    public Product addImageToProduct(Long productId, ImageProduct image) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        image.setProduct(product);
        product.getImages().add(image);
        productImageRepository.save(image);

        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product removeImageFromProduct(Long productId, Long imageId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado"));

        product.getImages().removeIf(img -> img.getId().equals(imageId));
        productImageRepository.findById(imageId).ifPresent(productImageRepository::delete);

        return productRepository.save(product);
    }
}
