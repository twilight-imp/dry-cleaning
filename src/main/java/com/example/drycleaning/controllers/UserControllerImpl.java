package com.example.drycleaning.controllers;

import com.example.drycleaning.dtos.input.ReviewDtoInput;
import com.example.drycleaning.dtos.edit.UserProfileDtoEdit;
import com.example.drycleaning.dtos.output.CityDtoOutput;
import com.example.drycleaning.errors.AddUserException;
import com.example.drycleaning.models.Loyalty;
import com.example.drycleaning.models.User;
import com.example.drycleaning.services.*;
import com.example.drycleaning.services.impl.AuthServiceImpl;
import contracts.controllers.UserController;
import contracts.viewmodel.BaseViewModel;
import contracts.viewmodel.order.*;
import contracts.viewmodel.review.ReviewCreateForm;
import contracts.viewmodel.review.ReviewCreateViewModel;
import contracts.viewmodel.user.*;
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
public class UserControllerImpl implements UserController {

    private UserService userService;


    private OrderService orderService;

    private BranchService branchService;

    private ProductService productService;

    private ServiceService serviceService;

    private CityService cityService;

    private ReviewService reviewService;

    private AuthServiceImpl authService;

    private static final Logger LOG = LogManager.getLogger(Controller.class);

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setBranchService(BranchService branchService) {
        this.branchService = branchService;
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
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Autowired
    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    public void setAuthService(AuthServiceImpl authService) {
        this.authService = authService;
    }


    @Override
    @GetMapping()
    public String profile(Principal principal, Model model) {
        LOG.log(Level.INFO, "User " + principal.getName() + " show profile");
        String username = principal.getName();
        User user = authService.getUser(username);
        Loyalty loyalty = user.getLoyalty();
        var viewModel = new UserProfileViewModel(
                createBaseViewModel("Профиль"),
                user.getLastName(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getCity().getName(),
                loyalty.getDiscount(),
                loyalty.getNumOrders());
        model.addAttribute("model", viewModel);
        return "profile";
    }

    @Override
    @GetMapping("/edit")
    public String editProfile(Principal principal, Model model) {
        String username = principal.getName();
        User user = authService.getUser(username);
        var viewModel = new UserProfileEditViewModel(
                createBaseViewModel("Редактирование профиля")
        );
        List<CityDtoOutput> cities = cityService.findAll();
        model.addAttribute("cities", cities);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new UserProfileEditForm(user.getLastName(), user.getName(),user.getPhone(),user.getEmail(),user.getCity().getName(),"",""));
        return "profile-edit";
    }

    @Override
    @PostMapping("/edit")
    public String editProfile(Principal principal, @Valid @ModelAttribute("form")UserProfileEditForm form, BindingResult bindingResult, Model model) {
        LOG.log(Level.INFO, "User " + principal.getName() + " edit profile");
        String username = principal.getName();
        User user = authService.getUser(username);
        if (bindingResult.hasErrors()) {
            var viewModel = new UserProfileEditViewModel(
                    createBaseViewModel("Редактирование профиля")
            );
            List<CityDtoOutput> cities = cityService.findAll();
            model.addAttribute("cities", cities);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "profile-edit";
        }
        try{
            userService.modify(new UserProfileDtoEdit(form.lastName(),form.name(),form.phone(),form.email(), form.password(), form.newPassword(), form.city()),user.getId());

        }catch (AddUserException e){
            var viewModel = new UserProfileEditViewModel(createBaseViewModel("Редактирование профиля"));
            List<CityDtoOutput> cities = cityService.findAll();
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("cities", cities);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "profile-edit";
        }
         return "redirect:/profile";
    }

    @Override
    @GetMapping("/orders")
    public String ordersOfUser(Principal principal, Model model) {
        LOG.log(Level.INFO, "User " + principal.getName() + " show the order history");
        String email = principal.getName();
        var viewModel = new OrderUserViewModel(
                createBaseViewModel("История заказов"),
                orderService.findByUser(email).stream().map(o -> new OrderViewModel(
                        o.getId(),
                        o.getFinalPrice(),
                        o.getBranchId(),
                        branchService.findById(o.getBranchId()).getName(),
                        cityService.findById(branchService.findById(o.getBranchId()).getCity()).getName(),
                        branchService.findById(o.getBranchId()).getAddress(),
                        o.getDate(), o.getOrderStatus().getDescription(),
                        reviewService.existsByOrderId(o.getId()),
                        o.getOrderPosition().stream().map(op -> new OrderPositionViewModel(op.getId(),productService.findById(op.getProduct()).getName(),productService.findById(op.getProduct()).getMaterial(),serviceService.findById(op.getService()).getName(), op.getNumber(), op.getAmount())).toList())).toList());
        model.addAttribute("model", viewModel);
        return "orders-list";
    }

    @Override
    @GetMapping("/review")
    public String addReview(@RequestParam String orderId, Model model) {
        var viewModel = new ReviewCreateViewModel(
                createBaseViewModel("Добавление отзыва")
        );
        model.addAttribute("orderId", orderId);
        model.addAttribute("model", viewModel);
        model.addAttribute("form", new ReviewCreateForm("", 0));
        return "review-add";
    }

    @Override
    @PostMapping("/review")
    public String addReview(@RequestParam String orderId, @Valid @ModelAttribute("form") ReviewCreateForm form, BindingResult bindingResult, Model model, Principal principal) {
        LOG.log(Level.INFO, "User " + principal.getName() + " add review to order " + orderId);
        if (bindingResult.hasErrors()) {
            var viewModel = new ReviewCreateViewModel(
                    createBaseViewModel("Добавление отзыва")
            );
            model.addAttribute("orderId", orderId);
            model.addAttribute("model", viewModel);
            model.addAttribute("form", form);
            return "review-add";
        }

        reviewService.add(new ReviewDtoInput(form.comment(),form.score(),orderId));
        return "redirect:/profile/orders";
    }

    @Override
    public BaseViewModel createBaseViewModel(String pageTitle) {
        return new BaseViewModel(pageTitle);
    }
}
