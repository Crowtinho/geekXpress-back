package com.geek.back.services;

import com.geek.back.dtos.ImageProductDTO;
import com.geek.back.dtos.ImageProductRequestDTO;
import com.geek.back.dtos.ProductDTO;
import com.geek.back.dtos.ProductRequestDTO;
import com.geek.back.entities.Category;
import com.geek.back.entities.ImageProduct;
import com.geek.back.entities.Product;
import com.geek.back.repositories.CategoryRepository;
import com.geek.back.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;


    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findById(Long id) {
        return productRepository.findById(id).map(this::toDTO);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        productRepository.findById(id).map(p -> {
            productRepository.deleteById(id);
            return p;
        }).orElseThrow(() -> new RuntimeException("Product not found: " + id));
    }

    @Override
    @Transactional
    public ProductDTO createProduct(ProductRequestDTO request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());

        // --------------------
        // Manejo de categorías nuevas/existentes
        // --------------------
        Set<Category> categories = request.getCategoryNames() // ahora enviamos nombres, no solo IDs
                .stream()
                .map(name -> {
                    return categoryRepository.findByName(name)
                            .orElseGet(() -> {
                                // Si no existe, crear nueva categoría
                                Category newCat = new Category();
                                newCat.setName(name);
                                return categoryRepository.save(newCat);
                            });
                })
                .collect(Collectors.toSet());
        product.setCategories(categories);

        // --------------------
        // Asociar imágenes
        // --------------------
        if (request.getImages() != null) {
            List<ImageProduct> images = request.getImages()
                    .stream()
                    .map(imgDTO -> {
                        ImageProduct img = new ImageProduct();
                        img.setUrl(imgDTO.getUrl());
                        img.setMainImage(imgDTO.isMainImage());
                        img.setProduct(product);
                        return img;
                    })
                    .toList();
            product.setImageProducts(images);
        }

        return toDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductRequestDTO request) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(request.getName());
                    product.setDescription(request.getDescription());
                    product.setPrice(request.getPrice());
                    product.setStock(request.getStock());

                    // --------------------
                    // Manejo seguro de categorías nuevas/existentes
                    // --------------------
                    Set<Category> categories = request.getCategoryNames()
                            .stream()
                            .map(name -> categoryRepository.findByName(name)
                                    .orElseGet(() -> {
                                        Category newCat = new Category();
                                        newCat.setName(name);
                                        return categoryRepository.save(newCat);
                                    }))
                            .collect(Collectors.toSet());
                    product.setCategories(categories);

                    // --------------------
                    // Actualizar imágenes (igual que antes)
                    // --------------------
                    List<ImageProductRequestDTO> requestImages = request.getImages() != null ? request.getImages() : List.of();
                    Map<String, ImageProduct> existingImagesMap = product.getImageProducts()
                            .stream()
                            .collect(Collectors.toMap(ImageProduct::getUrl, img -> img));
                    List<ImageProduct> updatedImages = new ArrayList<>();
                    for (ImageProductRequestDTO imgDTO : requestImages) {
                        if (updatedImages.stream().anyMatch(i -> i.getUrl().equals(imgDTO.getUrl()))) continue;
                        ImageProduct img = existingImagesMap.get(imgDTO.getUrl());
                        if (img != null) {
                            img.setMainImage(imgDTO.isMainImage());
                        } else {
                            img = new ImageProduct();
                            img.setUrl(imgDTO.getUrl());
                            img.setMainImage(imgDTO.isMainImage());
                            img.setProduct(product);
                        }
                        updatedImages.add(img);
                    }
                    product.getImageProducts().clear();
                    product.getImageProducts().addAll(updatedImages);
                    updatedImages.forEach(img -> img.setProduct(product));

                    return toDTO(productRepository.save(product));
                })
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }


    private ProductDTO toDTO(Product product) {
        if (product == null) return null;

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());

        // Nombres de categorías
        Set<String> categoryNames = product.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toSet());
        dto.setCategories(categoryNames);

        // Imágenes
        List<ImageProductDTO> images = product.getImageProducts()
                .stream()
                .map(img -> {
                    ImageProductDTO imgDTO = new ImageProductDTO();
                    imgDTO.setUrl(img.getUrl());
                    imgDTO.setMainImage(img.isMainImage());
                    return imgDTO;
                })
                .toList();
        dto.setImages(images);

        return dto;
    }

}
