package lk.oktocreative.weddingservice.util;



import lk.oktocreative.weddingservice.dto.CommonFileSavedBinaryDataDTO;
import lk.oktocreative.weddingservice.dto.CommonFileSavedSimpleDataDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.SQLException;

@Service
public class FileDataExtractor {
    public CommonFileSavedSimpleDataDTO toStringDataObject(CommonFileSavedBinaryDataDTO dto) {
        try {
            String hash;
            String fileName;
            String resourceUrl;
            StringBuffer buf = new StringBuffer();
            String temp;
            BufferedReader bufReader = null;
            bufReader = new BufferedReader(new InputStreamReader(dto.getHash().getBinaryStream()));
            while ((temp = bufReader.readLine()) != null) {
                buf.append(temp);
            }
            hash = buf.toString();
            buf = new StringBuffer();
            bufReader = new BufferedReader(new InputStreamReader(dto.getFileName().getBinaryStream()));
            while ((temp = bufReader.readLine()) != null) {
                buf.append(temp);
            }
            fileName = buf.toString();
            buf = new StringBuffer();
            bufReader = new BufferedReader(new InputStreamReader(dto.getResourceUrl().getBinaryStream()));
            while ((temp = bufReader.readLine()) != null) {
                buf.append(temp);
            }
            resourceUrl = buf.toString();
            buf = new StringBuffer();
            return new CommonFileSavedSimpleDataDTO(hash, dto.getDirectory(), fileName, resourceUrl);
        } catch (SQLException | IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String extractActualFileName(InputStreamReader data) {
        try {
            StringBuffer buf = new StringBuffer();
            String temp;
            BufferedReader bufReader = null;
            buf = new StringBuffer();
            bufReader = new BufferedReader(data);
            while ((temp = bufReader.readLine()) != null) {
                buf.append(temp);
            }
            return buf.toString();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public String blobToString(Blob blob) {
        if (blob == null) return null;
        try {
            return new String(blob.getBytes(1, (int) blob.length()));
        } catch (SQLException e) {
            return " ";
        }
    }

}

