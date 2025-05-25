package com.example.demo.bootstrap;

import com.example.demo.domain.InhousePart;
import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Product;
import com.example.demo.repositories.PartRepository;
import com.example.demo.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Bootstraps sample data for Parts and Products
 */
@Component
public class BootStrapData implements CommandLineRunner {

    private final PartRepository partRepository;
    private final ProductRepository productRepository;

    public BootStrapData(PartRepository partRepository, ProductRepository productRepository) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {


        System.out.println("🛠️ Deleting all existing data to refresh schema...");

        // Delete existing records, kept it on purpose as I may need it later
//        partRepository.deleteAll();
//        productRepository.deleteAll();

        System.out.println(" Database cleared. Now adding fresh data...");
        if (partRepository.count() == 0 && productRepository.count() == 0) {

            InhousePart part1 = new InhousePart("Latte Mug", 10.0, 50, 10, 100, 101);
            OutsourcedPart part2 = new OutsourcedPart("Coffee Spoon", 3.0, 100, 20, 200, "Spoon Supplies Co.");
            InhousePart part3 = new InhousePart("Coffee Filter", 5.0, 80, 15, 150, 102);
            InhousePart part4 = new InhousePart("Espresso Tamper", 15.0, 40, 5, 50, 103);
            OutsourcedPart part5 = new OutsourcedPart("Stainless Steel Straw", 2.5, 120, 25, 250, "Steel Co.");
//            InhousePart part1 = new InhousePart("Latte Mug", 10.0, 50, 10, 100, 101);
//            InhousePart part2 = new InhousePart("Coffee Spoon", 3.0, 100, 20, 200, 102);  // Changed from OutsourcedPart
//            InhousePart part3 = new InhousePart("Coffee Filter", 5.0, 80, 15, 150, 103);
//            InhousePart part4 = new InhousePart("Espresso Tamper", 15.0, 40, 5, 50, 104);
//            InhousePart part5 = new InhousePart("Stainless Steel Straw", 2.5, 120, 25, 250, 105);  // Changed from OutsourcedPart

            partRepository.save(part1);
            partRepository.save(part2);
            partRepository.save(part3);
            partRepository.save(part4);
            partRepository.save(part5);

            Product product1 = new Product("Espresso Blend", 12.0, 40);
            Product product2 = new Product("French Roast", 15.0, 30);
            Product product3 = new Product("Vanilla Syrup", 10.0, 50);
            Product product4 = new Product("Hazelnut Syrup", 12.0, 30);
            Product product5 = new Product("Cold Brew Coffee", 20.0, 25);

            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);

        } else {
            System.out.println("Sample inventory already exists. Skipping addition.");
        }

        // Logging the current state of the database
        System.out.println("Number of Products: " + productRepository.count());
        System.out.println(productRepository.findAll());
        System.out.println("Number of Parts: " + partRepository.count());
        System.out.println(partRepository.findAll());
    }
}
