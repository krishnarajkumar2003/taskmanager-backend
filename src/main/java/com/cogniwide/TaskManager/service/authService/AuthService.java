package com.cogniwide.TaskManager.service.authService;
import com.cogniwide.TaskManager.DTO.UserSignInDTO;
import com.cogniwide.TaskManager.DTO.UserSignUpDTO;
import com.cogniwide.TaskManager.customException.CustomException;
import com.cogniwide.TaskManager.entity.UserEntity;
import com.cogniwide.TaskManager.enums.Roles;
import com.cogniwide.TaskManager.repositopry.userRepository.UserRepository;
import com.cogniwide.TaskManager.service.jwtService.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void registerUser(UserSignUpDTO userSignUpDTO) {
        try {
            if (userSignUpDTO.getEmail() == null || userSignUpDTO.getEmail().isBlank() ||
                    userSignUpDTO.getUsername() == null || userSignUpDTO.getUsername().isBlank() ||
                    userSignUpDTO.getPassword() == null || userSignUpDTO.getPassword().isBlank()) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Missing username, email, or password");
            }

            if (userRepository.findByEmail(userSignUpDTO.getEmail()).isPresent()) {
                throw new CustomException(HttpStatus.CONFLICT, "Email already exists. Please try a different one.");
            }

            if (userRepository.findByUsername(userSignUpDTO.getUsername()).isPresent()) {
                throw new CustomException(HttpStatus.CONFLICT, "Username already exists. Please try a different one.");
            }

            UserEntity user = new UserEntity();
            user.setEmail(userSignUpDTO.getEmail().trim());
            user.setUsername(userSignUpDTO.getUsername().trim());
            user.setPassword(passwordEncoder.encode(userSignUpDTO.getPassword().trim()));
            user.setRole(Roles.ROLE_USER);

            userRepository.save(user);

        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while registering user");
        }
    }
    public String loginUser(UserSignInDTO userSignInDTO) {
        try {
            if (userSignInDTO.getEmail() == null || userSignInDTO.getEmail().isBlank() ||
                    userSignInDTO.getPassword() == null || userSignInDTO.getPassword().isBlank()) {
                throw new CustomException(HttpStatus.BAD_REQUEST, "Email and password must be provided");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userSignInDTO.getEmail(),
                            userSignInDTO.getPassword()
                    )
            );

            if (authentication.isAuthenticated()) {
                return jwtService.generateJwtToken(userSignInDTO.getEmail()) + " "+authentication.getAuthorities().iterator().next().getAuthority();
            } else {
                throw new CustomException(HttpStatus.UNAUTHORIZED, "Authentication failed");
            }

        } catch (BadCredentialsException e) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        } catch (CustomException ce) {
            throw ce;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.INTERNAL_SERVER_ERROR, "Email not registered");
        }
    }
}
