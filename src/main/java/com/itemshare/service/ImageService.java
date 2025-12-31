package com.itemshare.service;

import com.itemshare.entity.Image;
import com.itemshare.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public List<Image> findByItemIdOrderBySortOrderAsc(Long itemId) {
        return imageRepository.findByItemIdOrderBySortOrderAsc(itemId);
    }

    public List<Image> findByItemIdAndIsMainTrue(Long itemId) {
        return imageRepository.findByItemIdAndIsMainTrue(itemId);
    }

    public List<Image> findByItemIdsOrderBySortOrder(List<Long> itemIds) {
        return imageRepository.findByItemIdsOrderBySortOrder(itemIds);
    }

    public void deleteByItemId(Long itemId) {
        imageRepository.deleteByItemId(itemId);
    }

    public void deleteById(Long imageId) {
        imageRepository.deleteById(imageId);
    }
}
