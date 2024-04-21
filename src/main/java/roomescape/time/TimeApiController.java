package roomescape.time;

import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeApiController {
    private final TimeService timeService;

    public TimeApiController(final TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/times")
    public List<Time> findAll() {
        return timeService.findAll();
    }

    @PostMapping("/times")
    public ResponseEntity<Map<String, String>> create(@RequestBody TimeRequest timeRequest) {
        timeService.save(timeRequest);
        return ResponseEntity.ok().body(Map.of("status", "success"));
    }

    @DeleteMapping("/times/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        timeService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}