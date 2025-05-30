package project.raj.api.ecogrow.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.raj.api.ecogrow.models.User;
import project.raj.api.ecogrow.repository.UserRepository;

import java.util.Optional;

// TODO: Add service to rate limit login attempts to prevent brute force attacks.(_

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = Optional.of(userRepository.findByEmail(username))
                .orElseThrow(() ->new UsernameNotFoundException("User not found with email!"));
        return ShopUserDetails.buildUserDetails(user);
    }
}
