package pupiltutor.model;

import java.util.Map;
import java.util.Set;

public class Tutor {

    private String tutorId;
    private String name;
    private Map<String, int[]> instrumentGrades;
    private Set<String> blockedDays;
    private Map<String, Set<String>> blockedSlots;

    public Tutor(String tutorId, String name, Map<String, int[]> instrumentGrades, Set<String> blockedDays, Map<String, Set<String>> blockedSlots) {
        this.tutorId = tutorId;
        this.name = name;
        this.instrumentGrades = instrumentGrades;
        this.blockedDays = blockedDays;
        this.blockedSlots = blockedSlots;
    }

    public Map<String, int[]> getInstrumentGrades() {
        return instrumentGrades;
    }

    public void setInstrumentGrades(Map<String, int[]> instrumentGrades) {
        this.instrumentGrades = instrumentGrades;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getBlockedDays() {
        return blockedDays;
    }

    public void setBlockedDays(Set<String> blockedDays) {
        this.blockedDays = blockedDays;
    }

    public Map<String, Set<String>> getBlockedSlots() {
        return blockedSlots;
    }

    public void setBlockedSlots(Map<String, Set<String>> blockedSlots) {
        this.blockedSlots = blockedSlots;
    }

    public boolean teachesInstrument(String instrument) {
        if (instrument == null || instrumentGrades == null) {
            return false;
        }
        return instrumentGrades.containsKey(instrument.trim().toLowerCase());
    }

    public boolean isGradeAllowed(String instrument, int grade) {
        if (!teachesInstrument(instrument)){
            return false;
        }
        int[] gradeLimits = instrumentGrades.get(instrument.trim().toLowerCase());
        if (gradeLimits == null || gradeLimits.length < 2){
            return false;
        }
        return grade >= gradeLimits[0] && grade <= gradeLimits[1];
    }
}