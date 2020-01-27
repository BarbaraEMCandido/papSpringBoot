package com.example.project.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import lombok.AllArgsConstructor;
import lombok.Data;

import static org.junit.Assert.assertNotNull;

import com.example.project.domain.dto.request.UserCreateRequest;
import com.example.project.domain.dto.request.UserCreateRequestTest;
import com.example.project.service.SiteUserService;
import com.example.project.utils.IntegrationTestConfig;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * BaseSecurityTest
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = IntegrationTestConfig.appProperties)
@ActiveProfiles("test")
public class BaseSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private SiteUserService service;

    @Test
    public void should_PublicAcessSwagger() throws Exception {
        // when + then
        mockMvc.perform(MockMvcRequestBuilders.get("/swagger-ui.html")) //
                .andDo(MockMvcResultHandlers.print()) // pega resultado
                .andExpect(MockMvcResultMatchers.status().isOk()); // faz a validação.
    }

    @Test
    public void should_generateToken_forValidUser() throws Exception {

        UserCreateRequest usr = UserCreateRequestTest.usrValidEmail2;
        OauthLoginData loginInfo = new OauthLoginData("password", usr.getEmail(), usr.getPassword());

        // given
        this.service.createUser(usr.getEmail(), usr.getPassword(), usr.getIsAdmin());

        // when + then
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/oauth/token")//
                .contentType(MediaType.APPLICATION_JSON) //
                .content(mapper.writeValueAsString(loginInfo))) // Executa
                .andDo(MockMvcResultHandlers.print()) // pega resultado
                .andExpect(MockMvcResultMatchers.status().isOk()) // faz a validação.
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.jsonPath("$.acess_token").exists()) //
                .andExpect(MockMvcResultMatchers.jsonPath("$.token_type").exists()) //
                .andReturn();

        OauthResponseData response = mapper.readValue(result.getResponse().getContentAsString(),
                OauthResponseData.class);

        assertNotNull(response);
    
    }

    @Data
    @AllArgsConstructor
    private class OauthLoginData {

        private String grant_type;
        private String username;
        private String password;
    }

    @Data
    private class OauthResponseData {

        private String grant_type;
        private String username;
        private String password;
    }
}