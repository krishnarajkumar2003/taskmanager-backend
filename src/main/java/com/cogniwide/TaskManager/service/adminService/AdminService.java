package com.cogniwide.TaskManager.service.adminService;
import com.cogniwide.TaskManager.customException.CustomException;
import com.cogniwide.TaskManager.entity.UserEntity;
import com.cogniwide.TaskManager.enums.Roles;
import com.cogniwide.TaskManager.repositopry.userRepository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private final UserRepository adminRepository;

    public AdminService(UserRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public List<UserEntity> findAllUsersExcludingAdmins() {
        try {
            List<UserEntity> allUsers = adminRepository.findAll();
            return allUsers.stream()
                    .filter(user -> user.getRole() != Roles.ROLE_ADMIN)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching users from database");
        }
    }

    public void deleteUser(Integer userId) {
        try {
            UserEntity user = adminRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "User not found to delete"));
            if (user.getRole() == Roles.ROLE_ADMIN && user.getUsername().equalsIgnoreCase("ADMIN")) {
                throw new CustomException(HttpStatus.FORBIDDEN, "Cannot delete default admin user");
            }
            adminRepository.deleteById(userId);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            throw new CustomException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong while deleting the user"
            );
        }
    }
}
