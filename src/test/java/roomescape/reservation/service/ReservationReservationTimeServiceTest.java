package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.error.ReferDataDeleteException;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationTimeRepository;
import roomescape.reservation.service.ReservationTimeService;

@DisplayName("시간 서비스")
@ExtendWith(MockitoExtension.class)
class ReservationReservationTimeServiceTest {
    @Mock
    private ReservationTimeRepository reservationTimeRepository;

    @InjectMocks
    private ReservationTimeService reservationTimeService;

    @DisplayName("존재하지 않는 id일 경우 예외가 발생한다.")
    @Test
    void findByIdExceptionTest() {
        // given
        Long id = 1L;

        doReturn(Optional.empty()).when(reservationTimeRepository)
                .findById(id);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.findById(id))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("time id를 참조하고 있는 reservation이 있을 때, 해당 time을 삭제하면 ReferDataDeleteException 예외가 발생한다.")
    @Test
    void deleteByIdExceptionTest() {
        // given
        Long id = 1L;
        ReservationTime reservationTime = new ReservationTime(id, LocalTime.parse("10:00"));

        doReturn(Optional.of(reservationTime)).when(reservationTimeRepository)
                .findById(id);

        doReturn(Optional.of(reservationTime)).when(reservationTimeRepository)
                .findBySameReferId(id);

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(id))
                .isInstanceOf(ReferDataDeleteException.class);
    }
}
