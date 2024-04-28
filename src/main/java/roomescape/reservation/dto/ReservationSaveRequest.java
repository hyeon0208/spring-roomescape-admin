package roomescape.reservation.dto;

import java.time.LocalDate;
import roomescape.reservation.domain.Name;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;

public class ReservationSaveRequest {
    private String name;
    private LocalDate date;
    private Long timeId;

    public ReservationSaveRequest() {
    }

    public ReservationSaveRequest(final String name, final LocalDate date, final Long timeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
    }

    public Reservation toReservation(final ReservationTime reservationTime) {
        return new Reservation(null, new Name(name), date, reservationTime);
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }
}
