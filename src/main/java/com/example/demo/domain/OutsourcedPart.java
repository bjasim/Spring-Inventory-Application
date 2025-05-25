package com.example.demo.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("2") // Ensures correct inheritance mapping
public class OutsourcedPart extends Part {

    @Column(name = "company_name")
    private String companyName;

    public OutsourcedPart() {
        super();
    }

    public OutsourcedPart(String name, double price, int inv, int minInv, int maxInv, String companyName) {
        super(name, price, inv, minInv, maxInv);
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
