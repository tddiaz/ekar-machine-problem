package com.github.tddiaz.ekarmachineproblem.services;

import com.github.tddiaz.ekarmachineproblem.models.Consumer;
import com.github.tddiaz.ekarmachineproblem.models.CounterUpdateLog;
import com.github.tddiaz.ekarmachineproblem.repositories.ConsumerRepository;
import com.github.tddiaz.ekarmachineproblem.repositories.CounterUpdateLogRepository;
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
class ConsumerTaskHandlerTest {

    @Mock
    private Counter counter;

    @Mock
    private ConsumerRepository consumerRepository;

    @Mock
    private CounterUpdateLogRepository counterUpdateLogRepository;

    @InjectMocks
    private ConsumerTaskHandler consumerTaskHandler;

    @Test
    void givenConsumer_whenHandleAndCounterReachedMinValue_shouldNotProcessConsumer() {
        // given
        when(counter.isValueReachedMin()).thenReturn(true);
        var consumer = Consumer.createMultiple("123", 1).get(0);

        // when
        consumerTaskHandler.handle(consumer);

        // then
        verify(counter).lock();
        verify(counter).unlock();
        verify(consumerRepository).save(eq(consumer));
    }

    @Test
    void givenConsumer_whenHandleAndCounterNotReachedMinValue_shouldDecreaseCounter() {
        // given
        when(counter.isValueReachedMin()).thenReturn(false);
        when(counter.getValue()).thenReturn(1);
        var consumer = Consumer.createMultiple("123", 1).get(0);

        // when
        consumerTaskHandler.handle(consumer);

        // then
        verify(counter).decrement();
        verify(counter).lock();
        verify(counter).unlock();

        verify(consumerRepository).save(eq(consumer));

        assertThat(consumer.isExecuted()).isTrue();
        assertThat(consumer.getUpdatedCounterValue()).isEqualTo(1);
    }

    @Test
    void givenConsumer_whenDecrementingCounterValueReachedMin_shouldSaveCounterLog() {
        // given
        when(counter.isValueReachedMin()).thenReturn(false, true);
        when(counter.getValue()).thenReturn(1);
        var consumer = Consumer.createMultiple("123", 1).get(0);

        // when
        consumerTaskHandler.handle(consumer);

        // then
        verify(counter).lock();
        verify(counter).unlock();

        var argumentCaptor = ArgumentCaptor.forClass(CounterUpdateLog.class);
        verify(counterUpdateLogRepository).save(argumentCaptor.capture());

        var counterUpdateLog = argumentCaptor.getValue();
        assertThat(counterUpdateLog.getTaskId()).isEqualTo(consumer.getId());
        assertThat(counterUpdateLog.getStatus()).isEqualTo(CounterUpdateLog.Status.REACHED_MIN);
    }
}