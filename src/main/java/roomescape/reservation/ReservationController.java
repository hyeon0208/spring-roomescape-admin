package roomescape.reservation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservationController {

    @GetMapping("/admin/reservation")
    public String reservationHome() {
        return "admin/reservation-legacy";
    }
}