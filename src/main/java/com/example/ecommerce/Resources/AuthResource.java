package com.example.ecommerce.Resources;


import com.example.ecommerce.model.User;
import com.example.ecommerce.request.LoginRequest;
import com.example.ecommerce.response.BaseResponse;
import com.example.ecommerce.service.AuthService;
import com.example.ecommerce.utils.Define;
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

    @PostMapping("/login")
//    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BaseResponse> login(@RequestBody LoginRequest request){
        User user = authService.login(request.getUsername(),request.getPassword());
        if (user == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse(
                            Define.NOT_FOUND,
                            new String[]{Define.NOT_FOUND},
                            ""
                    )
            );
        } else if (user.getPassword().equals(request.getPassword())){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new BaseResponse(
                            Define.NOT_FOUND,
                            new String[]{Define.NOT_FOUND},
                            ""
                    )
            );
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new BaseResponse(
                            Define.SUCCESS,
                            new String[]{},
                            user
                    )
            );
        }

    }

    @GetMapping("")
    String test(){
        return "huy";
    }
}
