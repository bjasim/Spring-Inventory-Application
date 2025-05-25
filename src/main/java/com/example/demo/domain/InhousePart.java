package com.example.demo.domain;

import javax.persistence.Entity;

@Entity
public class InhousePart extends Part {
    private int partId;

    public InhousePart() {
        super(); // Call parent constructor
    }

    public InhousePart(String name, double price, int inv, int minInv, int maxInv, int partId) {
        super(name, price, inv, minInv, maxInv);
        this.partId = partId;
    }

    public int getPartId() { return partId; }
    public void setPartId(int partId) { this.partId = partId; }
}
