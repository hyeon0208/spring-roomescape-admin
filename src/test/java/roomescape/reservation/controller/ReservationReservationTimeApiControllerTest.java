package roomescape.reservation.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.reservation.controller.ReservationTimeApiController;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.ReservationTimeResponse;
import roomescape.reservation.dto.ReservationTimeSaveRequest;
import roomescape.reservation.service.ReservationTimeService;

@DisplayName("시간 API 컨트롤러")
@WebMvcTest(ReservationTimeApiController.class)
class ReservationReservationTimeApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationTimeService reservationTimeService;

    @DisplayName("모든 시간 조회 성공 시 200 응답을 받는다.")
    @Test
    public void findAllTest() throws Exception {
        // given
        ReservationTime reservationTime1 = new ReservationTime(1L, LocalTime.parse("10:00"));
        ReservationTime reservationTime2 = new ReservationTime(2L, LocalTime.parse("10:00"));
        List<ReservationTime> reservationTimes = List.of(reservationTime1, reservationTime2);

        // when
        doReturn(reservationTimes).when(reservationTimeService).findAll();

        // then
        mockMvc.perform(get("/times")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(reservationTimes.size())));
    }

    @DisplayName("시간 저장 테스트")
    @Nested
    class saveTest {
        @Autowired
        private ObjectMapper objectMapper;

        @DisplayName("시간 정보를 저장 성공 시 201 응답을 받는다.")
        @Test
        public void createSuccessTest() throws Exception {
            // given
            ReservationTimeSaveRequest reservationTimeSaveRequest = new ReservationTimeSaveRequest(LocalTime.parse("10:00"));
            ReservationTimeResponse time = new ReservationTimeResponse(1L, reservationTimeSaveRequest.getStartAt());

            // when
            doReturn(time).when(reservationTimeService)
                    .save(any(ReservationTimeSaveRequest.class));

            // then
            mockMvc.perform(post("/times")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reservationTimeSaveRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(time.getId()))
                    .andExpect(jsonPath("$.startAt").value(time.getStartAt().toString()));
        }

        @DisplayName("시간 저장 실패 시 400 응답을 받는다.")
        @Test
        public void createExceptionTest() throws Exception {
            // given
            ReservationTimeSaveRequest reservationTimeSaveRequest = new ReservationTimeSaveRequest(null);

            // when
            doThrow(new DataAccessException("데이터 접근 예외") {}).when(reservationTimeService)
                    .save(any(ReservationTimeSaveRequest.class));

            // then
            mockMvc.perform(post("/times")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reservationTimeSaveRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @DisplayName("시간 삭제 테스트")
    @Nested
    class deleteTest {

        @DisplayName("시간 삭제 성공시 204 응답을 받는다.")
        @Test
        public void deleteByIdSuccessTest() throws Exception {
            // given && when
            doThrow(IllegalArgumentException.class).when(reservationTimeService)
                    .deleteById(2L);

            // then
            mockMvc.perform(delete("/times/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @DisplayName("예약 정보 삭제 실패시 400 응답을 받는다.")
        @Test
        public void deleteByIdExceptionTest() throws Exception {
            // given
            Long id = 1L;

            // when
            doThrow(IllegalArgumentException.class).when(reservationTimeService)
                    .deleteById(id);

            // then
            mockMvc.perform(delete("/times/{id}", id)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }
}
