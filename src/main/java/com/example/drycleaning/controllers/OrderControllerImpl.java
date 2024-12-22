package com.example.drycleaning.controllers;

import com.example.drycleaning.dtos.input.OrderPositionDtoInput;
import com.example.drycleaning.dtos.output.*;
import com.example.drycleaning.services.*;
import contracts.controllers.OrderController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.order.*;
import contracts.viewmodel.user.BasketViewModel;
import contracts.viewmodel.user.UserBasketViewModel;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class OrderControllerImpl implements OrderController {

    private OrderService orderService;
    private OrderPositionService orderPositionService;
    private ProductService productService;
    private ServiceService serviceService;

    private BranchService branchService;

    private CityService cityService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setOrderPositionService(OrderPositionService orderPositionService) {
        this.orderPositionService = orderPositionService;
    }

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @Autowired
    public void setBranchService(BranchService branchService) {
        this.branchService = branchService;
    }

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }


    @Override
    public BaseViewModel createBaseViewModel(String pageTitle) {
        return new BaseViewModel(pageTitle);
    }

    @Override
    @GetMapping("/order")
    public String listOrderPositions(Principal principal, Model model) {
        LOG.log(Level.INFO, "User " + principal.getName() + " show new order");
        String email = principal.getName();
        OrderDtoOutput orderDtoOutput = orderService.findBasketUser(email);
        var viewModel = new UserBasketViewModel(
                createBaseViewModel("Заказ"),
                new BasketViewModel(
                orderDtoOutput.getFinalPrice(),
                orderDtoOutput.getBranchId(),
                branchService.findById(orderDtoOutput.getBranchId()).getName(),
                cityService.findByBranchId(orderDtoOutput.getBranchId()).getName(),
                branchService.findById(orderDtoOutput.getBranchId()).getAddress(),
                        orderDtoOutput.getOrderPosition().stream().map(op -> new OrderPositionViewModel(op.getId(),productService.findById(op.getProduct()).getName(), productService.findById(op.getProduct()).getMaterial(),serviceService.findById(op.getService()).getName(), op.getNumber(), op.getAmount())).toList()));
        model.addAttribute("model", viewModel);
        return "order";
    }

    @Override
    @GetMapping("/order/add")
    public String addOrderPosition(Model model) {
        var viewModel = new OrderPositionCreateViewModel(
                createBaseViewModel("Добавление позиции заказа")
        );

        model.addAttribute("products", productService.findAll());
        model.addAttribute("services", serviceService.findAll());
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new OrderPositionCreateForm("", "", 1));
        return "order-position-add";
    }

    @Override
    @PostMapping("/order/add")
    public String addOrderPosition(Principal principal, @Valid  @ModelAttribute("form") OrderPositionCreateForm form,
                                   BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "User " + principal.getName() + " add order position to new order");
        if (bindingResult.hasErrors()) {
            var viewModel = new OrderPositionCreateViewModel(
                    createBaseViewModel("Добавление позиции заказа")
            );
            model.addAttribute("products", productService.findAll());
            model.addAttribute("services", serviceService.findAll());
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "order-position-add";
        }
        String email = principal.getName();
        orderPositionService.add(new OrderPositionDtoInput(form.product(),form.service(),orderService.findBasketUser(email).getId(),form.number()));
        return "redirect:/user/order";
    }

    @Override
    @GetMapping("/order/edit")
    public String editOrderPosition(@RequestParam String orderPosition, Model model) {
        var orderPositionVar = orderPositionService.findById(orderPosition);
        var viewModel = new OrderPositionEditViewModel(
                createBaseViewModel("Изменение позиции заказа")
        );
        model.addAttribute("products", productService.findAll());
        model.addAttribute("services", serviceService.findAll());
        model.addAttribute("model", viewModel);
        model.addAttribute("position", orderPositionVar.getId());
        model.addAttribute("form", new OrderPositionEditForm(orderPositionVar.getProduct(), orderPositionVar.getService(),orderPositionVar.getNumber()));
        return "order-position-edit";
    }

    @Override
    @PostMapping("/order/edit")
    public String editOrderPosition(Principal principal,@RequestParam String orderPosition, @Valid @ModelAttribute("form") OrderPositionEditForm form,BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "User " + principal.getName() + " edit order position in new order");
        if (bindingResult.hasErrors()) {
            var viewModel = new OrderPositionEditViewModel(
                    createBaseViewModel("Изменение позиции заказа")
            );
            List<ProductDtoOutput> products = productService.findAll();
            List<ServiceDtoOutput> services = serviceService.findAll();
            model.addAttribute("products", products);
            model.addAttribute("services", services);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "order-position-edit";
        }

        orderPositionService.modify(new OrderPositionDtoInput(form.product(), form.service(),orderService.findBasketUser(principal.getName()).getId(),form.number()),orderPosition);
        return "redirect:/user/order";
    }
    @Override
    @GetMapping("/order/delete")
    public String delete(@RequestParam String orderPosition, Principal principal) {
        LOG.log(Level.INFO, "User " + principal.getName() + " delete order position from new order");
        orderPositionService.remove(orderPosition);
        return "redirect:/user/order";
    }

    @Override
    @GetMapping("/order/editBranch")
    public String editBranch(Principal principal, Model model) {
        String email = principal.getName();
        var order = orderService.findBasketUser(email);
        var viewModel = new OrderEditViewModel(
                createBaseViewModel("Изменение филиала")
        );
        List<BranchDtoOutput> branches = branchService.findByCityAndRating(email);
        model.addAttribute("branches", branches);
        model.addAttribute("model", viewModel);

        model.addAttribute("form", new OrderEditForm(order.getId(),order.getUserId(),order.getBranchId()));
        return "order-branch-edit";
    }

    @Override
    @PostMapping("/order/editBranch")
    public String editBranch(Principal principal, @Valid @ModelAttribute("form")OrderEditForm form,BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "User " + principal.getName() + " change branch for new order");
        String email = principal.getName();
        if (bindingResult.hasErrors()) {
            var viewModel = new OrderEditViewModel(
                    createBaseViewModel("Изменение филиала")
            );
            List<BranchDtoOutput> branches = branchService.findByCityAndRating(email);
            model.addAttribute("branches", branches);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "order-branch-edit";
        }

        orderService.changeBranch(form.branch(),form.orderId());
        return "redirect:/user/order";

    }

    @Override
    @GetMapping("/order/create")
    public String createOrder(Principal principal, Model model) {
        LOG.log(Level.INFO, "User " + principal.getName() + " create new order");
        String email = principal.getName();
        OrderDtoOutput orderDtoOutput = orderService.findBasketUser(email);
        if (orderDtoOutput.getOrderPosition().isEmpty()) {
            model.addAttribute("errorMessage", "Заказ пуст. Добавьте позиции в заказ.");
        }
        else if (orderDtoOutput.getBranchId() == null) {
            model.addAttribute("errorMessage", "Пожалуйста, выберите филиал.");
        }
        else orderService.confirm(orderDtoOutput.getId());
        return "redirect:/profile/orders";
    }

}
