package com.github.tddiaz.ekarmachineproblem.services;

import com.github.tddiaz.ekarmachineproblem.models.CounterUpdateLog;
import com.github.tddiaz.ekarmachineproblem.models.Producer;
import com.github.tddiaz.ekarmachineproblem.repositories.CounterUpdateLogRepository;
import com.github.tddiaz.ekarmachineproblem.repositories.ProducerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProducerTaskHandlerTest {

    @Mock
    private Counter counter;

    @Mock
    private ProducerRepository producerRepository;

    @Mock
    private CounterUpdateLogRepository counterUpdateLogRepository;

    @InjectMocks
    private ProducerTaskHandler producerTaskHandler;

    @Test
    void givenProducer_whenHandleAndCounterReachedMaxValue_shouldNotProcessProducer() {
        // given
        when(counter.isValueReachedMax()).thenReturn(true);
        var producer = Producer.createMultiple("123", 1).get(0);

        // when
        producerTaskHandler.handle(producer);

        // then
        verify(counter).lock();
        verify(counter).unlock();
        verify(producerRepository).save(eq(producer));
    }

    @Test
    void givenProducer_whenHandleAndCounterNotReachedMaxValue_shouldIncreaseCounter() {
        // given
        when(counter.isValueReachedMax()).thenReturn(false);
        when(counter.getValue()).thenReturn(1);
        var producer = Producer.createMultiple("123", 1).get(0);

        // when
        producerTaskHandler.handle(producer);

        // then
        verify(counter).increment();
        verify(counter).lock();
        verify(counter).unlock();

        verify(producerRepository).save(eq(producer));

        assertThat(producer.isExecuted()).isTrue();
        assertThat(producer.getUpdatedCounterValue()).isEqualTo(1);
    }

    @Test
    void givenProducer_whenIncrementingCounterValueReachedMax_shouldSaveCounterLog() {
        // given
        when(counter.isValueReachedMax()).thenReturn(false, true);
        when(counter.getValue()).thenReturn(1);
        var producer = Producer.createMultiple("123", 1).get(0);

        // when
        producerTaskHandler.handle(producer);

        // then
        verify(counter).lock();
        verify(counter).unlock();

        var argumentCaptor = ArgumentCaptor.forClass(CounterUpdateLog.class);
        verify(counterUpdateLogRepository).save(argumentCaptor.capture());

        var counterUpdateLog = argumentCaptor.getValue();
        assertThat(counterUpdateLog.getTaskId()).isEqualTo(producer.getId());
        assertThat(counterUpdateLog.getStatus()).isEqualTo(CounterUpdateLog.Status.REACHED_MAX);
    }
}