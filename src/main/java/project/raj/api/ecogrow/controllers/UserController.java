package project.raj.api.ecogrow.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.raj.api.ecogrow.dto.UserDto;
import project.raj.api.ecogrow.exceptions.ResourceNotFoundException;
import project.raj.api.ecogrow.requests.UserCreateRequest;
import project.raj.api.ecogrow.requests.UserUpdateRequest;
import project.raj.api.ecogrow.response.ApiResponse;
import project.raj.api.ecogrow.services.user.IUserService;

import static org.springframework.http.HttpStatus.NOT_FOUND;


@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        try {
            UserDto user = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("User found", user));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("User not found", null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createUser(@RequestBody UserCreateRequest request) {
        UserDto createdUser = userService.createUser(request);
        return ResponseEntity.ok(new ApiResponse("User created successfully", createdUser));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId,
                                                  @RequestBody UserUpdateRequest request) {
        try {
            UserDto updatedUser = userService.updateUser(request, userId);
            return ResponseEntity.ok(new ApiResponse("User updated successfully", updatedUser));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("User not found", null));
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiResponse("User deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse("User not found", null));
        }
    }
}
