package com.dramancompany.taxiServiceBe.assignment.controller;

import com.dramancompany.taxiServiceBe.ControllerTest;
import com.dramancompany.taxiServiceBe.assignment.domain.Assignment;
import com.dramancompany.taxiServiceBe.assignment.dto.AssignmentDto;
import com.dramancompany.taxiServiceBe.assignment.service.AssignmentService;
import com.dramancompany.taxiServiceBe.user.domain.User;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

import static com.dramancompany.taxiServiceBe.restDoc.ApiDocumentUtils.getDocumentRequest;
import static com.dramancompany.taxiServiceBe.restDoc.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssignmentController.class)
public class AssignmentControllerTest extends ControllerTest {

    @MockBean
    private AssignmentService assignmentService;


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void 차량배차요청() throws Exception {
        // given
        createPassenger();
        String assignmentReq = readJson("classpath:assignment/AssignmentReq.json");
        AssignmentDto.Res assignmentRes = objectMapper.readValue(readJson("classpath:assignment/AssignmentRes.json"), AssignmentDto.Res.class);
        given(assignmentService.requestAssignment(any(), any())).willReturn(assignmentRes);

        // when
        mvc.perform(post("/api/v1/assignment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(assignmentReq)
        )
                // then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("assignment-request", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                getAssignmentReqField()
                        ),
                        responseFields(
                                getAssignmentResField()
                        )
                ))
                .andDo(print());
    }

    @Test
    public void 차량배차완료() throws Exception {
        // given
        createDriver();
        Long assignmentId = 1L;
        AssignmentDto.Res assignmentRes = objectMapper.readValue(readJson("classpath:assignment/AssignmentCompleteRes.json"), AssignmentDto.Res.class);
        given(assignmentService.catchAssignment(any(), anyLong())).willReturn(assignmentRes);

        mvc.perform(post("/api/v1/assignment/driver/{assignmentId}", assignmentId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("assignment-complete", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("assignmentId").description("배차 요청 고유 아이디")
                        ),
                        responseFields(
                                getAssignmentResField()
                        )
                ))
                .andDo(print());
    }

    @Test
    public void 차량배차목록조회() throws Exception {
        // given
        createPassenger();
        List<AssignmentDto.Res> assignmentResList = objectMapper.readValue(readJson("classpath:assignment/AssignmentResList.json"),
                new TypeReference<List<AssignmentDto.Res>>() {
                });
        given(assignmentService.getAll()).willReturn(assignmentResList);

        mvc.perform(get("/api/v1/assignment")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                // then
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("assignment-list", // (4)
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[]").description("배차 목록 조회")
                        )
                                .andWithPrefix("[].",
                                        getAssignmentResField()
                                )
                        )
                ).andDo(print());

    }

    private void createPassenger() {
        User passenger = User.builder()
                .id(1L)
                .username("je.chang@gmail.com")
                .userType(User.UserType.Passenger)
                .build();

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(passenger, null, null));
    }

    private void createDriver() {
        User drvier = User.builder()
                .id(2L)
                .username("je.chang1@gmail.com")
                .userType(User.UserType.Driver)
                .build();

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(drvier, null, null));
    }

    private List<FieldDescriptor> getAssignmentReqField() {
        return Arrays.asList(
                fieldWithPath("address").type(JsonFieldType.STRING).description("주소 (100자 이하)")
        );
    }

    private List<FieldDescriptor> getAssignmentResField() {
        return Arrays.asList(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("차량 배차 고유 아이디"),
                fieldWithPath("passengerId").type(JsonFieldType.NUMBER).description("배차 요청자 고유 아이디"),
                fieldWithPath("driverId").type(JsonFieldType.NUMBER).description("드라이버 고유 아이디").optional(),
                fieldWithPath("address").type(JsonFieldType.STRING).description("주소 (100자 이하)"),
                fieldWithPath("status").type(JsonFieldType.NUMBER).description("배차 상태 : " + Assignment.Status.toText()),
                fieldWithPath("requestDt").type(JsonFieldType.STRING).description("요청 일시"),
                fieldWithPath("completeDt").type(JsonFieldType.STRING).description("배차 완료 일시").optional()
        );
    }

}