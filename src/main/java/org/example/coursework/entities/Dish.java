package org.example.coursework.entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;

public class Dish {
    private int id;
    private String name;
    private byte[] image;
    private String description;
    private int calories;
    private double price;
    private int categoryId;
    private transient ImageView imageView;
    public Dish(int id, String name, byte[] image, String description, int calories, double price, int categoryId) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.calories = calories;
        this.price = price;
        this.categoryId = categoryId;

    }
    public Dish(String name, byte[] image, String description, int calories, double price, int categoryId) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.calories = calories;
        this.price = price;
        this.categoryId = categoryId;

    }
    public Dish(String name, String description, int calories, double price, int categoryId) {
        this.name = name;
        this.description = description;
        this.calories = calories;
        this.price = price;
        this.categoryId = categoryId;

    }
    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getCalories() {
        return calories;
    }
    public void setCalories(int calories) {
        this.calories = calories;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public void setImageViewFromBytes() {
        if (image != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(image);
            Image fxImage = new Image(bis);
            this.imageView = new ImageView(fxImage);
            this.imageView.setFitHeight(100); // Задайте нужные размеры
            this.imageView.setFitWidth(100);
        }
    }

}
