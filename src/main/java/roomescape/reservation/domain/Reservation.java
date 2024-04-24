package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.time.domain.Time;

public class Reservation {
    private Long id;
    private Name name;
    private LocalDate date;
    private Time time;

    public Reservation() {
    }

    public Reservation(final Long id, final Name name, final LocalDate date, final Time time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Reservation(final Name name, final LocalDate date, final Time time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }
}
