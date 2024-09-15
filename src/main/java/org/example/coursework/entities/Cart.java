package org.example.coursework.entities;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<OrderItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void addItem(Dish dish, int quantity) {
        for (OrderItem item : items) {
            if (item.getDish().getId() == dish.getId()) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        items.add(new OrderItem(dish, quantity));
    }

    public void removeItem(Dish dish) {
        items.removeIf(item -> item.getDish().getId() == dish.getId());
    }

    public void updateItem(Dish dish, int quantity) {
        for (OrderItem item : items) {
            if (item.getDish().getId() == dish.getId()) {
                item.setQuantity(quantity);
                return;
            }
        }
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(item -> item.getDish().getPrice() * item.getQuantity()).sum();
    }

    public int getTotalCalories() {
        return items.stream().mapToInt(item -> item.getDish().getCalories() * item.getQuantity()).sum();
    }
}
