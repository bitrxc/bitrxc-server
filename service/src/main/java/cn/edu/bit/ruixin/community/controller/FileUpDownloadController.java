package cn.edu.bit.ruixin.community.controller;

import cn.edu.bit.ruixin.base.common.CommonResult;
import cn.edu.bit.ruixin.base.common.ResultCode;
import cn.edu.bit.ruixin.community.service.FileUpDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * TODO
 *
 * @author 78165
 * @date 2021/3/15
 */
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class FileUpDownloadController {

    @Autowired
    private FileUpDownloadService fileUpDownloadService;

    @PostMapping("/upload/image/room/{id}")
    public CommonResult uploadRoomImage(@RequestParam(value = "file", required = true) MultipartFile file,
                                        @PathVariable(name = "id") Integer room) {
        String url = fileUpDownloadService.uploadRoomImage(file, room);
        if (url != null && !url.equals("")) {
            return CommonResult.ok(ResultCode.SUCCESS).msg("图片上传成功！").data("url", url);
        } else {
            return CommonResult.error(ResultCode.INTERNAL_SERVER_ERROR).msg("图片上传失败！");
        }
    }


    /**
     * 后期配置 Nginx 静态资源，不建议使用该接口
     * @param filename
     * @param response
     * @throws IOException
     */
    @GetMapping("/download/image/room/{url}")
    public void getRoomImage(@PathVariable(name = "url")String filename, HttpServletResponse response) throws IOException {
        File path = new File(ResourceUtils.getURL("classpath:").getPath());
        String absolutePath = "static" + File.separator + "image" + File.separator + "room";
        String filepath = absolutePath + File.separator + filename;
        FileInputStream inputStream = new FileInputStream(filepath);
        byte[] buf = new byte[4*1024];
        int len = 0;
        response.setContentType("image/jpeg");
        response.setStatus(HttpStatus.OK.value());
        response.setCharacterEncoding("UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        while ((len = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
        }

        inputStream.close();
    }
}
