package pupiltutor.model;

public class TimeSlots {


    private String day;
    private String startTime;
    private String endTime;
    private boolean available;

    public TimeSlots(String day, String startTime, String endTime, boolean available) {
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = available;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


}