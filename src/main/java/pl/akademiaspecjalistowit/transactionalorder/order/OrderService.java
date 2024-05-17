package pl.akademiaspecjalistowit.transactionalorder.order;

public interface OrderService {

    void placeAnOrder(OrderDto orderDto);

    void removeAnOrder(Long id);

    void implementAnOrder(Long id);
}
