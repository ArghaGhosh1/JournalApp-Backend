package com.example.JournalApp.Controller;

import com.example.JournalApp.Entity.Users;
import com.example.JournalApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PutMapping()
    public ResponseEntity<?> updateUserDetail( @RequestBody Users user) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users old = userService.findByUser(username);
        if (old != null) {
            old.setUsername(user.getUsername() != null && !user.getUsername().equals("") ? user.getUsername() : old.getUsername());
            old.setPassword(user.getPassword() != null && !user.getPassword().equals("") ? user.getPassword() : old.getPassword());
            userService.saveNewUser(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getById(@PathVariable String username){
        Users user = userService.findByUser(username);
        if (user != null && !user.equals("")){
            return new ResponseEntity<>(user,HttpStatus.FOUND);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping()
    public ResponseEntity<?> delete(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        userService.deleteData(username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
