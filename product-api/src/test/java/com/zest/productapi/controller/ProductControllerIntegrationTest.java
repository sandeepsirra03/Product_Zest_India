package com.zest.productapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zest.productapi.dto.ItemRequest;
import com.zest.productapi.dto.ProductRequest;
import com.zest.productapi.entity.Role;
import com.zest.productapi.entity.User;
import com.zest.productapi.repository.ProductRepository;
import com.zest.productapi.repository.UserRepository;
import com.zest.productapi.security.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ProductRepository productRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private JwtService jwtService;

        private String adminToken;
        private String userToken;

        @BeforeEach
        void setUp() {
                User admin = User.builder()
                                .email("admin@test.com")
                                .password(passwordEncoder.encode("password"))
                                .role(Role.ADMIN)
                                .build();
                userRepository.save(admin);
                adminToken = "Bearer " + jwtService.generateToken(admin);

                User user = User.builder()
                                .email("user@test.com")
                                .password(passwordEncoder.encode("password"))
                                .role(Role.USER)
                                .build();
                userRepository.save(user);
                userToken = "Bearer " + jwtService.generateToken(user);
        }

        @AfterEach
        void tearDown() {
                productRepository.deleteAll();
                userRepository.deleteAll();
        }

        @Test
        void createProduct_WithUserToken_ShouldCreateAndReturnProduct() throws Exception {
                ProductRequest request = ProductRequest.builder()
                                .productName("Integration Test Product")
                                .createdBy("tester")
                                .items(List.of(new ItemRequest(5)))
                                .build();

                mockMvc.perform(post("/api/v1/products")
                                .header("Authorization", userToken)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.productName").value("Integration Test Product"));
        }

        @Test
        void createProduct_WithoutToken_ShouldReturnForbidden() throws Exception {
                ProductRequest request = ProductRequest.builder()
                                .productName("Integration Test Product")
                                .createdBy("tester")
                                .items(List.of(new ItemRequest(5)))
                                .build();

                mockMvc.perform(post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isForbidden());
        }

        @Test
        void getAllProducts_WithUserToken_ShouldReturnPage() throws Exception {
                mockMvc.perform(get("/api/v1/products?page=0&size=10")
                                .header("Authorization", userToken))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray());
        }
}
