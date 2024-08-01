package com.itm.space.backendresources;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserService userService;

    @Test
    @DisplayName("Need Find User By Id")
    public void findUserByIdTest() {
        UUID uuid = UUID.randomUUID();
        UserResponse userRequest = new UserResponse("Alexey", "Demesh", "aleksej.demesh.98@inbox.ru", Collections.emptyList(), Collections.emptyList());

        when(userService.getUserById(uuid)).thenReturn(userRequest);

        assertEquals("Alexey", userService.getUserById(uuid).getFirstName());

    }

    @Test
    @DisplayName("Check how working creating users")
    public void createUserTest(){
        UserRequest userRequest = new UserRequest("user",
                "aleksej.demesh.98@inbox.ru",
                "user",
                "Alexey",
                "Demesh");
        userService.createUser(userRequest);

        verify(userService,times(1)).createUser(userRequest);
    }
}
