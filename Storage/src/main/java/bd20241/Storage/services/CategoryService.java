package bd20241.Storage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Category;
import bd20241.Storage.payloads.requests.CreateCategoryRequest;
import bd20241.Storage.repositories.CategoryRepository;
import bd20241.Storage.utils.NanoId;

@Service
public class CategoryService {
    
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public boolean categoryExistsByName(String name) {
        return categoryRepository.findByName(name) != null;
    }

    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    public Category createCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setId(NanoId.randomNanoIdForStorage());
        category.setName(request.getName());
        categoryRepository.save(category);
        return category;
    }

    public Category getCategoryById(String id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }

    public Category updateCategory(String id, CreateCategoryRequest request) {
        categoryRepository.updateName(id, request.getName());
        Category category = new Category();
        category.setId(id);
        category.setName(request.getName());
        return category;
    }
}
