package com.example.drycleaning.controllers.admin;

import com.example.drycleaning.models.OrderStatus;
import com.example.drycleaning.dtos.edit.OrderDtoEdit;
import com.example.drycleaning.services.*;
import contracts.controllers.admin.OrderAdminController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.admin.branch.BranchAdminViewModel;
import contracts.viewmodel.admin.order.OrderAdminEditViewModel;
import contracts.viewmodel.admin.order.OrderAdminViewModel;
import contracts.viewmodel.admin.order.OrderListAdminViewModel;
import contracts.viewmodel.admin.order.form.OrderAdminEditForm;
import contracts.viewmodel.admin.order.OrderPositionAdminViewModel;
import jakarta.validation.Valid;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@Controller
public class OrderAdminControllerImpl implements OrderAdminController {

    private OrderService orderService;
    private ReviewService reviewService;


    private UserService userService;

    private BranchService branchService;

    private CityService cityService;

    private ProductService productService;

    private ServiceService serviceService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setOrderService(OrderService orderService){
        this.orderService = orderService;
    }
    @Autowired
    public void setReviewService(ReviewService reviewService){
        this.reviewService = reviewService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setBranchService(BranchService branchService) {
        this.branchService = branchService;
    }
    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }
    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
    @Autowired
    public void setServiceService(ServiceService serviceService) {
        this.serviceService = serviceService;
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
        var orderViewModels = orderService.findAll().stream()
                .map(o -> new OrderAdminViewModel(o.getId(),userService.findById(o.getUserId()).getPhone(),
                        new BranchAdminViewModel(o.getBranchId(),branchService.findById(o.getBranchId()).getName(), cityService.findByBranchId(o.getBranchId()).getName(), branchService.findById(o.getBranchId()).getAddress(), branchService.findById(o.getBranchId()).getPhone() ),o.getDate(),
                        o.getOrderStatus().getDescription(), o.getFinalPrice(),
                        o.getOrderPosition().stream().map(op -> new OrderPositionAdminViewModel(op.getId(), productService.findById(op.getProduct()).getName(), productService.findById(op.getProduct()).getMaterial(),serviceService.findById(op.getService()).getName(), op.getNumber(), op.getAmount())).toList(),
                        Optional.ofNullable(reviewService.findByOrderId(o.getId()))
                                .map(review -> review.getComment())
                                .orElse(""),
                        Optional.ofNullable(reviewService.findByOrderId(o.getId()))
                                .map(review -> review.getScore())
                                .orElse(0)
                )).toList();

        var viewModel = new OrderListAdminViewModel(
                createBaseViewModel("Заказы"),
                orderViewModels
        );

        model.addAttribute("model", viewModel);
        return "admin-order-list";
    }


    @Override
    @GetMapping("/edit")
    public String editForm(@RequestParam String id, Model model) {
        var order = orderService.findById(id);
        var viewModel = new OrderAdminEditViewModel(
                createBaseViewModel("Редактирование заказа")
        );
        model.addAttribute("statuses", Arrays.stream(OrderStatus.values())
                .filter(status -> !status.equals(OrderStatus.NEW))
                .toList());
        model.addAttribute("id", id);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new OrderAdminEditForm(order.getId(), order.getBranchId(),order.getOrderStatus().name()));
        return "admin-order-edit";
    }

    @Override
    @PostMapping("/edit")
    public String edit(@RequestParam String id,@Valid @ModelAttribute("form")  OrderAdminEditForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "Update status order " + id);
        if (bindingResult.hasErrors()) {
            var viewModel = new OrderAdminEditViewModel(
                    createBaseViewModel("Редактирование заказа")
            );
            model.addAttribute("id", id);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "admin-order-edit";
        }


        orderService.modify(new OrderDtoEdit(form.orderStatus(),form.branchId()), id);
        return "redirect:/admin/orders";
    }


}

