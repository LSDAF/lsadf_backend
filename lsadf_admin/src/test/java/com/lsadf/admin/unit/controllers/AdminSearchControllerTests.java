package com.lsadf.admin.unit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsadf.admin.controllers.admin.AdminSearchController;
import com.lsadf.admin.controllers.admin.impl.AdminSearchControllerImpl;
import com.lsadf.core.constants.ControllerConstants;
import com.lsadf.core.controllers.advices.GlobalExceptionHandler;
import com.lsadf.core.requests.common.Filter;
import com.lsadf.core.requests.search.SearchRequest;
import com.lsadf.core.requests.user.UserOrderBy;
import com.lsadf.core.unit.config.UnitTestConfiguration;
import com.lsadf.core.unit.config.WithMockJwtUser;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.lsadf.core.constants.ControllerConstants.Params.ORDER_BY;
import static com.lsadf.core.requests.user.UserOrderBy.ID_DESC;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = {GlobalExceptionHandler.class, AdminSearchController.class, AdminSearchControllerImpl.class})
@Import({UnitTestConfiguration.class,
        GlobalExceptionHandler.class
})
@TestMethodOrder(MethodOrderer.MethodName.class)
@ActiveProfiles("test")
class AdminSearchControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void searchUsers_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void searchUsers_should_return_403_when_user_not_admin() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchUsers_should_return_200_when_authenticated_user_is_admin() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    void searchGameSaves_should_return_401_when_user_not_authenticated() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON")
    void searchGameSaves_should_return_403_when_user_not_admin() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchGameSaves_should_return_200_when_authenticated_user_is_admin() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchUsers_should_return_200_when_no_body() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchGameSaves_should_return_200_when_no_body() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchUsers_should_return_200_when_order_by_is_set() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param(ControllerConstants.Params.ORDER_BY, UserOrderBy.ID_DESC.name()))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchGameSaves_should_return_200_when_order_by_is_set() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param(ControllerConstants.Params.ORDER_BY, UserOrderBy.ID_DESC.name()))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchUsers_should_return_400_when_invalid_order_by() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param(ControllerConstants.Params.ORDER_BY, "INVALID_ORDER_BY"))
                // then
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchGameSaves_should_return_400_when_invalid_order_by() {
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .param(ControllerConstants.Params.ORDER_BY, "INVALID_ORDER_BY"))
                // then
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchUsers_should_return_400_when_null_filter_type() {
        // given
        List<Filter> filters = List.of(new Filter(null, "Test"));
        SearchRequest request = new SearchRequest(filters);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchUsers_should_return_400_when_null_filter_value() {
        // given
        List<Filter> filters = List.of(new Filter("Test", null));
        SearchRequest request = new SearchRequest(filters);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchGameSaves_should_return_400_when_null_filter_type() {
        // given
        List<Filter> filters = List.of(new Filter(null, "Test"));
        SearchRequest request = new SearchRequest(filters);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchGameSaves_should_return_400_when_null_filter_value() {
        // given
        List<Filter> filters = List.of(new Filter("Test", null));
        SearchRequest request = new SearchRequest(filters);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchUsers_should_return_200_when_valid_filters() {
        // given
        List<Filter> filters = List.of(new Filter("Test", "Test"),
                new Filter("Test", "Test"));
        SearchRequest request = new SearchRequest(filters);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @SneakyThrows
    @WithMockJwtUser(username = "paul.ochon@test.com", name = "Paul OCHON", roles = {"ADMIN"})
    void searchGameSaves_should_return_200_when_valid_filters() {
        // given
        List<Filter> filters = List.of(new Filter("Test", "Test"),
                new Filter("Test", "Test"));
        SearchRequest request = new SearchRequest(filters);
        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/admin/search/game_saves")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
