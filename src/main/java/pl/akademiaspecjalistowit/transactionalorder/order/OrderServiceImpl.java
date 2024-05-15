package pl.akademiaspecjalistowit.transactionalorder.order;

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

        OrderEntity orderEntity = productReadService
                .getProductByName(orderDto.getProductName()).map(productEntity -> {
                    OrderEntity orderEntityToReturn = new OrderEntity(productEntity, orderDto.getQuantity());
                    updateProduct(productEntity, orderEntityToReturn);
                    orderRepository.save(orderEntityToReturn);
                    orderPlacedEventListener.notifyOrderPlaced(orderEntityToReturn);
                    return orderEntityToReturn;
                })
                .orElseThrow(() -> new OrderServiceException("Zamównie nie moze być realizowane, ponieważ " +
                        "zawiera pozycje niedostępną w magazynie"));
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

}
