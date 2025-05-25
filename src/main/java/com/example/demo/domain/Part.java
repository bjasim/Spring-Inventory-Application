package com.example.demo.domain;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="part_type", discriminatorType = DiscriminatorType.INTEGER)
@Table(name="Parts")
public abstract class Part implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    @Min(value = 0, message = "Minimum inventory must be 0 or more")
    @Column(nullable = false)
    private int minInv;
    @Min(value = 1, message = "Maximum inventory must be greater than 0")
    @Column(nullable = false)
    private int maxInv;
    @Min(value = 0, message = "Price must be positive")
    private double price;

    @Min(value = 0, message = "Inventory must be positive")
    private int inv;

    @ManyToMany
    @JoinTable(name = "product_part", joinColumns = @JoinColumn(name = "part_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private Set<Product> products = new HashSet<>();

    public Part() {}

    public Part(String name, double price, int inv, int minInv, int maxInv) {
        this.name = name;
        this.price = price;
        this.inv = inv;
        this.minInv = minInv;
        this.maxInv = maxInv;
    }

    public boolean isInventoryValid() {
        return minInv <= inv && inv <= maxInv;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public int getInv() { return inv; }

    public void setInv(int inv) {
        if (inv < minInv) {
            System.out.println("Warning: Inventory cannot be below " + minInv);
            return; // Prevents setting invalid inventory instead of crashing
        }
        this.inv = inv;
    }

    public int getMinInv() { return minInv; }

    public void setMinInv(int minInv) { this.minInv = minInv; }

    public int getMaxInv() { return maxInv; }

    public void setMaxInv(int maxInv) { this.maxInv = maxInv; }

    public Set<Product> getProducts() { return products; }

    public void setProducts(Set<Product> products) { this.products = products; }

    @Override
    public String toString() { return this.name; }
}