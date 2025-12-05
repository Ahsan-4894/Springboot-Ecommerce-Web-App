package com.zepox.EcommerceWebApp.util;

import com.zepox.EcommerceWebApp.dto.response.OutOfStockItemDto;
import com.zepox.EcommerceWebApp.entity.Item;
import com.zepox.EcommerceWebApp.entity.OrderItem;
import com.zepox.EcommerceWebApp.exception.custom.OutOfStockException;
import com.zepox.EcommerceWebApp.service.OrderItemService;
import com.zepox.EcommerceWebApp.service.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InventoryUtil {
    private final EntityManager entityManager;
    private final ProductService productService;
    private final OrderItemService orderItemService;


    @Transactional
    public void checkAvaliabilityOfStocks(List<String> requestedItemIds, List<Integer> requestedItemQuantities){
        List<Item> allRequestedItems = productService.getAllProductsByIds(requestedItemIds);
        Map<String, Item> itemsMap = allRequestedItems.stream()
                .collect(Collectors.toMap(Item::getId, Function.identity()));

        List<OutOfStockItemDto> outOfStockItems = new ArrayList<>();

        for(int i=0; i<allRequestedItems.size(); i++){
            String itemId = allRequestedItems.get(i).getId();
            int requestedItemQuantity = requestedItemQuantities.get(i);
            Item item = itemsMap.get(itemId);
            if(item == null || requestedItemQuantity > item.getQuantity() - item.getReservedStock()){
                outOfStockItems.add(
                        OutOfStockItemDto.builder()
                                .itemId(itemId)
                                .available(item!=null ? item.getQuantity() : 0)
                                .requested(requestedItemQuantity)
                                .build()
                );
            }
        }
        if(!outOfStockItems.isEmpty()){
           throw new OutOfStockException(outOfStockItems.toString());
        }
    }

    @Transactional
    public void increaseReservedStockBulk(Map<String, Integer> itemsMap) {
        if (itemsMap == null || itemsMap.isEmpty()) return;

        StringBuilder queryBuilder = new StringBuilder("UPDATE items SET reserved_stock = reserved_stock + CASE id ");

        for (Map.Entry<String, Integer> entry : itemsMap.entrySet()) {
            queryBuilder.append("WHEN '")  // opening quote
                    .append(entry.getKey())  // UUID
                    .append("' THEN ")       // closing quote before THEN
                    .append(entry.getValue())
                    .append(" ");
        }

        queryBuilder.append("END WHERE id IN (")
                .append(itemsMap.keySet().stream()
                        .map(id -> "'" + id + "'")  // wrap each id in quotes
                        .collect(Collectors.joining(",")))
                .append(")");

        Query query = entityManager.createNativeQuery(queryBuilder.toString());
        query.executeUpdate();
    }

    @Transactional
    public void updateReservedStockAfterPaymentSuccessful(String sessionId, String orderId){
//        1. Get all ordered items for this session
        List<OrderItem> orderedItems = orderItemService.getOrderItemsByOrderId(orderId);

        List<Item> updatedItems = new ArrayList<>();
        for(OrderItem orderItem : orderedItems){
            Item item = orderItem.getItem();
            int requestedQuant = orderItem.getQuantity();

//            Reduce both reserved and actual stock
            item.setReservedStock(item.getReservedStock() - requestedQuant);
            item.setQuantity(item.getQuantity() - requestedQuant);

            updatedItems.add(item);
        }
        productService.bulkUpdate(updatedItems);
        System.out.println("Reserved and actual stock updated for successful payment: " + sessionId);
    }

    @Transactional
    public void updateReservedStockAfterPaymentFail(String sessionId, String orderId){
//        1. Get all ordered items for this session
        List<OrderItem> orderedItems = orderItemService.getOrderItemsByOrderId(orderId);
        List<Item> updatedItems = new ArrayList<>();
        for (OrderItem orderItem : orderedItems) {
            Item item = orderItem.getItem();
            int requestedQty = orderItem.getQuantity();

//            Just un-reserve the stock (since payment failed)
            item.setReservedStock(item.getReservedStock() - requestedQty);

            updatedItems.add(item);
        }
        productService.bulkUpdate(updatedItems);
        System.out.println("Reserved stock reverted due to failed payment: " + sessionId);
    }
}
