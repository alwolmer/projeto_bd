package bd20241.Storage.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import bd20241.Storage.models.Order;
import bd20241.Storage.models.OrderedItem;
import bd20241.Storage.payloads.requests.CreateOrderRequest;
import bd20241.Storage.repositories.OrderRepository;
import bd20241.Storage.repositories.OrderedItemRepository;
import bd20241.Storage.utils.NanoId;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderedItemRepository orderedItemRepository;

    public OrderService(OrderRepository orderRepository, OrderedItemRepository orderedItemRepository) {
        this.orderRepository = orderRepository;
        this.orderedItemRepository = orderedItemRepository;
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

    public Order createOrder(CreateOrderRequest order) {
        Order newOrder = new Order();
        newOrder.setId(NanoId.randomNanoIdForStorage());
        newOrder.setClientId(order.getClientId());
        newOrder.setEmployeeCpf(order.getEmployeeCpf());
        newOrder.setCarrierCnpj(order.getCarrierCnpj());
        newOrder.setTrackingCode(UUID.randomUUID().toString());
        newOrder.setDeliveryAddressId(order.getDeliveryAddressId());
        orderRepository.save(newOrder);
        
        for (String itemId : order.getItemIds()) {
            OrderedItem orderedItem = new OrderedItem();
            orderedItem.setItemId(itemId);
            orderedItem.setOrderId(newOrder.getId());
            orderedItemRepository.save(orderedItem);
        }

        return newOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
