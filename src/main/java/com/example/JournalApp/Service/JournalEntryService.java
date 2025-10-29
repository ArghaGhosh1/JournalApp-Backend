package com.example.JournalApp.Service;

import com.example.JournalApp.Entity.JournalEntry;
import com.example.JournalApp.Entity.Users;
import com.example.JournalApp.Reposetri.JornalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class JournalEntryService {

    @Autowired
    private JornalEntryRepository journalEntryRepo;

    @Autowired
    private UserService userService;

    @Transactional
    public JournalEntry dataEntry(JournalEntry journalEntry, String username) {

        Users user = userService.findByUser(username);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry saved = journalEntryRepo.save(journalEntry);
        user.getJournalEntries().add(saved);
        userService.saveUser(user);

        return journalEntryRepo.save(journalEntry);
    }

    public JournalEntry dataEntry(JournalEntry journalEntry) {

        return journalEntryRepo.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> getById(ObjectId id) {
        return journalEntryRepo.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String username) {

        boolean removed = false;
        try {
            Users user = userService.findByUser(username);
            removed = user.getJournalEntries().removeIf(x -> x.getId().equals(id));
            if(removed){
                userService.saveUser(user);
                journalEntryRepo.deleteById(id);
            }
        }
        catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("an error oucored while deleting the entry.",e);
        }
        return removed;
    }
}
