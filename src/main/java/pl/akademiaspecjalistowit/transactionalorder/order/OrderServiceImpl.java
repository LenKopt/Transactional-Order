package pl.akademiaspecjalistowit.transactionalorder.order;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductEntity;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductException;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductReadService;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductReadService productReadService;
    private final OrderPlacedEventListener orderPlacedEventListener;

    @Override
    @Transactional
    public void placeAnOrder(OrderDto orderDto) {
        List<ProductEntity> productEntities = orderDto.getProducts().stream().map(productReadService::getProductByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        OrderEntity orderEntity = makeAnOrderWithWarehouseStateUpdate(orderDto, productEntities);
        orderRepository.save(orderEntity);
    }

    @Override
    public void removeAnOrder(Long id) {
        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(() -> new OrderServiceException("Nie mamy takiego zamówienia"));
        orderPlacedEventListener.removeProductFromDeletedOrder(orderEntity);
        orderRepository.deleteById(id);
    }

    @Override
    public void implementAnOrder(Long id) {
        orderRepository.deleteById(id);
        orderPlacedEventListener.removeAllProductsWithzZeroQuantity();
    }

    private static void updateProduct(ProductEntity currentProduct, OrderEntity orderEntity) {
        try {
            currentProduct.applyOrder(orderEntity);
        } catch (ProductException e) {
            throw new OrderServiceException(
                    "Zamównie nie może być zrealizowane ponieważ ilosć " +
                            "pozycji w magazynie jest niewystarczająca");
        }
    }

    private OrderEntity makeAnOrderWithWarehouseStateUpdate(OrderDto orderDto, List<ProductEntity> productEntity) {
        try {
            return new OrderEntity(
                    productEntity,
                    orderDto.getQuantity());
        } catch (ProductException e) {
            throw new OrderServiceException(
                    "Zamównie nie może być zrealizowane ponieważ ilosć " +
                            "pozycji w magazynie jest niewystarczająca");
        }
    }

}
