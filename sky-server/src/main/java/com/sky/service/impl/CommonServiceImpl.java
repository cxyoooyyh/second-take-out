package com.sky.service.impl;

import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author sharkCode
 * @date 2026/1/5 09:58
 */
@Slf4j
@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private AliOssUtil aliOssUtil;
    /**
     * 通过阿里云oss上传图片文件
     * @param file
     * @return
     */
    @Override
    public String uploadByAliOss(MultipartFile file) {
        String path = null;
        try {
            String originalFilename = file.getOriginalFilename();
            // 获取后缀名
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            // 新文件名称
            String fileName = UUID.randomUUID() + extension;
            path = aliOssUtil.upload(file.getBytes(), fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("alioss结束");
        return path;
    }
}
