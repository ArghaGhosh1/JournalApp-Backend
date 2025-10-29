package com.example.JournalApp.Reposetri;

import com.example.JournalApp.Entity.Users;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<Users, ObjectId> {

    Users findByUsername(String username);

    Users deleteByUsername(String username);
}
