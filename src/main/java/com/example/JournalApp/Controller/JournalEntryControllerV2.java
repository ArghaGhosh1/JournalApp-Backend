package com.example.JournalApp.Controller;

import com.example.JournalApp.Entity.JournalEntry;
import com.example.JournalApp.Entity.Users;
import com.example.JournalApp.Service.JournalEntryService;
import com.example.JournalApp.Service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryControllerV2 {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;


    @GetMapping
    public ResponseEntity<?> getAllJournalEntriesOfUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.findByUser(username);
        List<JournalEntry> all = user.getJournalEntries();
        if (all != null && !all.isEmpty()) {
            return new ResponseEntity<>(all, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<JournalEntry> creatEntry(@RequestBody JournalEntry myjou) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        try {
            journalEntryService.dataEntry(myjou, username);
            return new ResponseEntity<>(myjou, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("id/{myid}")
    public ResponseEntity<JournalEntry> getById(@PathVariable ObjectId myid) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userService.findByUser(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(myid)).collect(Collectors.toList());

        if (!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.getById(myid);

            if (journalEntry.isPresent()) {
                return new ResponseEntity<>(journalEntry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("id/{myid}")
    public ResponseEntity<?> delete(@PathVariable ObjectId myid) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        boolean removed = journalEntryService.deleteById(myid, username);
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("id/{myid}")
    public ResponseEntity<JournalEntry> update(@PathVariable ObjectId myid, @RequestBody JournalEntry myjou) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            Users user = userService.findByUser(username);
            List<JournalEntry> collect = user.getJournalEntries().stream().filter(x ->x.getId().equals(myid)).collect(Collectors.toList());
            if(!collect.isEmpty()){
                JournalEntry old = journalEntryService.getById(myid).orElse(null);
                if(!old.equals("")){
                    if (old != null) {
                        old.setTitle(myjou.getTitle() != null && !myjou.getTitle().equals("") ? myjou.getTitle() : old.getTitle());
                        old.setContent(myjou.getContent() != null && !myjou.getContent().equals("") ? myjou.getContent() : old.getContent());
                    }
                    journalEntryService.dataEntry(old);
                    return new ResponseEntity<>(old, HttpStatus.OK);
                }

            }

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


    }


}
