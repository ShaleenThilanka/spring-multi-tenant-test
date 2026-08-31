package lk.oktocreative.weddingservice.service.Impl;



import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lk.oktocreative.weddingservice.dto.CommonFileSavedBinaryDataDTO;
import lk.oktocreative.weddingservice.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {


    private final Cloudinary cloudinary;




    @Override
    public String saveFile(MultipartFile file,String folderName) {
        try {
            // Upload the file
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "resource_type", "auto", // Automatically detect image, video, or raw
                            "folder", folderName  // Optional: organize files into folders
                    ));

            // Return the secure URL to save in your database
            return uploadResult.get("secure_url").toString();

            // Note: You should also save uploadResult.get("public_id")
            // in your DB if you want to delete the file later!
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary upload failed: " + e.getMessage());
        }
    }

    @Override
    public String deleteFile(String url) {
        try {
            String publicId = extractPublicIdFromUrl(url);
            System.out.println("Attempting to delete Public ID: " + publicId); // Log this!

            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String status = result.get("result").toString();

            if (status.equals("not_found")) {
                System.err.println("Cloudinary could not find the file with ID: " + publicId);
            }

            return status;
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary deletion failed: " + e.getMessage());
        }
    }
    private String extractPublicIdFromUrl(String url) {
        try {
            // Example URL: https://res.cloudinary.com/cloudname/image/upload/v161234/folder/subfolder/image.jpg

            // 1. Get everything after "/upload/"
            String[] parts = url.split("/upload/");

            // 2. Remove the versioning (e.g., "v123456/") if it exists
            String pathWithExtension = parts[1].replaceFirst("v\\d+/", "");

            // 3. Remove the file extension (.jpg, .png, etc.)
            return pathWithExtension.substring(0, pathWithExtension.lastIndexOf("."));
        } catch (Exception e) {
            throw new RuntimeException("Could not extract Public ID from URL: " + url);
        }
    }

}
