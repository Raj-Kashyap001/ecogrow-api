package project.raj.api.ecogrow.services.user;

import project.raj.api.ecogrow.dto.UserDto;
import project.raj.api.ecogrow.models.User;
import project.raj.api.ecogrow.requests.UserCreateRequest;
import project.raj.api.ecogrow.requests.UserUpdateRequest;

public interface IUserService {
    UserDto getUserById(Long userId);
    UserDto createUser(UserCreateRequest request);
    UserDto updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    User getAuthenticatedUser();
}
