package com.tsg.employeeapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;

import com.tsg.employeeapi.domain.Sequence;

@ExtendWith(MockitoExtension.class)
public class SequenceGeneratorTest {

    @Mock
    private MongoOperations mongoOperations;

    @InjectMocks
    private SequenceGenerator sequenceGenerator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateNextId() {
        // Arrange
        String expectedId = "1";
        Sequence seq = new Sequence();
        seq.setSequence(expectedId);

        when(mongoOperations.findAndModify(
                any(),
                any(Update.class),
                any(FindAndModifyOptions.class),
                eq(Sequence.class)
        )).thenReturn(seq);

        // Act
        String actualId = sequenceGenerator.generateNextId();

        // Assert
        assertEquals(expectedId, actualId);
        verify(mongoOperations).findAndModify(
                any(), any(Update.class), any(FindAndModifyOptions.class), eq(Sequence.class));
    }

}
