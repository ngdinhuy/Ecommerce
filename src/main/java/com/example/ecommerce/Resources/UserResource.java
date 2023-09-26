package com.example.ecommerce.Resources;

import com.example.ecommerce.model.User;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
public class UserResource {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    ResponseEntity<BaseResponse> getUserById(@PathVariable("id") Integer id){
        User user = userService.findUserById(id);
        if (user == null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(HttpStatus.NOT_FOUND.value(), new String[]{Define.NOT_FOUND}, null)
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse(HttpStatus.OK.value(), new String[]{}, user)
        );
    }

    @PutMapping("/{id}")
    ResponseEntity<BaseResponse> updateUser(@RequestBody User userUpdate, @PathVariable Integer id){
        User user = userService.findUserById(id);
        if (user == null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(HttpStatus.NOT_FOUND.value(), new String[]{Define.NOT_FOUND}, null)
            );
        }
        Utils.updateUser(user, userUpdate);
        try{
            User newUser = userService.updateUser(user);
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, newUser);
        } catch (Exception e){
            return Utils.getResponse(HttpStatus.NO_CONTENT.value(), new String[]{e.getMessage()},"");
        }
    }
}
