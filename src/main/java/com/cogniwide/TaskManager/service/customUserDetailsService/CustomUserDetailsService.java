package com.cogniwide.TaskManager.service.customUserDetailsService;
import com.cogniwide.TaskManager.customException.CustomException;
import com.cogniwide.TaskManager.repositopry.userRepository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new CustomException(HttpStatus.UNAUTHORIZED,"User is not registered with this email: " + email)
        );
    }
}
