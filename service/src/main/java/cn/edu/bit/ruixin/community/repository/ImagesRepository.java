package cn.edu.bit.ruixin.community.repository;

import cn.edu.bit.ruixin.community.domain.Images;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/25
 */
public interface ImagesRepository extends JpaRepository<Images, Integer>, JpaSpecificationExecutor<Images> {
    @Query(nativeQuery = true, value = "SELECT `image_hash` FROM `images` WHERE `gallery` = ?")
    List<String> getAllImageHash(Integer gallery);
}