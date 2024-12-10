package com.neostudy.calculator.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PrescoringServiceTest {

    @Mock
    private ScoringService scoringService;

    @InjectMocks
    private PrescoringService prescoringService;
    @Test
    void prescoring() {
    }
}