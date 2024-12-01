package com.shaliya.springmultitenant.springmultitenant.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class RequestProductDTO {
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
