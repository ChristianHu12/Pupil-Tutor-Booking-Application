package pupiltutor.model;

import java.util.Locale;
import java.util.Map;

public class Pupil {

    private String matriculationNumber;
    private String name;
    private int age;
    private Map<String, Integer> instrumentGrades;

    public Pupil(String matriculationNumber, String name, int age, Map<String, Integer> instrumentGrades) {
        this.matriculationNumber = matriculationNumber;
        this.name = name;
        this.age = age;
        this.instrumentGrades = instrumentGrades;
    }
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMatriculationNumber() {
        return matriculationNumber;
    }

    public void setMatriculationNumber(String matriculationNumber) {
        this.matriculationNumber = matriculationNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Integer> getInstrumentGrades() {
        return instrumentGrades;
    }

    public void setInstrumentGrades(Map<String, Integer> instrumentGrades) {
        this.instrumentGrades = instrumentGrades;
    }


    public Integer getGradeForInstrument(String instrument) {
        if (instrument == null || instrumentGrades == null){
            return null;
        }
        return instrumentGrades.get(instrument.trim().toLowerCase());
    }
}