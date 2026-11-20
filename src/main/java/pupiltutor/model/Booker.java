package pupiltutor.model;

public class Booker {

    private String bookingId;
    private String tutorId;
    private String pupilMatriculation;
    private String pupilName;
    private String instrument;
    private String day;
    private String startTime;
    private Integer pupilAge;


    public Booker(String day, String bookingId, String tutorId, String pupilMatriculation, String pupilName, String instrument, String startTime, Integer pupilAge) {
        this.day = day;
        this.bookingId = bookingId;
        this.tutorId = tutorId;
        this.pupilMatriculation = pupilMatriculation;
        this.pupilName = pupilName;
        this.instrument = instrument;
        this.startTime = startTime;
        this.pupilAge = pupilAge;
    }

    public Booker() {
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }


    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPupilMatriculation() {
        return pupilMatriculation;
    }

    public void setPupilMatriculation(String pupilMatriculation) {
        this.pupilMatriculation = pupilMatriculation;
    }

    public String getPupilName() {
        return pupilName;
    }

    public void setPupilName(String pupilName) {
        this.pupilName = pupilName;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getPupilAge() {
        return pupilAge;
    }

    public void setPupilAge(Integer pupilAge) {
        this.pupilAge = pupilAge;
    }

}