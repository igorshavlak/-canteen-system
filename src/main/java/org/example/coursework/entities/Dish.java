package org.example.coursework.entities;

public class Dish {
    private int id;
    private String name;
    private byte[] image;
    private String description;
    private int calories;
    private int price;
    private int categoryId;
    public Dish(int id, String name, byte[] image, String description, int calories, int price, int categoryId) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.calories = calories;
        this.price = price;
        this.categoryId = categoryId;

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
    public int getPrice() {
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
}
