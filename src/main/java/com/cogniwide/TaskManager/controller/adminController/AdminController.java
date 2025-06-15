package com.cogniwide.TaskManager.controller.adminController;

import com.cogniwide.TaskManager.ServerResponse;
import com.cogniwide.TaskManager.customException.CustomException;
import com.cogniwide.TaskManager.entity.UserEntity;
import com.cogniwide.TaskManager.service.adminService.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("users")
    public ResponseEntity<ServerResponse<?>> users() {
        try {
            List<UserEntity> users = adminService.findAllUsersExcludingAdmins();

            if (users.isEmpty()) {
                ServerResponse<int[]> response = new ServerResponse<>(
                        HttpStatus.OK.value(),
                        new int[]{},
                        "No users found"
                );
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            ServerResponse<List<UserEntity>> response = new ServerResponse<>(
                    HttpStatus.OK.value(),
                    users,
                    "Admin fetched all users"
            );
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while fetching users");
        }
    }

    @DeleteMapping("users/{userId}")
    public ResponseEntity<ServerResponse<?>> deleteUser(@PathVariable(required = false) Integer userId) {
        try {
            adminService.deleteUser(userId);

            ServerResponse<String> response = new ServerResponse<>(
                    HttpStatus.OK.value(),
                    "User ID: " + userId,
                    "Deleted user: " + userId + " from the database"
            );
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong while deleting the user");
        }
    }
}
