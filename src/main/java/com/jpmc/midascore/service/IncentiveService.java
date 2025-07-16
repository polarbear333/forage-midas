package com.jpmc.midascore.service;

import com.jpmc.midascore.foundation.Incentive;
import com.jpmc.midascore.foundation.Transaction;


public interface IncentiveService {
    Incentive getIncentive(Transaction transaction);
}
