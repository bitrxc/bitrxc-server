package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.community.repository.ImagesRepository;
import cn.edu.bit.ruixin.community.service.ImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/25
 */
@Service
public class ImagesServiceImpl implements ImagesService {

    @Autowired
    private ImagesRepository imagesRepository;

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    @Override
    public List<String> getAllImagesByGalleryId(Integer gallery) {
        List<String> images = imagesRepository.getAllImageHash(gallery);
        return null;
    }
}
