package com.test.crudUser.controller;

import com.test.crudUser.endpoints.UserEndpoints;
import com.test.crudUser.entity.User;
import com.test.crudUser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;


@Slf4j
@RepositoryRestController
@RequiredArgsConstructor
@RequestMapping("/source")
public class SourceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping(path = "")
    public ResponseEntity<?> getSource() {
        HashMap<String, String> map = new HashMap<>();
        map.put("sourceCode", "http://teste.com.br");

        return ResponseEntity.ok(map);
    }
}
