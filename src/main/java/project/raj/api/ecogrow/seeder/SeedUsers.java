package project.raj.api.ecogrow.seeder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project.raj.api.ecogrow.models.Role;
import project.raj.api.ecogrow.models.User;
import project.raj.api.ecogrow.repository.RoleRepository;
import project.raj.api.ecogrow.repository.UserRepository;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
@Slf4j
public class SeedUsers implements ApplicationListener<ContextRefreshedEvent> {
    
    private static final int DEFAULT_USER_COUNT = 4;
    private static final String DEFAULT_PASSWORD = "1233";
    private static final String DEFAULT_LAST_NAME = "Demo";
    private static final String EMAIL_PATTERN = "user%d@email.com";
    private static final String ADMIN_EMAIL = "admin@dreamshop.com";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_FIRSTNAME = "Admin";
    private static final String ADMIN_LASTNAME = "User";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultRolesIfNotExist(defaultRoles);
        createDefaultAdminIfNotExist();
        seedDefaultUsersIfNotExist();
    }
    
    @Transactional
    protected void seedDefaultUsersIfNotExist() {
        for (int i = 1; i <= DEFAULT_USER_COUNT; i++) {
            String email = String.format(EMAIL_PATTERN, i);
            
            if (!userRepository.existsByEmail(email)) {
                createAndSaveUser(i, email);
                log.info("User {} created!", i);
            }
        }
    }
    
    @Transactional
    protected void createAndSaveUser(int userNumber, String email) {
        User user = new User();
        Role role = roleRepository.getByName("ROLE_USER");
        user.setFirstName("User" + userNumber);
        user.setLastName(DEFAULT_LAST_NAME);
        user.setEmail(email);
        user.setRoles(Set.of(role));
        user.setPassword(passwordEncoder.encode(DEFAULT_PASSWORD));
        userRepository.save(user);
    }

    @Transactional
    protected void createDefaultAdminIfNotExist() {
        if (!userRepository.existsByEmail(ADMIN_EMAIL)) {
            User adminUser = new User();
            adminUser.setFirstName(ADMIN_FIRSTNAME);
            adminUser.setLastName(ADMIN_LASTNAME);
            adminUser.setEmail(ADMIN_EMAIL);
            adminUser.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            Role adminRole = roleRepository.getByName("ROLE_ADMIN");
            adminUser.setRoles(Set.of(adminRole));
            userRepository.save(adminUser);
            log.info("Default admin user created!");
        }
    }

    @Transactional
    protected void createDefaultRolesIfNotExist(Set<String> roles) {
        Set<String> existingRoleNames = roleRepository.findByNameIn(roles)
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        roles.stream()
                .filter(role -> !existingRoleNames.contains(role))
                .map(Role::new)
                .forEach(roleRepository::save);
    }
}