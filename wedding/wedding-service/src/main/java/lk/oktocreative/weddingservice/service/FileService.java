package lk.oktocreative.weddingservice.service;


import org.springframework.web.multipart.MultipartFile;

public  interface FileService {

    String saveFile(MultipartFile file, String folderName);
    String deleteFile(String url);
}