//package com.example.demo.controllers;
//
//public class ProductPurchaseController {
//}
package com.example.demo.controllers;

import com.example.demo.domain.Product;
import com.example.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class ProductPurchaseController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductPurchaseController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/buyProduct")
    public String buyProduct(@RequestParam("productID") Long productID, Model model) {
        Optional<Product> optionalProduct = productRepository.findById(productID);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            if (product.getInv() > 0) {
                product.setInv(product.getInv() - 1);
                productRepository.save(product);

                // Redirect to success page with a success message
                model.addAttribute("message", "Purchase Successful! " + product.getName() + " bought.");
                return "purchaseSuccess";
            } else {
                // Redirect to failure page if no inventory
                model.addAttribute("message", "Purchase Failed! " + product.getName() + " is out of stock.");
                return "purchaseError";
            }
        } else {
            // Redirect to failure page if product not found
            model.addAttribute("message", "Purchase Failed! Product not found.");
            return "purchaseError";
        }
    }

    @GetMapping("/purchaseSuccess")
    public String showSuccessPage() {
        return "purchaseSuccess";
    }

    @GetMapping("/purchaseError")
    public String showErrorPage() {
        return "purchaseError";
    }
}
