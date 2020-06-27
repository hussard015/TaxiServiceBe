package com.dramancompany.taxiServiceBe.user.controller;

import com.dramancompany.taxiServiceBe.IntegrationTest;
import com.dramancompany.taxiServiceBe.user.domain.User;
import com.dramancompany.taxiServiceBe.user.dto.UserDto;
import com.dramancompany.taxiServiceBe.user.repository.UserRepository;
import com.dramancompany.taxiServiceBe.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.dramancompany.taxiServiceBe.restDoc.ApiDocumentUtils.getDocumentRequest;
import static com.dramancompany.taxiServiceBe.restDoc.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends IntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        userRepository.deleteAll();
    }

    @Test
    public void 회원가입() throws Exception {
        // given
        String userSignUpReq = readJson("classpath:user/UserSignUpReq.json");

        // when
        mvc.perform(post("/api/v1/pub/signUp")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(userSignUpReq)
        )
        // then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-signUp", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                getUserSignUpReqField()
                        ),
                        responseFields(
                                getUserSignUpResField()
                        )
                ))
                .andDo(print());
    }

    @Test
    public void 로그인() throws Exception {
        // given
        // 회원가입을 시켜 놓는다.
        UserDto.SignUpReq UserSignUpReq = objectMapper.readValue(readJson("classpath:user/UserSignUpReq.json"), UserDto.SignUpReq.class);
        userService.singUp(UserSignUpReq);

        String UserSignInReq = readJson("classpath:user/UserSignInReq.json");

        // when
        mvc.perform(post("/api/v1/pub/signIn")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(UserSignInReq)
        )
                // then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("user-signIn", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                getUserSignInReqField()
                        ),
                        responseFields(
                                getUserSignInResField()
                        )
                ))
                .andDo(print());
    }

    private List<FieldDescriptor> getUserSignUpReqField() {
        return Arrays.asList(
                fieldWithPath("username").type(JsonFieldType.STRING).description("유저 아이디 (이메일 형식)"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드"),
                fieldWithPath("userType").type(JsonFieldType.NUMBER).description("유저 타입 : " + User.UserType.toText()));
    }

    private List<FieldDescriptor> getUserSignUpResField() {
        return Arrays.asList(
                fieldWithPath("username").type(JsonFieldType.STRING).description("유저 아이디 (이메일 형식)"),
                fieldWithPath("userType").type(JsonFieldType.NUMBER).description("유저 타입 : " + User.UserType.toText()));
    }

    private List<FieldDescriptor> getUserSignInReqField() {
        return Arrays.asList(
                fieldWithPath("username").type(JsonFieldType.STRING).description("유저 아이디 (이메일 형식)"),
                fieldWithPath("password").type(JsonFieldType.STRING).description("패스워드"));
    }

    private List<FieldDescriptor> getUserSignInResField() {
        return Arrays.asList(
                fieldWithPath("username").type(JsonFieldType.STRING).description("유저 아이디 (이메일 형식)"),
                fieldWithPath("userType").type(JsonFieldType.NUMBER).description("유저 타입 : " + User.UserType.toText()),
                fieldWithPath("token").type(JsonFieldType.STRING).description("유저 JWT 토큰"));
    }
}