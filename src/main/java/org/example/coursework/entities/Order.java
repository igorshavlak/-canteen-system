package org.example.coursework.entities;

import javafx.beans.property.*;

import java.util.Date;

public class Order {
    private IntegerProperty orderId;
    private ObjectProperty<Date> date;
    private DoubleProperty totalPrice;
    private BooleanProperty status;

    public Order(int orderId, Date date, double totalPrice, boolean status) {
        this.orderId = new SimpleIntegerProperty(orderId);
        this.date = new SimpleObjectProperty<>(date);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
        this.status = new SimpleBooleanProperty(status);
    }

    // Геттеры и сеттеры
    public int getOrderId() {
        return orderId.get();
    }

    public IntegerProperty orderIdProperty() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId.set(orderId);
    }

    public Date getDate() {
        return date.get();
    }

    public ObjectProperty<Date> dateProperty() {
        return date;
    }

    public void setDate(Date date) {
        this.date.set(date);
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public boolean isStatus() {
        return status.get();
    }

    public BooleanProperty statusProperty() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status.set(status);
    }
}
