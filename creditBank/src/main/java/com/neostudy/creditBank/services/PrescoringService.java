package com.neostudy.creditBank.services;

import com.neostudy.creditBank.dto.LoanOfferDto;
import com.neostudy.creditBank.dto.LoanStatementRequestDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrescoringService {
    public List<LoanOfferDto> prescoring(LoanStatementRequestDto loanStatementRequestDto) {
        List<LoanOfferDto> loanOfferDtos = new ArrayList<>();
        LoanOfferDto loanOfferDto1 = new LoanOfferDto();
        LoanOfferDto loanOfferDto2 = new LoanOfferDto();
        LoanOfferDto loanOfferDto3 = new LoanOfferDto();
        LoanOfferDto loanOfferDto4 = new LoanOfferDto();
        //TODO Реализовать прескоринг

        loanOfferDtos.add(loanOfferDto1);
        loanOfferDtos.add(loanOfferDto2);
        loanOfferDtos.add(loanOfferDto3);
        loanOfferDtos.add(loanOfferDto4);
        return loanOfferDtos;
    }
}
