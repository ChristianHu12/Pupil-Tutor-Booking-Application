package pupiltutor.controller;

import pupiltutor.model.TimeSlots;
import pupiltutor.model.Booker;
import pupiltutor.service.BookerClient;

import java.sql.Time;
import java.util.List;

public class BookingController {

    private BookerClient bookerClient;

    public BookingController(BookerClient bookingService) {
    }

    public List<TimeSlots> getAvailability(String day) {
        return null;
    }

    public Booker bookLesson(Booker request) {
        return null;
    }

    public List<Booker> getPupilBookings(String matriculation) {
        return null;
    }

    public void cancelPupilBooking(String bookingId, String matriculation) {
    }

    public List<Booker> getTutorBookings(String day) {
        return null;
    }

    public void blockAvailability(String day, String startTime) {
    }

    public void unblockAvailability(String day, String startTime) {
    }

    public List<Booker> checkBookingsAgainstTracker() {
        return null;
    }
}