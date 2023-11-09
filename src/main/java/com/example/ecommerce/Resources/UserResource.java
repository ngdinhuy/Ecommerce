package com.example.ecommerce.Resources;

import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.User;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.response.UserInfoResponse;
import com.example.ecommerce.service.OrderService;
import com.example.ecommerce.service.PaypalAuthService;
import com.example.ecommerce.service.S3Service;
import com.example.ecommerce.service.UserService;
import com.example.ecommerce.utils.Define;
import com.example.ecommerce.utils.Utils;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserResource {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    PaypalAuthService paypalAuthService;

    @GetMapping("/{id}")
    ResponseEntity<BaseResponse> getUserById(@PathVariable("id") Integer id){
        User user = userService.findUserById(id);
        List<Order> myListOrder = orderService.getListOrderByUserId(id);
        if (user == null){
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(HttpStatus.NOT_FOUND.value(), new String[]{Define.NOT_FOUND}, null)
            );
        }
        UserInfoResponse userInfoResponse = new UserInfoResponse(user, myListOrder.size());
        return ResponseEntity.status(HttpStatus.OK).body(
                new BaseResponse(HttpStatus.OK.value(), new String[]{}, userInfoResponse)
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

    @GetMapping("/bucket")
    ResponseEntity<BaseResponse> getListBucket(){
        return Utils.getResponse(0,new String[]{}, s3Service.getAllBucket());
    }

    @PostMapping("/change_avatar/{idUser}")
    ResponseEntity<BaseResponse> changeAvatar(MultipartFile multipartFile,@PathVariable Integer idUser){
        try{
            User user = userService.findUserById(idUser);
            if (user == null){
                return ResponseEntity.status(HttpStatus.OK).body(
                        new BaseResponse(HttpStatus.NOT_FOUND.value(), new String[]{Define.NOT_FOUND}, null)
                );
            }

            String url = String.format(Define.PATH_AVATAR_URL, idUser, idUser);
            if (user.getAvatar() != null){
                s3Service.deleteObject(Define.BUCKET_NAME, url);
            }
             String avatar = s3Service.uploadProductImage(url, Utils.convertMultipartToFile(multipartFile));
             user.setAvatar(avatar);
             return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, userService.updateUser(user));
        } catch (Exception e){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{e.getMessage()}, null);
        }
    }

    @GetMapping("/withdraw")
    ResponseEntity<BaseResponse> withdraw(@RequestParam("id_user") Integer idUser,
                                          @RequestParam("money") Double money,
                                          @RequestParam("mail_paypal") String mailPaypal,
                                          @RequestParam("password") String password,
                                          @RequestParam("is_save_mail_paypal") Boolean isSaveMailPaypal){
        User user = userService.findUserById(idUser);
        if (user == null){
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"User is not exist"}, null);
        }
        if (!user.getPassword().equals(password)){
            return Utils.getResponse(HttpStatus.NOT_FOUND.value(), new String[]{"Password is incorrect"}, null);
        }
        if (user.getProperty() < money){
            return Utils.getResponse(Define.ERROR_CODE, new String[]{"You don't have enough money"}, null);
        }
        //thanh toán với paypal
        String accessToken = paypalAuthService.getAccessToken();
        String responsePayment = paypalAuthService.transferMoneyToSeller(money.toString(), mailPaypal, accessToken);
        if (responsePayment.equals("Success")){
            if (isSaveMailPaypal) {
                user.setMailPaypal(mailPaypal);
            }
            user.setProperty(user.getProperty() - money);
            return Utils.getResponse(HttpStatus.OK.value(), new String[]{}, userService.updateUser(user));
        } else {
            return Utils.getResponse(Define.ERROR_CODE, new String[]{responsePayment}, null);
        }
    }

}
