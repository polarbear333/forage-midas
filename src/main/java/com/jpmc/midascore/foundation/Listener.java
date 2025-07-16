package com.jpmc.midascore.foundation;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class Listener {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRecordRepository transactionRecordRepository;


    private int transactionCount = 0;

    @KafkaListener(topics = "${general.kafka-topic}", groupId = "midas-core-group")
    public void listen(Transaction transaction) {
        // Simply acknowledge the message
        transactionCount++;
        System.out.println("Received transaction #" + transactionCount + ": " + transaction);
        System.out.println("Amount: " + transaction.getAmount());

        // Step 1: Lookup sender and recipient
        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        // Step 2: Validate transaction
        if (sender != null && recipient != null && sender.getBalance() >= transaction.getAmount()) {
            // Step 3: Adjust balances
            sender.setBalance(sender.getBalance() - transaction.getAmount());
            recipient.setBalance(recipient.getBalance() + transaction.getAmount());

            // Step 4: Persist transaction record
            TransactionRecord record = new TransactionRecord();
            record.setSender(sender);
            record.setRecipient(recipient);
            record.setAmount(transaction.getAmount());

            transactionRecordRepository.save(record);
            userRepository.save(sender);
            userRepository.save(recipient);

            System.out.println("Transaction recorded successfully.");
        } else {
            System.out.println("Invalid transaction. Skipped.");
        }
    }
}

