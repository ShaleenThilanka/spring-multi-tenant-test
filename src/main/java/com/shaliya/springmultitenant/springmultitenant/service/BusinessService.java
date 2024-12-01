package com.shaliya.springmultitenant.springmultitenant.service;

import com.shaliya.springmultitenant.springmultitenant.entity.User;

import java.io.IOException;

public interface BusinessService {
    String createBusiness(String businessName, User user) throws IOException;
}
