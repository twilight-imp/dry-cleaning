package com.example.drycleaning.controllers.admin;

import com.example.drycleaning.dtos.input.ProductDtoInput;
import com.example.drycleaning.dtos.output.CityDtoOutput;
import com.example.drycleaning.dtos.output.TypeProductDtoOutput;
import com.example.drycleaning.services.ProductService;
import com.example.drycleaning.services.CityService;
import com.example.drycleaning.services.TypeProductService;
import contracts.controllers.admin.ProductAdminController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.admin.product.ProductAdminCreateViewModel;
import contracts.viewmodel.admin.product.ProductAdminEditViewModel;
import contracts.viewmodel.admin.product.ProductAdminViewModel;
import contracts.viewmodel.admin.product.ProductListAdminViewModel;
import contracts.viewmodel.admin.product.form.ProductAdminCreateFrom;
import contracts.viewmodel.admin.product.form.ProductAdminEditForm;
import contracts.viewmodel.product.ProductViewModel;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductAdminControllerImpl implements ProductAdminController {

    private ProductService productService;

    private TypeProductService typeProductService;
    private static final Logger LOG = LogManager.getLogger(Controller.class);
    @Autowired
    public void setProductService(ProductService productService){
        this.productService = productService;
    }

    @Autowired
    public void setTypeProductService(TypeProductService typeProductService){
        this.typeProductService = typeProductService;
    }
    @Override
    public BaseViewModel createBaseViewModel(String pageTitle) {
        return new BaseViewModel(
                pageTitle
        );
    }

    @Override
    @GetMapping
    public String list(Model model) {
        var productViewModels = productService.findAll().stream()
                .map(p -> new ProductAdminViewModel(p.getId(),p.getName(),p.getMaterial(), p.getExtraCharge(),typeProductService.findById(p.getTypeProductId()).getName()))
                .toList();

        var viewModel = new ProductListAdminViewModel(
                createBaseViewModel("Изделия"),
                productViewModels
        );

        model.addAttribute("model", viewModel);
        return "admin-product-list";
    }

    @Override
    @GetMapping("/create")
    public String createForm(Model model) {
        var viewModel = new ProductAdminCreateViewModel(
                createBaseViewModel("Добавление изделия")
        );
        List<TypeProductDtoOutput> typesProduct = typeProductService.findAll();
        model.addAttribute("typesProduct", typesProduct);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new ProductAdminCreateFrom("","",0,""));
        return "admin-product-create";
    }

    @Override
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("form")ProductAdminCreateFrom form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Admin create new product");
        if (bindingResult.hasErrors()) {
            var viewModel = new ProductAdminCreateViewModel(
                    createBaseViewModel("Добавление изделия")
            );
            List<TypeProductDtoOutput> typesProduct = typeProductService.findAll();
            model.addAttribute("typesProduct", typesProduct);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "admin-product-create";
        }
        var id = productService.add(new ProductDtoInput(form.name(),form.material(),form.extraCharge(), form.typeProductId() ));
        return "redirect:/admin/products";
    }

    @Override
    @GetMapping("/edit")
    public String editForm(@RequestParam String id, Model model) {
        var product = productService.findById(id);
        var viewModel = new ProductAdminEditViewModel(
                createBaseViewModel("Редактирование изделия")
        );
        List<TypeProductDtoOutput> typesProduct = typeProductService.findAll();
        model.addAttribute("typesProduct", typesProduct);
        model.addAttribute("id", id);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new ProductAdminEditForm(product.getId(), product.getName(), product.getMaterial(),product.getExtraCharge(),product.getTypeProductId()));
        return "admin-product-edit";
    }

    @Override
    @PostMapping("/edit")
    public String edit(@RequestParam String id,@Valid @ModelAttribute("form")  ProductAdminEditForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Admin edit product " + id);
        if (bindingResult.hasErrors()) {
            var viewModel = new ProductAdminEditViewModel(
                    createBaseViewModel("Редактирование изделия")
            );
            List<TypeProductDtoOutput> typesProduct = typeProductService.findAll();
            model.addAttribute("typesProduct", typesProduct);
            model.addAttribute("id", id);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "admin-product-edit";
        }

        productService.modify(new ProductDtoInput(form.name(),form.material(), form.extraCharge(),form.typeProductId()),id);
        return "redirect:/admin/products";
    }

    @Override
    @GetMapping("/delete")
    public String delete(@RequestParam String id) {
        LOG.log(Level.INFO, "Admin delete product " + id);
        productService.remove(id);
        return "redirect:/admin/products";
    }
}




