package pl.akademiaspecjalistowit.transactionalorder.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.akademiaspecjalistowit.transactionalorder.product.ProductEntity;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //private String productName;

    private Integer quantity;
    @ManyToMany
    @JoinTable(name = "products_in_orders", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "id"))
    private List<ProductEntity> productEntityList;

    public OrderEntity(List<ProductEntity> productEntityList, Integer quantity) {
        validate(quantity);
        this.productEntityList = productEntityList;
        this.quantity = quantity;
        productEntityList.forEach(e -> e.applyOrder(this));
    }


    private void validate(Integer quantity) {
        if (quantity <= 0) {
            throw new OrderException("Zamówienie pownno zawierać nie ujemną ilość pozycji");
        }
    }
}
