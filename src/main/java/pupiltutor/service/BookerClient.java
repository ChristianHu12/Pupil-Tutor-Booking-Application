package pupiltutor.service;

import pupiltutor.model.Pupil;
import pupiltutor.model.TimeSlots;
import pupiltutor.model.Booker;
import pupiltutor.model.Tutor;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BookerClient {

    private Tutor tutor;
    private final Map<String, Booker> bookings = new ConcurrentHashMap<>();
    private PupilClient pupilClient;

    public BookerClient(PupilClient pupilClient) {
        this.pupilClient = pupilClient;
        Map<String, int[]> tutorGrades = new LinkedHashMap<>();

        tutorGrades.put("piano", new int[]{3,7});
        tutorGrades.put("guitar", new int[]{1,8});

        Set<String> blockedDays = new HashSet<>();
        Map<String, Set<String>> blockedSlots = new HashMap<>();

        this.tutor = new Tutor("T1","Michael", tutorGrades, blockedDays, blockedSlots);
    }

    public List<TimeSlots> getAvailability(String day) {
        List<TimeSlots> slots = new ArrayList<>();
        if (day == null){
            return slots;
        }
        String avalDays = day.trim().toLowerCase();
        int startTime;
        if (avalDays.equals("monday") || avalDays.equals("tuesday") || avalDays.equals("wednesday") ||
        avalDays.equals("thursday") || avalDays.equals("friday")) {
            startTime  = 16*60;
        } else if (avalDays.equals("saturday")){
            startTime = 9 * 60;
        }
        else  {
            return slots;
        }

        int endTime = 20 * 60;
        boolean dayBlocked = tutor.getBlockedDays().contains(avalDays);
        Set<String> timesBlocked= tutor.getBlockedSlots().get(avalDays);

        for (int min = startTime; min < endTime; min += 30) {
            String startDay = toTimeString(min);
            String endDay = toTimeString(min + 30);

            boolean isBlocked = dayBlocked || (timesBlocked != null && timesBlocked.contains(startDay));

            boolean booked = false;
            for (Booker b : bookings.values()) {
                if (avalDays.contains(b.getDay()) && startDay.equals(b.getStartTime())) {
                    booked = true;
                    break;
                }
            }

            boolean avalaible = !booked && !isBlocked;
            slots.add(new TimeSlots(avalDays, startDay, endDay, avalaible));
        }
        return slots;
    }

    private String toTimeString(int totalMinutes){
        int hours = totalMinutes/60;
        int minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    public Booker createBooking(Booker book) {
        if (book == null || book.getDay() ==  null || book.getPupilMatriculation() == null
        || book.getInstrument() == null || book.getStartTime() == null) {
            throw new IllegalArgumentException("Wrong booking details happened");
        }

        String day = book.getDay().trim().toLowerCase();
        String startTime = book.getStartTime().trim();
        String instrument = book.getInstrument().trim().toLowerCase();

        List<TimeSlots> timeSlots = getAvailability(day);
        boolean valid = false;
        for(TimeSlots slots : timeSlots) {
            if (startTime.equals(slots.getStartTime()) && slots.isAvailable()){
                valid = true;
                break;
            }
        }

        if (!valid){
            throw new IllegalArgumentException("Not Valid");
        }

        Pupil pupil = pupilClient.getPupil(book.getPupilMatriculation());
        Integer instrumentGrade = pupil.
        if ()
        return null;
    }

    public List<Booker> getBookingsForPupil(String matriculationNumber) {
        return null;
    }

    public boolean cancelBooking(String bookingId, String matriculationNumber) {
        return false;
    }

    public List<Booker> getBookingsForTutor(String day) {
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