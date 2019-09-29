package com.test.chat.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service for managing files
 */
public interface FileService {
    /**
     * Uploads given file and stors in filesystem
     * with given name
     *
     * @param file file to upload
     * @param picName name for the file
     * @throws IOException throws exception when directory
     * cannot be found and created
     */
    void uploadFile(MultipartFile file, String picName) throws IOException;

    /**
     * Delete file with given name
     * from filesystem
     *
     * @param picName name of the file to be deleted
     * @throws IOException throws exception when file cant be deleted
     */
    void removeFile(String picName) throws IOException;
}
