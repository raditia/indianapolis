package com.gdn.login;

import com.gdn.request.LoginRequest;
import com.gdn.response.LoginResponse;
import com.gdn.response.WebResponse;

public interface LoginService {
    WebResponse<LoginResponse> loginWithEmailAndPassword(LoginRequest loginRequest);
}
