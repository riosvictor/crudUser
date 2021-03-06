package com.test.crudUser.service;

import com.test.crudUser.dto.UserDTO;
import com.test.crudUser.entity.User;
import com.test.crudUser.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public UserService() {}

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(String userId){
        Optional<User> user = userRepository.findById(userId);
        return user.get();
    }

    public User insertUser(UserDTO user){
        ModelMapper mp = new ModelMapper();
        User u = mp.map(user, User.class);
        u.onCreate();
        return userRepository.insert(u);
    }

    public User updateUser(UserDTO user){
        User oldUser = userRepository.findById(user.getId()).get();

        ModelMapper mp = new ModelMapper();
        User u = mp.map(user, User.class);
        u.onUpdate();
        u.setCreated(oldUser.getCreated());
        return userRepository.save(u);
    }

    public String deleteUser(String userId){
        userRepository.deleteById(userId);
        return userId;
    }
}
