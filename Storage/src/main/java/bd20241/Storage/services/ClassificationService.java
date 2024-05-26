package bd20241.Storage.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Classification;
import bd20241.Storage.repositories.ClassificationRepository;

@Service
public class ClassificationService {
    private final ClassificationRepository classificationRepository;

    public ClassificationService(ClassificationRepository classificationRepository) {
        this.classificationRepository = classificationRepository;
    }

    public Classification create(String productId, String categoryId) {
        Classification classification = new Classification();
        classification.setProductId(productId);
        classification.setCategoryId(categoryId);
        classificationRepository.save(classification);
        return classification;
    }

    public List<String> getCategoriesByProductId(String productId) {
        List<Classification> classifications = classificationRepository.findByProductId(productId);
        List<String> categoryIds = new ArrayList<>();
        for (Classification classification : classifications) {
            categoryIds.add(classification.getCategoryId());
        }
        return categoryIds;
    } 

    public void deleteByProductIdAndCategoryId(String productId, String categoryId) {
        classificationRepository.deleteByProductIdAndCategoryId(productId, categoryId);
    }
    
}
