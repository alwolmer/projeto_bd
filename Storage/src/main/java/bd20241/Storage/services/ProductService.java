package bd20241.Storage.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Category;
import bd20241.Storage.models.Product;
import bd20241.Storage.payloads.requests.CreateProductRequest;
import bd20241.Storage.payloads.responses.ProductResponse;
import bd20241.Storage.repositories.ProductRepository;
import bd20241.Storage.utils.NanoId;


@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ClassificationService classificationService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, ClassificationService classificationService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.classificationService = classificationService;
    }

    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        Product product = new Product();
        product.setId(NanoId.randomNanoIdForStorage());
        product.setName(createProductRequest.getName());
        product.setWeight(createProductRequest.getWeight());
        product.setVolume(createProductRequest.getVolume());
        productRepository.save(product);
        List<String> categories = new ArrayList<>();
        for (String categoryName : createProductRequest.getCategories()) { 
            Category currentCategory = categoryService.getCategoryByName(categoryName);
            categories.add(currentCategory.getName());
            classificationService.create(product.getId(), currentCategory.getId());
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setWeight(product.getWeight());
        productResponse.setVolume(product.getVolume());
        productResponse.setCategories(categories);
        return productResponse;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(product.getId());
            productResponse.setName(product.getName());
            productResponse.setWeight(product.getWeight());
            productResponse.setVolume(product.getVolume());
            List<String> categories = new ArrayList<>();
            for (String categoryId : classificationService.getCategoriesByProductId(product.getId())) {
                Category category = categoryService.getCategoryById(categoryId);
                categories.add(category.getName());
            }
            productResponse.setCategories(categories);
            productResponses.add(productResponse);
        }
        return productResponses;
    }

    public ProductResponse updateProduct(String id, CreateProductRequest createProductRequest) {
        Product product = productRepository.findById(id);
        product.setName(createProductRequest.getName());
        product.setWeight(createProductRequest.getWeight());
        product.setVolume(createProductRequest.getVolume());
        productRepository.update(product);
        List<String> existingCategoriesIds = classificationService.getCategoriesByProductId(id);
        List<String> categories = new ArrayList<>();
        for (String categoryName : createProductRequest.getCategories()) {
            Category currentCategory = categoryService.getCategoryByName(categoryName);
            if (existingCategoriesIds.contains(currentCategory.getId())) {
                existingCategoriesIds.remove(currentCategory.getId());
                categories.add(currentCategory.getName());
            } else {
                classificationService.create(product.getId(), currentCategory.getId());
                categories.add(currentCategory.getName());
            }     
        }
        for (String categoryId : existingCategoriesIds) {
            classificationService.deleteByProductIdAndCategoryId(product.getId(), categoryId);
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setWeight(product.getWeight());
        productResponse.setVolume(product.getVolume());
        productResponse.setCategories(categories);
        return productResponse;
    }

}
