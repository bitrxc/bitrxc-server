package cn.edu.bit.ruixin.community.service.impl;

import cn.edu.bit.ruixin.base.security.utils.IdWorker;
import cn.edu.bit.ruixin.community.domain.Images;
import cn.edu.bit.ruixin.community.exception.FileUploadDownloadException;
import cn.edu.bit.ruixin.community.myenum.ImageType;
import cn.edu.bit.ruixin.community.service.FileUpDownloadService;
import cn.edu.bit.ruixin.community.service.ImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/15
 */
@Service
public class FileUpDownloadServiceImpl implements FileUpDownloadService {

    @Autowired
    private IdWorker idWorker;
    
    @Autowired
    private ImagesService imagesService;

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    @Override
    public String uploadRoomImage(MultipartFile file, Integer room) {
        // 获取文件类型
        String type = file.getContentType();
        if (type.equals(ImageType.JPG.getType()) || type.equals(ImageType.PNG.getType())) {
            ByteArrayInputStream inputStream = null;
            FileOutputStream outputStream = null;
            FileChannel outChannel = null;
            try {
                // 获取jar包路径
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                String absolutePath = path.getAbsolutePath() + File.separator + "static" + File.separator + "image" + File.separator + "room";
                File absFile = new File(absolutePath);
                if (!absFile.exists()) {
                    absFile.mkdirs();
                }
                // 生成文件名，使用雪花算法生成全局ID
                Long id = idWorker.nextId();
                String filename = id.toString() + ((type.equals(ImageType.JPG.getType()))?".jpeg":".png");

                // 存入数据库
                Images images = new Images();
                images.setRoom(room);
                images.setImageHash(filename);

                String filepath = absolutePath + File.separator + filename;
                inputStream = (ByteArrayInputStream) file.getInputStream();
                outputStream = new FileOutputStream(filepath);
                // 使用NIO写文件
                outChannel = outputStream.getChannel();

                byte[] bytes = new byte[16*1024];

                ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);

                while (inputStream.read(bytes) > 0) {
                    buffer.put(bytes);
                    buffer.flip();
                    outChannel.write(buffer);
                    buffer.clear();
                }
                // 返回url
                return filename;

            } catch (IOException e) {
//                System.out.println("读写文件异常");
                e.printStackTrace();
                throw new FileUploadDownloadException("文件上传失败");
            } finally {
                if (outChannel != null) {
                    try {
                        outChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            // 抛出文件格式异常
            throw new FileUploadDownloadException("上传文件格式不正确");
        }
    }


}
