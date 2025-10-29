package com.example.JournalApp.Service;

import com.example.JournalApp.Entity.Users;
import com.example.JournalApp.Reposetri.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class UserService {

    @Autowired
    private UserRepository UserRepo;
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Users saveNewUser(Users user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER"));
        return UserRepo.save(user);
    }

    public Users saveUser(Users user){
        return UserRepo.save(user);
    }

    public List<Users> getAll(){
        return UserRepo.findAll();
    }

    public Optional<Users> getById(ObjectId id){
        return UserRepo.findById(id);
    }

    public boolean deleteData(String username){
        UserRepo.deleteByUsername(username);
        return true;
    }

    public Users findByUser(String username){
        return UserRepo.findByUsername(username);
    }

    public Users saveNewAdmin(Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        return UserRepo.save(user);
    }
}
