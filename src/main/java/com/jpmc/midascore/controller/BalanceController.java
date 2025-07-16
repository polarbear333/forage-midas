package com.jpmc.midascore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class BalanceController {
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/balance")
    public ResponseEntity<Balance> getBalance(@RequestParam Long userId) {
        UserRecord user = userRepository.findById(userId.longValue());
        if(user != null){
            return ResponseEntity.ok(new Balance(user.getBalance()));
        }else{
            return ResponseEntity.ok(new Balance(0.0f));
        }
    }
    
}
