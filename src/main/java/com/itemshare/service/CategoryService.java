package com.itemshare.service;

import com.itemshare.entity.Category;
import com.itemshare.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public List<Category> findByParentIdOrderBySortOrderAsc(Long parentId) {
        return categoryRepository.findByParentIdOrderBySortOrderAsc(parentId);
    }

    public List<Category> findByStatusOrderBySortOrderAsc(Byte status) {
        return categoryRepository.findByStatusOrderBySortOrderAsc(status);
    }

    public List<Category> findRootCategories() {
        return categoryRepository.findRootCategories();
    }

    public List<Category> findByParentIdAndStatus(Long parentId) {
        return categoryRepository.findByParentIdAndStatus(parentId);
    }

    public void deleteCategory(Long categoryId) {
        categoryRepository.findById(categoryId).ifPresent(category -> {
            category.setStatus((byte)0); // 设置为禁用状态
            categoryRepository.save(category);
        });
    }

    public void updateCategoryStatus(Long categoryId, Byte status) {
        categoryRepository.findById(categoryId).ifPresent(category -> {
            category.setStatus(status);
            categoryRepository.save(category);
        });
    }
}
