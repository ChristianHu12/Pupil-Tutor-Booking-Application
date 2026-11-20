package pupiltutor.service;

import org.springframework.stereotype.Service;
import pupiltutor.model.Pupil;
import pupiltutor.model.TimeSlots;
import pupiltutor.model.Booker;
import pupiltutor.model.Tutor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BookerClient {

    private Tutor tutor;
    private final Map<String, Tutor> tutors = new ConcurrentHashMap<>();
    private final Map<String, Booker> bookings = new ConcurrentHashMap<>();
    private PupilClient pupilClient;

    public BookerClient(PupilClient pupilClient) {
        this.pupilClient = pupilClient;

        Map<String, int[]> tutorGrades = new LinkedHashMap<>();
        tutorGrades.put("piano", new int[]{1, 7});
        tutorGrades.put("guitar", new int[]{1, 8});
        Tutor firstTutor = new Tutor("T1", "Michael", tutorGrades, new HashSet<>(), new HashMap<>());

        Map<String, int[]> secondTutorGrades = new LinkedHashMap<>();
        secondTutorGrades.put("drums", new int[]{1, 8});
        secondTutorGrades.put("violin", new int[]{1, 6});
        Tutor secondTutor = new Tutor("T2", "Hannah", secondTutorGrades, new HashSet<>(), new HashMap<>());

        Map<String, int[]> thirdTutorGrades = new LinkedHashMap<>();
        thirdTutorGrades.put("flute", new int[]{1, 7});
        thirdTutorGrades.put("piano", new int[]{3, 8});
        Tutor thirdTutor = new Tutor("T3", "David", thirdTutorGrades, new HashSet<>(), new HashMap<>());

        tutors.put(firstTutor.getTutorId(), firstTutor);
        tutors.put(secondTutor.getTutorId(), secondTutor);
        tutors.put(thirdTutor.getTutorId(), thirdTutor);

        this.tutor = firstTutor;
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
                if (avalDays.contains(b.getDay().toLowerCase()) && startDay.equals(b.getStartTime())) {
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

        Pupil pupil = pupilClient.getPupilByMatriculation(book.getPupilMatriculation());
        if (pupil == null){
            throw new IllegalArgumentException("No Pupil here");
        }
        Integer instrumentGrade = pupil.getGradeForInstrument(instrument);
            if (instrumentGrade == null){
                throw new IllegalArgumentException("No Instrument grade here");
            }

        Tutor matchedTutor = null;
        for (Tutor t : tutors.values()) {
            if (t.isGradeAllowed(instrument, instrumentGrade)) {
                matchedTutor = t;
                break;
            }
        }
        if (matchedTutor == null) {
            throw new IllegalArgumentException("Grade too low or high for this tutor");
        }
        book.setTutorId(matchedTutor.getTutorId());

        for (Booker existingBook : bookings.values()){
            if (book.getPupilMatriculation().equals(existingBook.getPupilMatriculation()) &&
                    day.equalsIgnoreCase(existingBook.getDay())){
                throw new IllegalArgumentException("Pupil has already a booking for today");
            }
        }

        if (pupil.getAge() < 16 && startTime.compareTo("18:00") >= 0){
            throw new IllegalArgumentException("This student is too young to be booking at 6pm onwards");
        }

        String bookingID = UUID.randomUUID().toString();
        book.setBookingId(bookingID);
        book.setDay(day);
        book.setInstrument(instrument);
        book.setPupilAge(pupil.getAge());
        book.setPupilName(pupil.getName());
        bookings.put(bookingID, book);


        return book;
    }

    public List<Booker> getBookingsForPupil(String matriculationNumber) {
        List<Booker> result = new ArrayList<>();
        if (matriculationNumber == null){
            return result;
        }
        for (Booker book : bookings.values()){
            if (matriculationNumber.equals(book.getPupilMatriculation())){
                result.add(book);
            }
        }
        return result;
    }

    public boolean cancelBooking(String bookingId, String matriculationNumber) {
        if (bookingId == null || matriculationNumber == null){
            return false;
        }

        Booker book = bookings.get(bookingId);
        if (book != null && matriculationNumber.equals(book.getPupilMatriculation())){
            bookings.remove(bookingId);
            return true;
        }

        return false;
    }

    public List<Booker> getBookingsForTutor(String day) {
        List<Booker> result = new ArrayList<>();
        if (day == null){
            return result;
        }
        String bookDay = day.trim().toLowerCase();
        for (Booker book : bookings.values()){
            if (bookDay.equals(book.getDay())){
                result.add(book);
            }
        }
        return result;
    }
    public void blockAvailability(String day, String startTime) {
        if (day == null || day.isBlank()){
            return;
        }
        String blockDay = day.trim().toLowerCase();
        if (startTime == null || startTime.isBlank()){
            tutor.getBlockedDays().add(blockDay);
        } else {
            tutor.getBlockedSlots().computeIfAbsent(blockDay, blockString -> new HashSet<>()).add(startTime.trim());
        }
    }

    public void unblockAvailability(String day, String startTime) {
        if(day == null || day.isEmpty()){
            return;
        }
        String removeBlockDay = day.trim().toLowerCase();
        if (startTime == null || startTime.isBlank()){
            tutor.getBlockedDays().remove(removeBlockDay);
        } else{
            Set<String> slots = tutor.getBlockedSlots().get(removeBlockDay);
            if (slots != null){
                slots.remove(startTime.trim());
            }
        }
    }

    public List<Booker> checkBookingsAgainstTracker() {
        List<Booker> badBookings = new ArrayList<>();
        if (pupilClient == null){
            return badBookings;
        }

        for (Booker booking : bookings.values()){
            Pupil pupil = pupilClient.getPupilByMatriculation(booking.getPupilMatriculation());
            if (pupil == null) {
                badBookings.add(booking);
                continue;
            }
            String instrument = booking.getInstrument().trim().toLowerCase();
            Integer grade = pupil.getGradeForInstrument(instrument);
            int[] validGrades = tutor.getInstrumentGrades().get(instrument);

            if (grade == null || validGrades == null) {
                badBookings.add(booking);
                continue;
            }

            if (grade < validGrades[0] || grade > validGrades[1]) {
                badBookings.add(booking);
                continue;
            }
            if (pupil.getAge() < 16 && booking.getStartTime().trim().compareTo("18:00") >= 0) {
                badBookings.add(booking);
            }
        }
        return badBookings;
    }
}