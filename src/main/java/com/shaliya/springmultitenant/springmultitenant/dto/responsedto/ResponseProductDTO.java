package com.shaliya.springmultitenant.springmultitenant.dto.responsedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseProductDTO {
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
