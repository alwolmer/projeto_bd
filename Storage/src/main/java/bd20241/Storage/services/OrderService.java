package bd20241.Storage.services;

import java.util.List;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Order;
import bd20241.Storage.repositories.OrderRepository;
import bd20241.Storage.utils.NanoId;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void deleteOrder(String id) {
        orderRepository.deleteById(id);
    }

    public Order updateOrder(Order order) {
        orderRepository.updateOrder(order);
        return order;
    }

    public Order getOrderById(String id) {
        return orderRepository.findById(id);
    }

    public Order createOrder(Order order) {
        order.setId(NanoId.randomNanoIdForStorage());
        orderRepository.save(order);
        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
