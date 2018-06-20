package com.gdn.login.implementation;

import com.gdn.entity.User;
import com.gdn.login.LoginService;
import com.gdn.mapper.LoginResponseMapper;
import com.gdn.repository.UserRepository;
import com.gdn.request.LoginRequest;
import com.gdn.response.LoginResponse;
import com.gdn.response.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public WebResponse<LoginResponse> loginWithEmailAndPassword(LoginRequest loginRequest) {
        User user = userRepository.findByEmailAddressAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if(user != null){
            return WebResponse.OK(LoginResponseMapper.toLoginResponse(user));
        } else{
            return WebResponse.NOT_FOUND();
        }
    }

}
