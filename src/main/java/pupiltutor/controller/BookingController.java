package pupiltutor.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pupiltutor.model.TimeSlots;
import pupiltutor.model.Booker;
import pupiltutor.service.BookerClient;

import java.util.List;

@RestController
@CrossOrigin
public class BookingController {

    private static final String TUTORKEY = "TUTOR_KEY_1234567890_SECRET";
    private static final String PUPILKEY = "PUPIL_KEY_1234567890_SECRET";

    private final BookerClient bookerClient;

    public BookingController(BookerClient bookerClient) {
        this.bookerClient = bookerClient;
    }

    private String checkRole(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String roleKey = authHeader.substring(7);
        if (roleKey.equals(TUTORKEY)){
            return "TUTOR";
        }
        if (roleKey.equals(PUPILKEY)) {
            return "PUPIL";
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    private void requireTutor(String authHeader) {
        if (!"TUTOR".equals(checkRole(authHeader))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    private void requirePupil(String authHeader) {
        if (!"PUPIL".equals(checkRole(authHeader))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<List<TimeSlots>> getAvailability(@RequestParam String day) {
        List<TimeSlots> slots = bookerClient.getAvailability(day);
        return new ResponseEntity<>(slots, HttpStatus.OK);
    }
    @PostMapping("/bookings")
    public ResponseEntity<Booker> bookLesson(@RequestBody Booker request, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        requirePupil(authHeader);
        try {
            Booker created = bookerClient.createBooking(request);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/bookings/pupil/{matriculation}")
    public ResponseEntity<List<Booker>> getPupilBookings(@PathVariable String matriculation,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        requirePupil(authHeader);
        List<Booker> list = bookerClient.getBookingsForPupil(matriculation);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/bookings/{bookingId}")
    public ResponseEntity<Void> cancelPupilBooking(@PathVariable String bookingId, @RequestParam String matriculation,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        requirePupil(authHeader);
        boolean cancelled = bookerClient.cancelBooking(bookingId, matriculation);
        if (!cancelled) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/bookings/tutor")
    public ResponseEntity<List<Booker>> getTutorBookings(@RequestParam String day, @RequestHeader(value = "Authorization", required = false) String authHeader) {
        requireTutor(authHeader);
        List<Booker> list = bookerClient.getBookingsForTutor(day);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/availability/block")
    public ResponseEntity<Void> blockAvailability(@RequestParam String day, @RequestParam(required = false) String startTime,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        requireTutor(authHeader);
        bookerClient.blockAvailability(day, startTime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/availability/unblock")
    public ResponseEntity<Void> unblockAvailability(@RequestParam String day, @RequestParam(required = false) String startTime,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        requireTutor(authHeader);
        bookerClient.unblockAvailability(day, startTime);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/bookings/check")
    public ResponseEntity<List<Booker>> checkBookingsAgainstTracker(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        requireTutor(authHeader);
        List<Booker> issues = bookerClient.checkBookingsAgainstTracker();
        return new ResponseEntity<>(issues, HttpStatus.OK);
    }
}