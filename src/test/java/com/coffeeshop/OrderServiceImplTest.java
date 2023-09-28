package com.coffeeshop;

import com.coffeeshop.error.EventNotApplicableException;
import com.coffeeshop.error.OrderNotFoundException;
import com.coffeeshop.model.event.EventType;
import com.coffeeshop.model.event.OrderEvent;
import com.coffeeshop.model.event.OrderRegisteredEvent;
import com.coffeeshop.model.order.Order;
import com.coffeeshop.repository.OrderEventRepository;
import com.coffeeshop.repository.OrderRepository;
import com.coffeeshop.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderEventRepository eventRepository;

    @Test
    public void testPublishEventWithoutOrderRegistration() {
        OrderEvent event = mock(OrderEvent.class);
        when(event.isApplicable(any())).thenReturn(false);

        assertThrows(EventNotApplicableException.class, () -> orderService.publishEvent(event));
    }

    @Test
    public void testPublishEventForCompletedOrder() {
        Order order = new Order();
        order.setStatus(EventType.DELIVERED);
        OrderEvent event = mock(OrderEvent.class);
        when(event.getOrder()).thenReturn(order);
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        assertThrows(EventNotApplicableException.class, () -> orderService.publishEvent(event));
    }

    @Test
    public void testPublishEventForCancelledOrder() {
        Order order = new Order();
        order.setStatus(EventType.CANCELLED);
        OrderEvent event = mock(OrderEvent.class);

        when(event.getOrder()).thenReturn(order);
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        assertThrows(EventNotApplicableException.class, () -> {
            orderService.publishEvent(event);
        });
    }

    @Test
    public void testEventForDeliveredOrder() {
        Order order = new Order();
        order.setId(1);
        order.setStatus(EventType.DELIVERED);

        OrderEvent event = new OrderRegisteredEvent();
        event.setEventType(EventType.REGISTERED);
        event.setOrder(order);

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        assertThrows(EventNotApplicableException.class, () -> orderService.publishEvent(event));
    }

    @Test
    public void testEventForCancelledOrder() {
        Order order = new Order();
        order.setId(1);
        order.setStatus(EventType.CANCELLED);

        OrderEvent event = new OrderRegisteredEvent();
        event.setEventType(EventType.REGISTERED);
        event.setOrder(order);

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));

        assertThrows(EventNotApplicableException.class, () -> orderService.publishEvent(event));
    }


    @Test
    public void testSuccessfulEventAddition() {
        Order order = new Order();
        order.setId(1);

        OrderEvent event = new OrderRegisteredEvent();
        event.setEventType(order.getStatus());
        event.setOrder(order);

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        when(orderRepository.save(any())).thenReturn(order);

        when(eventRepository.save(any())).thenReturn(event);

        assertDoesNotThrow(() -> orderService.publishEvent(event));
    }


    @Test
    public void testFindNonExistentOrder() {
        when(orderRepository.findByIdWithEvents(anyInt())).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> orderService.findOrder(1));
    }

    @Test
    public void testFindOrderSuccessfully() {
        Order order = new Order();
        order.setId(1);
        order.setStatus(EventType.REGISTERED);

        when(orderRepository.findByIdWithEvents(anyInt())).thenReturn(Optional.of(order));

        Order retrievedOrder = orderService.findOrder(1);
        assertNotNull(retrievedOrder);
        assertEquals(EventType.REGISTERED, retrievedOrder.getStatus());
    }
}

