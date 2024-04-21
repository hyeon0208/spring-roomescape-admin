package roomescape.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import roomescape.time.Time;

@DisplayName("예약 레포지토리")
@JdbcTest
@Sql(scripts = "classpath:reservation-test.sql")
@Import(ReservationRepository.class)
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @TestFactory
    Stream<DynamicTest> reservationRepositoryDynamicTests() {
        return Stream.of(
                DynamicTest.dynamicTest("예약 정보를 저장한다.", () -> {
                    // given
                    ReservationRequest reservationRequest = new ReservationRequest("브라운", "2024-08-05", 1L);

                    // when
                    Reservation reservation = reservationRepository.save(reservationRequest, new Time(1L, "10:00"));

                    // then
                    assertThat(reservation.getId()).isEqualTo(2L);
                }),
                DynamicTest.dynamicTest("id로 예약 정보를 조회한다.", () -> {
                    // given
                    Optional<Reservation> reservation = reservationRepository.findById(1L);

                    // then
                    assertAll(
                            () -> assertThat(reservation.get().getId()).isEqualTo(1L),
                            () -> assertThat(reservation.get().getName()).isEqualTo("브라운"),
                            () -> assertThat(reservation.get().getDate()).isEqualTo("2024-08-05"),
                            () -> assertThat(reservation.get().getTime()).isEqualTo(new Time(1L, "10:00"))
                    );
                }),
                DynamicTest.dynamicTest("모든 예약 정보를 조회한다.", () -> {
                    // given & when
                    List<Reservation> reservations = reservationRepository.findAll();

                    // then
                    assertThat(reservations).hasSize(2);
                }),
                DynamicTest.dynamicTest("id로 예약 정보를 제거한다.", () -> {
                    // given
                    Long id = 2L;

                    // when
                    reservationRepository.deleteById(id);

                    // then
                    assertThat(reservationRepository.findById(id).isEmpty()).isTrue();
                })
        );
    }
}
