package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.repository.ReservationTimeRepository;

@DisplayName("시간 레포지토리")
@JdbcTest
@Import({ReservationTimeRepository.class, ReservationRepository.class})
class ReservationReservationTimeRepositoryTest {
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private ReservationTime savedReservationTime;

    @BeforeEach
    void init() {
        ReservationTime reservationTime = new ReservationTime(null, LocalTime.parse("10:00"));
        savedReservationTime = reservationTimeRepository.save(reservationTime);
    }

    @DisplayName("id로 시간 정보를 조회한다.")
    @Test
    void findById() {
        // given
        Optional<ReservationTime> findTime = reservationTimeRepository.findById(savedReservationTime.getId());

        // when
        ReservationTime reservationTime = findTime.get();

        // then
        assertAll(
                () -> assertThat(savedReservationTime.getId()).isEqualTo(reservationTime.getId()),
                () -> assertThat(savedReservationTime.getStartAt()).isEqualTo(reservationTime.getStartAt())
        );
    }

    @DisplayName("모든 시간 정보를 조회한다.")
    @Test
    void findAll() {
        // given & when
        List<ReservationTime> reservations = reservationTimeRepository.findAll();

        // then
        assertThat(reservations).hasSize(1);
    }

    @DisplayName("time 기본키를 참조하는 예약이 있는지 조회한다.")
    @Test
    void test() {
        // given
        Reservation reservation = new Reservation(
                null,
                new Name("브라운"), LocalDate.parse("2024-08-05"),
                new ReservationTime(savedReservationTime.getId(), LocalTime.parse("10:00"))
        );
        reservationRepository.save(reservation);

        ReservationTime savedReservationTime2 = reservationTimeRepository.save(new ReservationTime(null, LocalTime.parse("11:00")));

        // when
        Optional<ReservationTime> time1 = reservationTimeRepository.findBySameReferId(savedReservationTime.getId());
        Optional<ReservationTime> time2 = reservationTimeRepository.findBySameReferId(savedReservationTime2.getId());

        // then
        assertAll(
                () -> assertThat(time1.isPresent()).isTrue(),
                () -> assertThat(time2.isPresent()).isFalse()
        );
    }

    @DisplayName("id로 시간 정보를 제거한다.")
    @Test
    void delete() {
        // given & when
        reservationTimeRepository.deleteById(savedReservationTime.getId());

        // then
        assertThat(reservationTimeRepository.findById(savedReservationTime.getId()).isEmpty()).isTrue();
    }
}
