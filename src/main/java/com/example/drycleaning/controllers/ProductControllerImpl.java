package com.example.drycleaning.controllers;

import com.example.drycleaning.models.TypeProduct;
import com.example.drycleaning.services.ProductService;
import com.example.drycleaning.services.TypeProductService;
import contracts.controllers.ProductController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.product.ProductListViewModel;
import contracts.viewmodel.product.ProductViewModel;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductControllerImpl implements ProductController {

    private ProductService productService;
    private TypeProductService typeProductService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setTypeProductService(TypeProductService typeProductService) {
        this.typeProductService = typeProductService;
    }


    @Override
    public BaseViewModel createBaseViewModel(String pageTitle) {
        return new BaseViewModel(pageTitle);
    }



    @Override
    @GetMapping()
    public String listProducts(Principal principal, Model model) {
        if (principal != null) {
            LOG.log(Level.INFO, "User " + principal.getName() + " show all products");
        } else {
            LOG.log(Level.INFO, "Unauthenticated user show all products");
        }
        var baseViewModel = createBaseViewModel("Изделия");
        if (principal != null) {
            String email = principal.getName();
            var viewModel = new ProductListViewModel(
                    baseViewModel,
                    productService.findAllForUser(email).stream().map(p -> new ProductViewModel(p.getName(),typeProductService.findById(p.getTypeProductId()).getName(),p.getMaterial(), p.getExtraCharge())).toList());
            model.addAttribute("model", viewModel);
        }else {

            var viewModel = new ProductListViewModel(
                    baseViewModel,
                    productService.findAll().stream().map(p -> new ProductViewModel(p.getName(), typeProductService.findById(p.getTypeProductId()).getName(), p.getMaterial(), p.getExtraCharge())).toList());
            model.addAttribute("model", viewModel);
        }
        return "products-list";
    }

}
