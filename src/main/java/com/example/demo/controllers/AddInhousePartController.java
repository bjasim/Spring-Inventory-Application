package com.example.demo.controllers;

import com.example.demo.domain.InhousePart;
import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 *
 *
 *
 *
 */
@Controller
public class AddInhousePartController {
    @Autowired
    private ApplicationContext context;

    @GetMapping("/showFormAddInPart")
    public String showFormAddInhousePart(Model theModel) {
        InhousePart inhousepart = new InhousePart();
        theModel.addAttribute("inhousepart", inhousepart);
        return "InhousePartForm";
    }

    @PostMapping("/showFormAddInPart")
    public String submitForm(@Valid @ModelAttribute("inhousepart") InhousePart part, BindingResult theBindingResult, Model theModel) {
        theModel.addAttribute("inhousepart", part);

        // Validate minInv <= maxInv
        if (part.getMinInv() > part.getMaxInv()) {
            theBindingResult.rejectValue("minInv", "error.part", "Min inventory cannot be greater than Max inventory");
        }

        // Validate inventory only ONCE
        if (part.getInv() < part.getMinInv()) {
            theBindingResult.rejectValue("inv", "error.part", "Inventory must be at least " + part.getMinInv());
        } else if (part.getInv() > part.getMaxInv()) {
            theBindingResult.rejectValue("inv", "error.part", "Inventory cannot exceed " + part.getMaxInv());
        }

        // Check if there are any errors
        if (theBindingResult.hasErrors()) {
            return "InhousePartForm"; // Return to form with one error message
        }

        // Save part if validation passes
        InhousePartService repo = context.getBean(InhousePartServiceImpl.class);
        InhousePart ip = repo.findById((int) part.getId());
        if (ip != null) part.setProducts(ip.getProducts());
        repo.save(part);

        return "confirmationaddpart";
    }

}