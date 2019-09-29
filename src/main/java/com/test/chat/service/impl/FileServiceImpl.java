package com.test.chat.service.impl;

import com.test.chat.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service("fileService")
public class FileServiceImpl implements FileService {
    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Value("${user.upload.path}")
    private String imageUploadPath;

    @Override
    public void uploadFile(MultipartFile file, String picName) throws IOException {
        logger.info("Uploading picture");

        File dir = new File(imageUploadPath);
        if (!dir.exists()) {
            logger.error("Cant find given directory");
            boolean result = dir.mkdir();
            if(!result){
                logger.error("Cant create directory");
                throw new IOException("Error while creating directory");
            }
        }
        File picture = new File(imageUploadPath + picName);
        file.transferTo(picture);
    }

    @Override
    public void removeFile(String picName) throws IOException {
        logger.info("Removing picture");
        File picture = new File(imageUploadPath + picName);
        boolean result = picture.delete();
        if(!result){
            logger.error("Cant delete picture");
            throw new IOException("Error while deleteing file");
        }
    }
}
