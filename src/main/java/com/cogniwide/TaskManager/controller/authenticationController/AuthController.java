package com.cogniwide.TaskManager.controller.authenticationController;

import com.cogniwide.TaskManager.DTO.UserSignInDTO;
import com.cogniwide.TaskManager.DTO.UserSignUpDTO;
import com.cogniwide.TaskManager.ServerResponse;
import com.cogniwide.TaskManager.customException.CustomException;
import com.cogniwide.TaskManager.service.authService.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("register")
    public ResponseEntity<ServerResponse<?>> register(@RequestBody UserSignUpDTO userSignUpDTO) {
        try {
            authService.registerUser(userSignUpDTO);
            ServerResponse<UserSignUpDTO> response = new ServerResponse<>(
                    HttpStatus.CREATED.value(),
                    userSignUpDTO,
                    "User account created successfully"
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "User registration failed unexpectedly");
        }
    }

    @PostMapping("login")
    public ResponseEntity<ServerResponse<?>> login(@RequestBody UserSignInDTO userSignInDTO) {
        try {
            String jwtTokenWithRoleMessage = authService.loginUser(userSignInDTO);
            ServerResponse<String> response = new ServerResponse<>(
                    HttpStatus.OK.value(),
                    jwtTokenWithRoleMessage.split(" ")[0],
                    "JWT generated successfully for " + jwtTokenWithRoleMessage.split(" ")[1]
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error during authentication");
        }
    }

}
