package com.example.demo.controllers;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.service.PartService;
import com.example.demo.service.ProductService;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Controller for handling the main screen displaying parts and products.
 */
@Controller
public class MainScreenController {

    private final PartService partService;
    private final ProductService productService;

    public MainScreenController(PartService partService, ProductService productService) {
        this.partService = partService;
        this.productService = productService;
    }

    @GetMapping("/mainscreen")
    public String listPartsAndProducts(Model theModel,
                                       @Param("partkeyword") String partkeyword,
                                       @Param("productkeyword") String productkeyword) {
        try {
            // Fetch and add parts to the model
            List<Part> partList = partService.listAll(partkeyword);
            theModel.addAttribute("parts", partList);
            theModel.addAttribute("partkeyword", partkeyword);

            // Fetch and add products to the model
            List<Product> productList = productService.listAll(productkeyword);
            theModel.addAttribute("products", productList);
            theModel.addAttribute("productkeyword", productkeyword);

        } catch (Exception e) {
            // Handle errors and add error message to the model
            theModel.addAttribute("error", "Error fetching data: " + e.getMessage());
        }

        // Return the view for the main screen
        return "mainscreen";
    }

    /**
     * Redirects the root URL (/) to /mainscreen.
     *
     * @return Redirect to the main screen.
     */
    @GetMapping("/")
    public String redirectToMainScreen() {
        return "redirect:/mainscreen";
    }
}
