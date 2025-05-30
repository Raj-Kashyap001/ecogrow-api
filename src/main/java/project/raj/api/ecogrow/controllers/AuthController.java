package project.raj.api.ecogrow.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.raj.api.ecogrow.models.Role;
import project.raj.api.ecogrow.models.User;
import project.raj.api.ecogrow.repository.RoleRepository;
import project.raj.api.ecogrow.repository.UserRepository;
import project.raj.api.ecogrow.requests.LoginRequest;
import project.raj.api.ecogrow.requests.SignUpRequest;
import project.raj.api.ecogrow.response.ApiResponse;
import project.raj.api.ecogrow.response.JwtResponse;
import project.raj.api.ecogrow.security.jwt.JwtUtils;
import project.raj.api.ecogrow.security.user.ShopUserDetails;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth") // TODO: Add Oauth2 with google
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtils jwtUtils;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        try {
            Authentication auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwt = jwtUtils.generateTokenForUser(auth);
            ShopUserDetails userDetails = (ShopUserDetails) auth.getPrincipal();
            JwtResponse response = new JwtResponse(userDetails.getId(), jwt);
            return ResponseEntity.ok(new ApiResponse("Login Successful", response));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(UNAUTHORIZED)
                    .body(new ApiResponse("Invalid Credentials", null));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signupRequest) {
        try {
            // Check if email already exists
            if (userRepository.existsByEmail(signupRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new ApiResponse("Error: Email is already in use!", null));
            }

            // Create a new user account
            User user = new User();
            user.setEmail(signupRequest.getEmail());
            user.setFirstName(signupRequest.getFirstName());
            user.setLastName(signupRequest.getLastName());
            user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

            // Set default role (e.g., ROLE_USER)
            Set<Role> roles = new HashSet<>();
            Role userRole = roleRepository.getByName("ROLE_USER");
            roles.add(userRole);
            user.setRoles(roles);
            userRepository.save(user);

            return ResponseEntity.ok(new ApiResponse("User registered successfully!", null));
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(new ApiResponse("An error occurred during registration: " + e.getMessage(), null));
        }
    }

}
