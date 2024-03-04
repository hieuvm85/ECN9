package com.example.Ecommerce_BE.payload.response;

import java.time.LocalDateTime;

import com.example.Ecommerce_BE.model.entity.Cart;

public class CartResponse {
    private int id;
    private LocalDateTime dateTimeCreated;
    private int quantity;
    private Product product;

    // Constructor
    public CartResponse(int id, LocalDateTime dateTimeCreated, int quantity, Product product) {
        this.id = id;
        this.dateTimeCreated = dateTimeCreated;
        this.quantity = quantity;
        this.product = product;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(LocalDateTime dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    // Product class
    public static class Product {
        private int id;
        private String title;
        private double originalPrice;
        private double sellingPrice;
        private double weight;
        private double rate;
        private String linkImages;
        private Shop shop;

        // Constructor
        public Product(int id, String title, double originalPrice, double sellingPrice, double weight, double rate, String linkImages, Shop shop) {
            this.id = id;
            this.title = title;
            this.originalPrice = originalPrice;
            this.sellingPrice = sellingPrice;
            this.weight = weight;
            this.rate = rate;
            this.linkImages = linkImages;
            this.shop = shop;
        }

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public double getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(double originalPrice) {
            this.originalPrice = originalPrice;
        }

        public double getSellingPrice() {
            return sellingPrice;
        }

        public void setSellingPrice(double sellingPrice) {
            this.sellingPrice = sellingPrice;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public double getRate() {
            return rate;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public String getLinkImages() {
            return linkImages;
        }

        public void setLinkImages(String linkImages) {
            this.linkImages = linkImages;
        }

        public Shop getShop() {
            return shop;
        }

        public void setShop(Shop shop) {
            this.shop = shop;
        }
    }

    // Shop class
    public static class Shop {
        private int id;
        private String nameShop;
        private String linkImageAvatarShop;

        // Constructor
        public Shop(int id, String nameShop, String linkImageAvatarShop) {
            this.id = id;
            this.nameShop = nameShop;
            this.linkImageAvatarShop = linkImageAvatarShop;
        }

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNameShop() {
            return nameShop;
        }

        public void setNameShop(String nameShop) {
            this.nameShop = nameShop;
        }

        public String getLinkImageAvatarShop() {
            return linkImageAvatarShop;
        }

        public void setLinkImageAvatarShop(String linkImageAvatarShop) {
            this.linkImageAvatarShop = linkImageAvatarShop;
        }
    }
    public CartResponse(Cart cart) {
        this.id = cart.getId();
        this.dateTimeCreated = LocalDateTime.now(); // Assuming it's created now
        this.quantity = cart.getQuantity();
        this.product = new Product(
            cart.getProduct().getId(),
            cart.getProduct().getTitle(),
            cart.getProduct().getOriginalPrice(),
            cart.getProduct().getSellingPrice(),
            cart.getProduct().getWeight(),
            cart.getProduct().getRate(),
            cart.getProduct().getLinkImages().get(0),
            new Shop(
                cart.getProduct().getShop().getId(),
                cart.getProduct().getShop().getNameShop(),
                cart.getProduct().getShop().getLinkImageAvatarShop()
            )
        );
    }
}
