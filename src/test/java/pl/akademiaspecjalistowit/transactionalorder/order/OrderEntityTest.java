//package pl.akademiaspecjalistowit.transactionalorder.order;
//
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.function.Executable;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;
//import pl.akademiaspecjalistowit.transactionalorder.product.ProductEntity;
//
//class OrderEntityTest {
//
//    @Test
//    void should_create_order_for_valid_quantity() {
//        //given
//        int validQuantity = 10;
//        ProductEntity pizzaProduct = new ProductEntity("pizza", 10);
//
//        //when
//        OrderEntity pizzaOrder = new OrderEntity(pizzaProduct, validQuantity);
//
//        //then
//        Assertions.assertThat(pizzaOrder).isNotNull();
//    }
//
//    @ParameterizedTest
//    @ValueSource(ints = {-1, 0})
//    void should_not_create_order_for_invalid_quantity(int invalidQuantity) {
//        //given
//        ProductEntity pizzaProduct = new ProductEntity("pizza", 10);
//        //when
//        Executable e = () -> new OrderEntity(pizzaProduct, invalidQuantity);
//
//        //then
//        assertThrows(OrderException.class, e);
//    }
//}