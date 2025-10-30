package tech.erben.java17.records.solution.web;

import tech.erben.java17.records.solution.service.InventoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/")
    public String showInventory(@RequestParam(required = false) BigDecimal discount,
                                @RequestParam(required = false) String vin,
                                Model model) {
        model.addAttribute("cars", inventoryService.inventory());
        model.addAttribute("leasingOffers", inventoryService.leasingOffers());
        model.addAttribute("testDriveBookings", inventoryService.testDriveBookings());

        if (vin != null && discount != null) {
            var result = inventoryService.prepareDiscountPreview(vin, discount);
            model.addAttribute("discountedCar", result.preview());
            model.addAttribute("discountMemory", result.history());
        }

        if (!model.containsAttribute("discountMemory")) {
            model.addAttribute("discountMemory", inventoryService.discountHistory());
        }

        return "inventory";
    }
}
