package pl.akademiaspecjalistowit.transactionalorder.order;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("addOrder")
    @ResponseStatus(HttpStatus.CREATED)
    public void placeAnOrder(@RequestBody OrderDto orderDto) {
        orderService.placeAnOrder(orderDto);
    }
    @DeleteMapping("{id}")
    public String removeAnOrder(@PathVariable("id")Long id){
        orderService.removeAnOrder(id);
        return "Deletion was successful";
    }
    @PostMapping("{id}")
    public String implementationAnOrder(@PathVariable("id")Long id){
        orderService.implementAnOrder(id);
        return "Order was implemented";
    }
}
