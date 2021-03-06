package com.test.crudUser.endpoints;

import org.springframework.stereotype.Component;

@Component
public class UserEndpoints {
    public static final String USERS = "/users";
    public static final String USER = "/users/{id}";
    public static final String CREATE_USER = "/users";
    public static final String UPDATE_USER = "/users/{id}";
    public static final String DELETE_USER = "/users/{id}";
}
