package org.example;

public class Laptop {

    private String name;
    private long price;

    public Laptop(String name, long price) {
        this.name = name;
        this.price = price;
    }

    public Laptop(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
