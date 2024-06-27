package com.example.RailingShop.Services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.RailingShop.Entities.DeletedProduct;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.Review;
import com.example.RailingShop.Enums.ProductColor;
import com.example.RailingShop.Enums.ProductMaterial;
import com.example.RailingShop.Exceptions.ResourceNotFoundException;
import com.example.RailingShop.Repositories.DeletedProductRepository;
import com.example.RailingShop.Repositories.OrderProductRepository;
import com.example.RailingShop.Repositories.ProductRepository;
import com.example.RailingShop.Repositories.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    DeletedProductRepository deletedProductRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ReviewRepository reviewRepository;


public Product saveProduct(Product product, MultipartFile imageFile) throws IOException {
    if (imageFile != null && !imageFile.isEmpty()) {
        Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
        product.setImageUrl(uploadResult.get("url").toString());
    }
    return productRepository.save(product);
}
    public Optional<Product> getProduct(Long id){
        return productRepository.findById(id);
    }
    public List<Product> getSortedProducts(String sortBy, String sortDirection) {
        List<Product> products = productRepository.findAll();

        Comparator<Product> comparator = switch (sortBy) {
            case "name" -> Comparator.comparing(Product::getName);
            case "price" -> Comparator.comparing(Product::getPrice);
            default -> null;
        };

        if (comparator != null) {
            if ("desc".equals(sortDirection)) {
                comparator = comparator.reversed();
            }
            products.sort(comparator);
        }
        return products;
    }


    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product id: " + productId));
        // Изтриване на свързаните записи в order_products
        orderProductRepository.deleteByProductId(productId);

        // Преместване на продукта в deleted_products
        DeletedProduct deletedProduct = new DeletedProduct(product);
        deletedProductRepository.save(deletedProduct);

        // Изтриване на продукта
        productRepository.delete(product);
    }



    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
    }

    public List<Product> searchProducts(Long id, String name, Integer quantity, BigDecimal priceMin, BigDecimal priceMax) {
        if (priceMin != null) {
            priceMin = priceMin.setScale(2, RoundingMode.HALF_UP);
        }
        if (priceMax != null) {
            priceMax = priceMax.setScale(2, RoundingMode.HALF_UP);
        }

        return productRepository.searchProducts(id, name, quantity, priceMin, priceMax);
    }

    public void save(Product product) {
        productRepository.save(product);
    }


    public List<Product> findAllAvailableQuantity(String keyword) {
        List<Product> availableProducts = new ArrayList<>();

        if (keyword != null) {
            List<Product> searchResults = productRepository.findByDescriptionContainingIgnoreCase(keyword);

            for (Product product : searchResults) {
                if (product.getQuantity() > 0) {
                    availableProducts.add(product);
                }
            }
        } else {
            List<Product> allProducts = productRepository.findAll();

            for (Product product : allProducts) {
                if (product.getQuantity() > 0) {
                    availableProducts.add(product);
                }
            }
        }

        return availableProducts;
    }
    public double getAverageRatingForProduct(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }

    public List<Product> getProductsSortedByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    public List<Product> getProductsSortedByPriceDesc() {
        return productRepository.findAllByOrderByPriceDesc();
    }

    public List<Product> getProductsSortedByReviewsCount() {
        return productRepository.findAllOrderByReviewsCount();
    }



    public List<Product> getFilteredProducts(String keyword, String sortBy, boolean inStockOnly, double minRating,
                                             BigDecimal minPrice, BigDecimal maxPrice, ProductColor color,
                                             ProductMaterial material, String manufacturer) {
        List<Product> products = productRepository.findAll();

        if (keyword != null && !keyword.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getName().contains(keyword) || p.getDescription().contains(keyword))
                    .collect(Collectors.toList());
        }

        if (inStockOnly) {
            products = products.stream()
                    .filter(Product::isAvailable)
                    .collect(Collectors.toList());
        }

        if (minRating > 0) {
            products = products.stream()
                    .filter(p -> p.getAverageRating() >= minRating)
                    .collect(Collectors.toList());
        }

        if (minPrice != null) {
            products = products.stream()
                    .filter(p -> p.getPrice().compareTo(minPrice) >= 0)
                    .collect(Collectors.toList());
        }

        if (maxPrice != null) {
            products = products.stream()
                    .filter(p -> p.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
        }

        if (color != null) {
            products = products.stream()
                    .filter(p -> p.getColor() == color)
                    .collect(Collectors.toList());
        }

        if (material != null) {
            products = products.stream()
                    .filter(p -> p.getMaterial() == material)
                    .collect(Collectors.toList());
        }

        if (manufacturer != null && !manufacturer.isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getManufacturer().equalsIgnoreCase(manufacturer))
                    .collect(Collectors.toList());
        }

        Comparator<Product> comparator = switch (sortBy) {
            case "priceAsc" -> Comparator.comparing(Product::getPrice);
            case "priceDesc" -> Comparator.comparing(Product::getPrice).reversed();
            case "reviews" -> Comparator.comparing(Product::getReviewsCount).reversed();
            default -> Comparator.comparing(Product::getName);
        };

        products.sort(comparator);

        return products;
    }

}
