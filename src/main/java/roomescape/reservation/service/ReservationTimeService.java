package roomescape.reservation.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.error.ReferDataDeleteException;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.ReservationTimeResponse;
import roomescape.reservation.dto.ReservationTimeSaveRequest;
import roomescape.reservation.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {
    private final ReservationTimeRepository reservationTimeRepository;

    public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository) {
        this.reservationTimeRepository = reservationTimeRepository;
    }

    public ReservationTimeResponse save(final ReservationTimeSaveRequest reservationTimeSaveRequest) {
        ReservationTime reservationTime = reservationTimeRepository.save(reservationTimeSaveRequest.toTime());
        return ReservationTimeResponse.toResponse(reservationTime);
    }

    public ReservationTimeResponse findById(final Long id) {
        ReservationTime reservationTime = reservationTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 시간이 없습니다."));
        return ReservationTimeResponse.toResponse(reservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponse::toResponse)
                .toList();
    }

    public void deleteById(final Long id) {
        findById(id);
        if (reservationTimeRepository.findBySameReferId(id).isPresent()) {
            throw new ReferDataDeleteException("해당 시간으로 예약한 정보가 존재합니다.");
        }
        reservationTimeRepository.deleteById(id);
    }
}
