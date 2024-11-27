package com.neostudy.creditBank.services;

import com.neostudy.creditBank.dto.CreditDto;
import com.neostudy.creditBank.dto.ScoringDataDto;
import org.springframework.stereotype.Service;

@Service
public class ScoringService {

    public CreditDto scoring(ScoringDataDto scoringDataDto) {
        CreditDto creditDto = new CreditDto();

        //TODO Реализовать скоринг

        return creditDto;
    }

}
