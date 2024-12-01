package com.shaliya.springmultitenant.springmultitenant.service;

import com.shaliya.springmultitenant.springmultitenant.dto.CommonResponseDTO;
import com.shaliya.springmultitenant.springmultitenant.dto.requestdto.RequestProductDTO;
import com.shaliya.springmultitenant.springmultitenant.dto.responsedto.ResponseProductDTO;

import java.util.List;

public interface ProductService {
    CommonResponseDTO addProduct(RequestProductDTO product);

    List<ResponseProductDTO> getAllProducts();
}
