package cn.edu.bit.ruixin.community.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/15
 */
public interface FileUpDownloadService {
    String uploadRoomImage(MultipartFile file, Integer room);
}
