package cn.edu.bit.ruixin.community.service;

import cn.edu.bit.ruixin.community.domain.Images;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/25
 */
public interface ImagesService {
    List<String> getAllImagesByGalleryId(Integer gallery);
    Integer addImages(Images images);
}
