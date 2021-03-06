package com.test.crudUser.controller;

import com.test.crudUser.dto.UserDTO;
import com.test.crudUser.endpoints.UserEndpoints;
import com.test.crudUser.entity.User;
import com.test.crudUser.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RepositoryRestController
@RequestMapping("/softplan/")
@RequiredArgsConstructor
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping(path = UserEndpoints.USERS)
    public ResponseEntity<?> getUsers(){
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping(path = UserEndpoints.USER)
    public ResponseEntity<?> getUser(@PathVariable("id") String userId){
        try{
            LOGGER.info("UserController::: " + userId);
            User user = userService.getUser(userId);

            return ResponseEntity.ok(user);
        }catch (RuntimeException e){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @PostMapping(path = UserEndpoints.CREATE_USER)
    public ResponseEntity<?> createUser(@RequestBody UserDTO u){
        LOGGER.info("UserController::: " + u);

        User user = userService.insertUser(u);

        return ResponseEntity.ok(user);
    }

    @PutMapping(path = UserEndpoints.UPDATE_USER)
    public ResponseEntity<?> updateUser(@PathVariable("id") String userId, @RequestBody UserDTO u){
        try{
            LOGGER.info("UserController::: " + u);
            u.setId(userId);

            User user = userService.updateUser(u);

            return ResponseEntity.accepted().body(user);
        }catch (RuntimeException e){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }

    @DeleteMapping(path = UserEndpoints.DELETE_USER)
    public ResponseEntity<?> deleteUser(@PathVariable("id") String userId){
        try{
            LOGGER.info("UserController::: " + userId);
            String result = userService.deleteUser(userId);

            return ResponseEntity.ok(result);
        }catch (RuntimeException e){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
        }
    }
}
