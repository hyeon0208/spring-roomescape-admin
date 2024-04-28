package roomescape.reservation.repository;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.domain.ReservationTime;

@Repository
public class ReservationTimeRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservationTimeRepository(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ReservationTime save(final ReservationTime reservationTime) {
        LocalTime startAt = reservationTime.getStartAt();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into reservation_time(start_at) values (?)", new String[]{"id"});
            ps.setString(1, startAt.toString());
            return ps;
        }, keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return new ReservationTime(id, startAt);
    }

    public Optional<ReservationTime> findById(final Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from reservation_time where id = ?",
                    createTimeRowMapper(), id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<ReservationTime> findBySameReferId(final Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("""
                    select t.id, t.start_at from reservation_time t
                    inner join reservation r
                    on t.id = r.time_id 
                    where t.id = ?;
                    """, createTimeRowMapper(), id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<ReservationTime> findAll() {
        return jdbcTemplate.query("select * from reservation_time", createTimeRowMapper());
    }

    private RowMapper<ReservationTime> createTimeRowMapper() {
        return (resultSet, rowNum) -> new ReservationTime(
                resultSet.getLong("id"),
                resultSet.getTime("start_at").toLocalTime()
        );
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from reservation_time where id = ?", id);
    }
}
