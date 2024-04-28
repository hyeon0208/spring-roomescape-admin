package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.Objects;
import roomescape.reservation.domain.ReservationTime;

public class ReservationTimeResponse {
    private Long id;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startAt;

    public ReservationTimeResponse() {
    }

    public ReservationTimeResponse(final Long id, final LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTimeResponse toResponse(final ReservationTime reservationTime) {
        return new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt());
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTimeResponse reservationTimeResponse = (ReservationTimeResponse) o;
        return Objects.equals(id, reservationTimeResponse.id) && Objects.equals(startAt, reservationTimeResponse.startAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startAt);
    }
}
