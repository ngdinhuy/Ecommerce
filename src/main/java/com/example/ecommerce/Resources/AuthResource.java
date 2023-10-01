package com.example.ecommerce.Resources;


import com.example.ecommerce.model.Cart;
import com.example.ecommerce.model.User;
import com.example.ecommerce.request.LoginRequest;
import com.example.ecommerce.request.RegisterRequest;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.service.AuthService;
import com.example.ecommerce.service.CartService;
import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthResource {
    @Autowired
    private AuthService authService;

    @Autowired
    private CartService cartService;

    @PostMapping("/login")
    ResponseEntity<BaseResponse> login(@RequestBody LoginRequest request){
        User user = authService.getUserByUsername(request.getUsername());
        if (user == null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(
                            HttpStatus.NOT_FOUND.value(),
                            new String[]{Define.NOT_FOUND},
                            null
                    )
            );
        } else if (!user.getPassword().equals(request.getPassword())){
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
    ResponseEntity<BaseResponse> register(@RequestBody RegisterRequest request){
        User user = new User(request.getName(), request.getUsername(), request.getPassword(), request.getRole());
        User userByName = authService.getUserByUsername(request.getUsername());
        if (userByName != null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(HttpStatus.CONFLICT.value(), new String[]{Define.USERNAME_WAS_EXIST},null)
            );
        }
        try{
            User userInfo = authService.register(user);
            //Khởi tạo cart ngay khi customer tạo tài khoản
            if (request.getRole() == Define.ROLE_CUSTOMER){
                if (cartService.checkUserHasCart(userInfo.getId())){
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    cartService.addCart(newCart);
                } else {
                    return Utils.getResponse(HttpStatus.OK.value(), new String[]{"Cart is exist"}, null);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(HttpStatus.OK.value(),new String[]{}, userInfo)
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new BaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),new String[]{e.getMessage()}, null)
            );
        }
    }
}
