package roomescape.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import roomescape.reservation.domain.ReservationTime;

public class ReservationTimeSaveRequest {
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startAt;

    public ReservationTimeSaveRequest() {
    }

    public ReservationTimeSaveRequest(final LocalTime startAt) {
        this.startAt = startAt;
    }

    public ReservationTime toTime() {
        return new ReservationTime(null, startAt);
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
