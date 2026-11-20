
package pupiltutor.model;

import java.util.Map;

public class PupilResponseJSON {

    public TrackerPupil pupil;

    public static class TrackerPupil {
        public String name;
        public int age;
        public int matric;
        public Map<String, Instrument> instruments;
    }
    public static class Instrument {
        public int grade;
    }
}

