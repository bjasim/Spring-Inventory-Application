package com.example.demo.controllers;

import com.example.demo.domain.InhousePart;
import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.repositories.PartRepository;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Controller for handling part management.
 */
@Controller
public class AddPartController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PartRepository partRepository;

    @GetMapping("/showPartFormForUpdate")
    public String showPartFormForUpdate(@RequestParam("partID") int theId, Model theModel) {
        PartService repo = context.getBean(PartServiceImpl.class);
        OutsourcedPartService outsourcedrepo = context.getBean(OutsourcedPartServiceImpl.class);
        InhousePartService inhouserepo = context.getBean(InhousePartServiceImpl.class);

        boolean inhouse = true;
        Part existingPart = repo.findById(theId); // Fetch part from DB

        if (existingPart instanceof OutsourcedPart) {
            inhouse = false;
        }

        String formType;
        if (inhouse) {
            InhousePart inhousePart = inhouserepo.findById(theId);
            if (inhousePart == null) {
                inhousePart = new InhousePart(); // Prevent null pointer errors
            }
            theModel.addAttribute("inhousepart", inhousePart);
            formType = "InhousePartForm";
        } else {
            OutsourcedPart outsourcedPart = outsourcedrepo.findById(theId);
            if (outsourcedPart == null) {
                outsourcedPart = new OutsourcedPart();
            }
            theModel.addAttribute("outsourcedpart", outsourcedPart);
            formType = "OutsourcedPartForm";
        }

        System.out.println(" Rendering form: " + formType + " with ID: " + theId);
        return formType;
    }

    @GetMapping("/deletepart")
    public String deletePart(@Valid @RequestParam("partID") int theId, Model theModel) {
        PartService repo = context.getBean(PartServiceImpl.class);
        Part part = repo.findById(theId);
        if (part.getProducts().isEmpty()) {
            repo.deleteById(theId);
            return "confirmationdeletepart";
        } else {
            return "negativeerror";
        }
    }

    @PostMapping("/savePart")
    public String savePart(@Valid @ModelAttribute("part") Part part, BindingResult result, Model model) {
        System.out.println("DEBUG: Entering savePart method...");
        System.out.println("Received Part -> ID: " + part.getId() + ", Name: " + part.getName() + ", MinInv: " + part.getMinInv() + ", MaxInv: " + part.getMaxInv() + ", Inv: " + part.getInv());

        // Fetch part type from DB
        Part existingPart = partRepository.findById(part.getId()).orElse(null);

        if (existingPart instanceof OutsourcedPart) {
            OutsourcedPart outsourcedPart = (OutsourcedPart) existingPart;
            System.out.println("Updating OutsourcedPart: " + outsourcedPart.getCompanyName());
        } else if (existingPart instanceof InhousePart) {
            InhousePart inhousePart = (InhousePart) existingPart;
            System.out.println("Updating InhousePart: " + inhousePart.getName());
        }

        // Ensure correct values are retrieved
        if (existingPart != null) {
            part.setMinInv(existingPart.getMinInv());
            part.setMaxInv(existingPart.getMaxInv());
        }

        // Validation checks
        if (part.getMinInv() > part.getMaxInv()) {
            result.rejectValue("minInv", "error.part", "Min inventory cannot be greater than Max inventory");
        }
        if (part.getInv() < part.getMinInv() || part.getInv() > part.getMaxInv()) {
            result.rejectValue("inv", "error.part", "Inventory must be between Min and Max inventory");
        }

        if (result.hasErrors()) {
            return "partForm";  // Return form with errors
        }

        partRepository.save(part);
        System.out.println(" Part saved successfully!");
        return "redirect:/";
    }

}
