package com.cogniwide.TaskManager.controller.userController;

import com.cogniwide.TaskManager.DTO.UpdateUserDTO;
import com.cogniwide.TaskManager.ServerResponse;
import com.cogniwide.TaskManager.customException.CustomException;
import com.cogniwide.TaskManager.entity.UserEntity;
import com.cogniwide.TaskManager.service.userServices.UserServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    private UserServices userServices;

    @GetMapping("user-profile")
    public ResponseEntity<ServerResponse<UserEntity>> getProfile() {
        try {
            UserEntity user = userServices.findUser();
            ServerResponse<UserEntity> response = new ServerResponse<>(
                    HttpStatus.OK.value(),
                    user,
                    "Fetched (" + user.getUsername() + ") profile successfully"
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

    @PutMapping("update-user-profile")
    public ResponseEntity<ServerResponse<UserEntity>> updateUserProfile(@RequestBody UpdateUserDTO updateUserDTO) {
        try {
            UserEntity updatedProfile = userServices.updateUserProfile(updateUserDTO);
            ServerResponse<UserEntity> response = new ServerResponse<>(
                    HttpStatus.OK.value(),
                    updatedProfile,
                    "User profile updated successfully"
            );
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
        }
    }

}
