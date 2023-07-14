package com.lcwd.electronic.store.services.Impl;

import com.lcwd.electronic.store.exceptions.BadApiRequest;
import com.lcwd.electronic.store.services.FileServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileServiceI {
    Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFilename = file.getOriginalFilename();
        logger.info("file name {}", originalFilename);
        String fileName = UUID.randomUUID().toString(); //random created name
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileNameWithExtension = fileName + extension;

        String fullPathWithFileName = path + File.separator + fileNameWithExtension;

        logger.info("full image path: {} ", fullPathWithFileName);
        if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {

            //fileSave
            logger.info("file extension is {}", extension);
            File folder = new File(path);
            if (!folder.exists()) {
                //creating an folder
                folder.mkdirs();
            }

            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;

        } else {
            throw new BadApiRequest("File with exrension: " + extension + " not allowed");
        }
    }
    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        String fullPath = path + File.separator + name;
        FileInputStream inputStream = new FileInputStream((fullPath));
        return inputStream;
    }
}
