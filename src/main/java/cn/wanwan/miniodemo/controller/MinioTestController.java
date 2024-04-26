package cn.wanwan.miniodemo.controller;

import cn.wanwan.miniodemo.utils.MinioUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@RestController
@RequestMapping("/minioTestController")
public class MinioTestController {

    @Resource
    private MinioUtil minioUtil;

    @GetMapping("/downloadFile")
    public void downloadFile(@RequestParam HttpServletResponse response, String bucketName, String filePath, String fileName) throws IOException {
        try (InputStream inputStream = minioUtil.downloadFile(bucketName, filePath)) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            OutputStream outStream = response.getOutputStream();
            byte[] bytes = new byte[2048];
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                outStream.write(bytes, 0, len);
            }
        }
    }

    @PostMapping("/uploadFile")
    public void uploadFile(MultipartFile multipartFile, String bucketName, String filePath) throws Exception {
        minioUtil.uploadFile(multipartFile.getInputStream(), bucketName, filePath);
    }
}
