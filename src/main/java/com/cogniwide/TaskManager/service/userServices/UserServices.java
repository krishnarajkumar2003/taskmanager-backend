package com.cogniwide.TaskManager.service.userServices;

import com.cogniwide.TaskManager.DTO.UpdateUserDTO;
import com.cogniwide.TaskManager.customException.CustomException;
import com.cogniwide.TaskManager.entity.UserEntity;
import com.cogniwide.TaskManager.repositopry.userRepository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    private UserEntity getAuthenticatedUser() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "User not found"));
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Authentication failed");
        }
    }

    public UserEntity findUser() {
        try {
            return getAuthenticatedUser();
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while fetching user");
        }
    }

    public UserEntity updateUserProfile(UpdateUserDTO updateUserDTO) {
        try {
            UserEntity user = getAuthenticatedUser();

            String newEmail = updateUserDTO.getEmail();
            String newUsername = updateUserDTO.getUsername();

            boolean isEmailChanged = newEmail != null && !newEmail.isBlank() && !newEmail.equals(user.getEmail());
            boolean isUsernameChanged = newUsername != null && !newUsername.isBlank() && !newUsername.equals(user.getUsername());

            if (!isEmailChanged && !isUsernameChanged) {
                return user; // No changes, return existing user
            }

            if (isEmailChanged) {
                if (!newEmail.toLowerCase().endsWith("@gmail.com")) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Email must end with @gmail.com");
                }

                if (userRepository.findByEmail(newEmail).isPresent()) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Email is already in use");
                }

                user.setEmail(newEmail);
            }

            if (isUsernameChanged) {
                if (userRepository.findByUsername(newUsername).isPresent()) {
                    throw new CustomException(HttpStatus.BAD_REQUEST, "Username is already in use");
                }

                user.setUsername(newUsername);
            }

            return userRepository.save(user);

        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while updating profile");
        }
    }



}
