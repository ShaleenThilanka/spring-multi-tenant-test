package com.shaliya.springmultitenant.springmultitenant.service.impl;

import com.shaliya.springmultitenant.springmultitenant.dto.CommonResponseDTO;
import com.shaliya.springmultitenant.springmultitenant.dto.requestdto.RequestProductDTO;
import com.shaliya.springmultitenant.springmultitenant.dto.responsedto.ResponseProductDTO;
import com.shaliya.springmultitenant.springmultitenant.entity.Product;
import com.shaliya.springmultitenant.springmultitenant.repo.ProductRepo;
import com.shaliya.springmultitenant.springmultitenant.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;

    public ProductServiceImpl(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }


    @Override
    public CommonResponseDTO addProduct(RequestProductDTO dto) {
        Product product = Product.builder()
                .id(UUID.randomUUID().toString())
                .price(dto.getPrice())
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .build();
        productRepo.save(product);
        return new CommonResponseDTO(
                201, "Registration was successful!", product.getName(), new ArrayList<>()
        );
    }

    @Override
    public List<ResponseProductDTO> getAllProducts() {
        List<Product> products = productRepo.findAll();
        List<ResponseProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            productDTOs.add(new ResponseProductDTO(
                    product.getId(), product.getName(), product.getPrice(), product.getQuantity()
            ));
        }
        return productDTOs;
    }
}
