package com.itm.space.backendresources;


import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import com.itm.space.backendresources.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIntegrationTest extends BaseIntegrationTest {

    @MockBean
    private UserService userService;

    @MockBean
    private Keycloak keycloak;

    private final static String URL = "http://backend-gateway-client:9090/api/users";

    @BeforeEach
    void setUp() {
        when(keycloak.realm(anyString())).thenReturn(mock(RealmResource.class));
        when(keycloak.realm(anyString()).users()).thenReturn(mock(UsersResource.class));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldBeReturnStatusOk_FindById() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserResponse userResponse = new UserResponse("Alexey", "Demesh", "aleksej.demesh.98@inbox.ru", Collections.emptyList(), Collections.emptyList());

        when(userService.getUserById(uuid)).thenReturn(userResponse);
        mvc.perform(get("http://backend-gateway-client:9090/api/users/{id}", uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alexey"))
                .andExpect(jsonPath("$.lastName").value("Demesh"))
                .andExpect(jsonPath("$.email").value("aleksej.demesh.98@inbox.ru"));
    }

    @Test
    @WithMockUser(roles = "MODERATOR")
    public void shouldBeReturnStatusOK_CreateUser() throws Exception {
        UserRequest userRequest = new UserRequest("user", "aleksej.demesh.98@inbox.ru",
                "user", "Alexey", "Demesh");


        Response response = Response.status(Response.Status.CREATED).location(new URI("user_id")).build();
        when(keycloak.realm(anyString()).users().create(any())).thenReturn(response);

        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isOk());
        verify(keycloak.realm(anyString()).users(), times(0)).create(any());


    }
}
