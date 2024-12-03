package com.neostudy.creditBank.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
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