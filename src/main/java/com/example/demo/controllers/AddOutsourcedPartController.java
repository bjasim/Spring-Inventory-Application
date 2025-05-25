package com.example.demo.controllers;

import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.service.OutsourcedPartService;
import com.example.demo.service.OutsourcedPartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

/**
 * Controller for adding outsourced parts with validation.
 */
@Controller
public class AddOutsourcedPartController {
    @Autowired
    private ApplicationContext context;

    @GetMapping("/showFormAddOutPart")
    public String showFormAddOutsourcedPart(Model theModel) {
        OutsourcedPart outsourcedPart = new OutsourcedPart();
        theModel.addAttribute("outsourcedpart", outsourcedPart);
        return "OutsourcedPartForm";
    }

    @PostMapping("/showFormAddOutPart")
    public String submitForm(@Valid @ModelAttribute("outsourcedpart") OutsourcedPart part, BindingResult bindingResult, Model theModel) {
        theModel.addAttribute("outsourcedpart", part);

        // Validate minInv <= maxInv
        if (part.getMinInv() > part.getMaxInv()) {
            bindingResult.rejectValue("minInv", "error.part", "Min inventory cannot be greater than Max inventory");
        }

        // Validate inventory with separate error messages
        if (part.getInv() < part.getMinInv()) {
            bindingResult.rejectValue("inv", "error.part", "Inventory must be at least " + part.getMinInv());
        } else if (part.getInv() > part.getMaxInv()) {
            bindingResult.rejectValue("inv", "error.part", "Inventory cannot exceed " + part.getMaxInv());
        }

        // Check for validation errors before proceeding
        if (bindingResult.hasErrors()) {
            return "OutsourcedPartForm"; // Stay on the form with error messages
        }

        // Save the part after validation passes
        OutsourcedPartService repo = context.getBean(OutsourcedPartServiceImpl.class);
        OutsourcedPart existingPart = repo.findById((int) part.getId());
        if (existingPart != null) {
            part.setProducts(existingPart.getProducts());
        }
        repo.save(part);

        return "confirmationaddpart";
    }
}
