package com.example.ecommerce.Resources;


import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.StatisticMonthly;
import com.example.ecommerce.model.User;
import com.example.ecommerce.request.LoginRequest;
import com.example.ecommerce.request.RegisterRequest;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.service.AuthService;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.service.StatisticMonthlyService;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(path = "/auth")
public class AuthResource {
    @Autowired
    private AuthService authService;

    @Autowired
    private CartService cartService;

    @Autowired
    UserService userService;

    @Autowired
    StatisticMonthlyService statisticMonthlyService;

    @PostMapping("/login")
    ResponseEntity<BaseResponse> login(@RequestBody LoginRequest request) {
        User user = authService.getUserByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(
                            HttpStatus.NOT_FOUND.value(),
                            new String[]{Define.NOT_FOUND},
                            null
                    )
            );
        } else if (!user.getPassword().equals(request.getPassword())) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(
                            HttpStatus.NOT_FOUND.value(),
                            new String[]{Define.PASSWORD_IS_INCORRECT},
                            null
                    )
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(
                            HttpStatus.OK.value(),
                            new String[]{},
                            user
                    )
            );
        }

    }

    @PostMapping("/register")
    ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest request) {
        User user = new User(request.getName(), request.getUsername(), request.getPassword(), request.getRole(), false, 0.0);
        User userByName = authService.getUserByUsername(request.getUsername());

        String validate = Utils.validatePassword(request.getPassword());
        if (!validate.isEmpty()) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{validate}, null);
        }

        if (userByName != null) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(HttpStatus.CONFLICT.value(), new String[]{Define.USERNAME_WAS_EXIST}, null)
            );
        }
        try {
            User userInfo = authService.register(user);
            //Khởi tạo cart ngay khi customer tạo tài khoản
            if (request.getRole() == Define.ROLE_CUSTOMER) {
                if (cartService.checkUserHasCart(userInfo.getId())) {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    cartService.addCart(newCart);
                } else {
                    return Utils.getResponse(HttpStatus.OK.value(), new String[]{"Cart is exist"}, null);
                }
            }
            //Khởi tạo thống kê theo tháng ngay khi tạo tài khoản với seller
            StatisticMonthly statisticMonthly = new StatisticMonthly(userInfo, Utils.getCurrentMonth(), 0.0);
            statisticMonthlyService.addIncome(statisticMonthly);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(HttpStatus.OK.value(), new String[]{}, userInfo)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), new String[]{e.getMessage()}, null)
            );
        }
    }

    @PostMapping(path = "/change_password")
    ResponseEntity<BaseResponse> changePassword(@RequestBody MultiValueMap<String, String> request) {
        Integer idUser = Integer.parseInt(Objects.requireNonNull(request.getFirst("id_user")));
        String newPassword = Objects.requireNonNull(request.getFirst("new_password"));
        String oldPassword = Objects.requireNonNull(request.getFirst("old_password"));

        String validate = Utils.validatePassword(newPassword);
        if (!validate.isEmpty()) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{validate}, null);
        }
        User user = userService.findUserById(idUser);
        if (user == null) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"IdUser is incorrect!!"}, null);
        }
        if (!user.getPassword().equals(oldPassword)) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"Password is incorrect"}, null);
        }
        try {
            user.setPassword(newPassword);
            return Utils.getResponse(Define.ERROR_CODE, new String[]{}, userService.updateUser(user));
        } catch (Exception e) {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()}, null);
        }
    }

    @GetMapping("/verify_password")
    ResponseEntity<BaseResponse> verifyPassword(@RequestParam("id_user") Integer idUser, @RequestParam("password") String password){
        User user = userService.findUserById(idUser);
        if (user == null){
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{"user is not exist"}, null);
        }
        if (!user.getPassword().equals(password)){
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{"Password is not correct"}, null);
        }
        return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, user);

    }
}
