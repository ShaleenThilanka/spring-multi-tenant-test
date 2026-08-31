package com.shaliya.springmultitenant.springmultitenant.dto.core;



import au.com.aurigs.serviceapi.dto.SuperDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonFileSavedSimpleDataDTO implements SuperDTO {
    private String hash;
    private String directory;
    private String fileName;
    private String resourceUrl;
}
