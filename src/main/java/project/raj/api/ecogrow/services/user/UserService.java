package project.raj.api.ecogrow.services.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.raj.api.ecogrow.dto.UserDto;
import project.raj.api.ecogrow.exceptions.AlreadyExistsException;
import project.raj.api.ecogrow.exceptions.ResourceNotFoundException;
import project.raj.api.ecogrow.models.User;
import project.raj.api.ecogrow.repository.UserRepository;
import project.raj.api.ecogrow.requests.UserCreateRequest;
import project.raj.api.ecogrow.requests.UserUpdateRequest;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id: "+userId));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto createUser(UserCreateRequest request) {
        return Optional.of(request)
                .filter(user -> userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(req.getEmail());
                    user.setPassword(passwordEncoder.encode(req.getPassword()));
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    User savedUser = userRepository.save(user);
                    return modelMapper.map(savedUser, UserDto.class);
                }).orElseThrow(()-> new AlreadyExistsException("User Already Exists with email: "+request.getEmail()));
    }

    @Override
    public UserDto updateUser(UserUpdateRequest request, Long userId) {
         return userRepository.findById(userId).map(existingUser -> {
           existingUser.setFirstName(request.getFirstName());
           existingUser.setLastName(request.getLastName());
          User updatedUser = userRepository.save(existingUser);
          return modelMapper.map(updatedUser, UserDto.class);
        }).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFoundException("User not found!");
                });
    }

    @Override
    public User getAuthenticatedUser() throws AuthenticationException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new AuthenticationException("User not authenticated. Please login again.") {};
        }
        return user;
    }
}
