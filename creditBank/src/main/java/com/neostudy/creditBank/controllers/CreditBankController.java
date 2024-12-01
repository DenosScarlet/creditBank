package com.neostudy.creditBank.controllers;

import com.neostudy.creditBank.dto.LoanStatementRequestDto;
import com.neostudy.creditBank.dto.ScoringDataDto;
import com.neostudy.creditBank.services.PrescoringService;
import com.neostudy.creditBank.services.ScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/calculator")
public class CreditBankController {
    private final PrescoringService prescoringService;
    private final ScoringService scoringService;

    @Autowired
    public CreditBankController(PrescoringService prescoringService, ScoringService scoringService) {
        this.prescoringService = prescoringService;
        this.scoringService = scoringService;
    }
    /*@Autowired
    public CreditBankController(ScoringService scoringService) {
        this.scoringService = scoringService;
    }*/


    @PostMapping("/offers")
    public ResponseEntity<String> offers(@RequestBody LoanStatementRequestDto loanStatement) throws IOException {
        //LoanOfferDto LoanOfferDto = null;
        //List<LoanOfferDto> loanOffer;
        return ResponseEntity.ok(prescoringService.prescoring(loanStatement).toString());
    }

    @PostMapping("/calc")
    public ResponseEntity<String> calc(@RequestBody ScoringDataDto scoringData) {
        return ResponseEntity.ok(scoringService.scoring(scoringData).toString());
    }

}
