package com.shaliya.springmultitenant.springmultitenant.controller;

import com.shaliya.springmultitenant.springmultitenant.config.CurrentTenantIdentifierResolverImpl;
import com.shaliya.springmultitenant.springmultitenant.config.MultiTenantConnectionProvider;
import com.shaliya.springmultitenant.springmultitenant.dto.CommonResponseDTO;
import com.shaliya.springmultitenant.springmultitenant.dto.requestdto.RequestProductDTO;
import com.shaliya.springmultitenant.springmultitenant.dto.responsedto.ResponseProductDTO;
import com.shaliya.springmultitenant.springmultitenant.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/add")
    public CommonResponseDTO addProduct(
            @RequestBody RequestProductDTO product,
            @RequestHeader("X-Tenant-ID") String tenantId
    ) {
        CurrentTenantIdentifierResolverImpl.setCurrentTenant(tenantId);
        try {
            return productService.addProduct(product);
        } finally {
            CurrentTenantIdentifierResolverImpl.clearCurrentTenant();
        }
    }

    @GetMapping("/list")
    public List<ResponseProductDTO> getProducts(@RequestHeader("X-Tenant-ID") String tenantId) {
        CurrentTenantIdentifierResolverImpl.setCurrentTenant(tenantId);
        try {
            return productService.getAllProducts();
        } finally {
            CurrentTenantIdentifierResolverImpl.clearCurrentTenant();
        }
    }
}
