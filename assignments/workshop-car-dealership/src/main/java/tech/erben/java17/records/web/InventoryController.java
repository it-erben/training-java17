package tech.erben.java17.records.web;

import tech.erben.java17.records.service.InventoryService;
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
        if (vin != null && discount != null) {
            var preview = inventoryService.prepareDiscountPreview(vin, discount);
            if (preview != null) {
                model.addAttribute("discountedCar", preview);
            }
        }
        model.addAttribute("discountMemory", inventoryService.getDiscountCache());
        return "inventory";
    }
}
