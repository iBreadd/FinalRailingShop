package com.example.RailingShop.Controllers;

import com.cloudinary.Cloudinary;
import com.example.RailingShop.Entities.OrderProduct;
import com.example.RailingShop.Entities.Product;
import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Enums.ProductColor;
import com.example.RailingShop.Enums.ProductMaterial;
import com.example.RailingShop.Repositories.ProductRepository;
import com.example.RailingShop.Repositories.UserRepository;
import com.example.RailingShop.Services.ProductService;
import com.example.RailingShop.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
        Product product = new Product();
        model.addAttribute("product", new Product());
        return "addProduct";
    }

    @PostMapping("/addProduct")
    public String addProduct(Product product, MultipartFile imageFile,RedirectAttributes redirectAttributes){
        if (!imageFile.isEmpty()) {
            try {
                String imageUrl = imageService.uploadImage(imageFile);
                product.setImageUrl(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        product.setAvailable(product.getQuantity() > 0);
        productRepository.save(product);
        redirectAttributes.addFlashAttribute("message", "You add a new product!");
        return "redirect:/shop/products";
    }

    @GetMapping("/products")
    public String getProducts(@RequestParam(defaultValue = "id") String sortBy,
                              @RequestParam(defaultValue = "asc") String sortDirection,
                              Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
        List<Product> products = productService.getSortedProducts(sortBy, sortDirection);
        model.addAttribute("products", products);
        model.addAttribute("sortDirection", sortDirection);
        return "products";
    }

    @PostMapping("/delete/{productId}")
    private ModelAndView deleteProduct(@PathVariable(name = "productId") Long productId,RedirectAttributes redirectAttributes) {
        productService.deleteProduct(productId);
        redirectAttributes.addFlashAttribute("message", "You delete a product!");
        return new ModelAndView("redirect:/shop/products");
    }

    @GetMapping("/editProduct/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model,RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "redirect:/shop/login";
        }
        model.addAttribute("product", productService.getProductById(id));
        return "editProduct";
    }

    @PostMapping("/editProduct/{id}")
    public String updateProduct(RedirectAttributes redirectAttributes,@PathVariable Long id, @ModelAttribute Product product, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        if (existingProduct == null) {
            return "redirect:/shop/products";
        }

        // Актуализация на данните на продукта
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setManufacturer(product.getManufacturer());
        existingProduct.setAvailable(product.getQuantity() > 0);
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setMaterial(product.getMaterial());
        existingProduct.setColor(product.getColor());
        existingProduct.setPrice(product.getPrice());
        // Добавете останалите полета, които трябва да бъдат актуализирани

        // Проверка дали е подаден файл за качване на изображение
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = imageService.uploadImage(imageFile); // Качване на новото изображение
                existingProduct.setImageUrl(imageUrl); // Актуализиране на URL на изображението
            } catch (IOException e) {
                e.printStackTrace(); // Обработка на грешка при качването на изображението
            }
        }

        // Запазване на актуализирания продукт в репозитория
        productRepository.save(existingProduct);
        redirectAttributes.addFlashAttribute("message", "You edit a product!");

        return "redirect:/shop/products"; // Изберете подходящо URL за пренасочване
    }



    @PostMapping("/products")
    public String searchProducts(
            @RequestParam(name = "searchById", required = false) Long searchById,
            @RequestParam(name = "searchByName", required = false) String searchByName,
            @RequestParam(name = "searchByQuantity", required = false) Integer searchByQuantity,
            @RequestParam(name = "price-min", required = false) BigDecimal priceMin,
            @RequestParam(name = "price-max", required = false) BigDecimal priceMax,
            Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user==null){
            return "redirect:/shop/login";
        }
        if (priceMin != null) {
            priceMin = priceMin.setScale(2, RoundingMode.HALF_UP);
        }
        if (priceMax != null) {
            priceMax = priceMax.setScale(2, RoundingMode.HALF_UP);
        }
        List<Product> products = productService.searchProducts(searchById, searchByName, searchByQuantity,priceMin, priceMax);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/all")
    public String showShop(Model model,
                           @RequestParam(required = false) String keyword,
                           @RequestParam(defaultValue = "name") String sortBy,
                           @RequestParam(defaultValue = "false") boolean inStockOnly,
                           @RequestParam(defaultValue = "0") double minRating,
                           @RequestParam(required = false) BigDecimal minPrice,
                           @RequestParam(required = false) BigDecimal maxPrice,
                           @RequestParam(required = false) ProductColor color,
                           @RequestParam(required = false) ProductMaterial material,
                           @RequestParam(required = false) String manufacturer,
                           OrderProduct orderProduct) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // Ако потребителят е аутентициран, вземаме информацията за него
            String username = authentication.getName();
            User user = userRepository.findByUsername(username);

            if (user != null) {
                // Подаваме информацията за потребителя на модела
                model.addAttribute("user", user);
                model.addAttribute("userRole", user.getRole().toString());
            }
        }


        // Вземаме списък с филтрирани продукти от сервисния слой
        List<Product> products = productService.getFilteredProducts(keyword, sortBy, inStockOnly, minRating, minPrice, maxPrice, color, material, manufacturer);

        // Подаваме необходимите атрибути на модела
        model.addAttribute("products", products);
        model.addAttribute("items", orderProduct.getQuantity());

        // Връщаме шаблона за страницата с продуктите
        return "all";
    }

}

