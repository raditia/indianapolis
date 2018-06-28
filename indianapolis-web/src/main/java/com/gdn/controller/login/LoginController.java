package com.gdn.controller.login;

import com.gdn.login.LoginService;
import com.gdn.request.LoginRequest;
import com.gdn.response.LoginResponse;
import com.gdn.response.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @RequestMapping(
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<LoginResponse> loginWithEmailAndPassword(@RequestBody LoginRequest loginRequest){
        return loginService.loginWithEmailAndPassword(loginRequest);
    }
}
