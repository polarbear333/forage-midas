package com.jpmc.midascore.service.impl;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.IncentiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveServiceImpl implements IncentiveService{
    
    @Autowired
    private RestTemplate restTemplate;

    private final String incentiveApiUrl = "http://localhost:8080/incentive";

    @Override
    public Incentive getIncentive(Transaction transaction){
        try{
            return restTemplate.postForObject(incentiveApiUrl, transaction, Incentive.class);

        }catch(Exception e){
            System.out.println("Failed to get incentive" + e.getMessage());
            //returns 0 incentive if API call fails
            return new Incentive(0.0f);
        }
    }

}
