package com.zepox.EcommerceWebApp.service;

import com.zepox.EcommerceWebApp.entity.Item;
import com.zepox.EcommerceWebApp.entity.Order;
import com.zepox.EcommerceWebApp.entity.OrderItem;
import com.zepox.EcommerceWebApp.entity.key.OrderItemDetailsId;
import com.zepox.EcommerceWebApp.exception.custom.ItemDoesNotExistException;
import com.zepox.EcommerceWebApp.repository.OrderItemRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepo orderItemRepo;
    private final ProductService productService;

    @Transactional
    public List<OrderItem> addOrderItems(Order order, List<String> itemIds, List<Integer> quantites){
        System.out.println("Under Order Item Service");
        System.out.println(itemIds);
        System.out.println(quantites);

        List<Item> items = productService.getAllProductsByIds(itemIds);
        Map<String, Item> itemMap = items.stream()
                .collect(Collectors.toMap(
                        Item::getId,
                        Function.identity()
                ));
        List<OrderItem>  orderItems = new ArrayList<>();
        for(int i=0; i<itemMap.size(); i++){
            String itemId = itemIds.get(i);
            Integer quantity = quantites.get(i);

            Item item = itemMap.get(itemId);

            if(item == null) throw new ItemDoesNotExistException("Item not found");


            OrderItemDetailsId orderItemDetailsId = new OrderItemDetailsId(order.getId(), itemId);
            orderItems.add(
                    OrderItem.builder()
                            .id(orderItemDetailsId)
                            .quantity(quantity)
                            .order(order)
                            .item(item)
                            .build()
            );
        }
        return orderItemRepo.saveAll(orderItems);
    }

    @Transactional
    public void removeOrderItems(String orderId){
        orderItemRepo.deleteOrderItemsByOrderId(orderId);
    }

    @Transactional
    public List<OrderItem> getOrderItemsByOrderId(String orderId){
        return orderItemRepo.findOrderItemsByOrderId(orderId).orElseThrow(()-> new ItemDoesNotExistException("Item not found"));
    }

    public boolean checkItemExistInTheseOrders(List<String> orderIds, String itemId){
        List<OrderItem> orderItems = orderItemRepo.findOrderItemsByOrderIdAndItemId(orderIds, itemId).orElseThrow(()-> new ItemDoesNotExistException("Items not found"));
        return !orderItems.isEmpty();
    }

}
