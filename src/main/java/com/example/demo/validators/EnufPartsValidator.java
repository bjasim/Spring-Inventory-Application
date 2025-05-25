package com.example.demo.validators;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *  Validator to ensure product inventory changes do not violate part inventory constraints.
 */
public class EnufPartsValidator implements ConstraintValidator<ValidEnufParts, Product> {

    @Autowired
    private ApplicationContext context;

    public static ApplicationContext myContext;

    @Override
    public void initialize(ValidEnufParts constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Product product, ConstraintValidatorContext constraintValidatorContext) {
        if (context == null) return true;  // Skip validation if context is not available
        if (context != null) myContext = context;

        ProductService repo = myContext.getBean(ProductServiceImpl.class);

        if (product.getId() != 0) {
            Product myProduct = repo.findById((int) product.getId());

            if (myProduct.getParts().isEmpty()) {
                return true; // No parts to validate, allow update
            }

            for (Part p : myProduct.getParts()) {
                // Instead of calculations, directly compare with MinInv
                if (product.getInv() < p.getMinInv()) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                            "Updating this product would cause part '" + p.getName() +
                                    "' to fall below its minimum inventory of " + p.getMinInv()
                    ).addConstraintViolation();
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
