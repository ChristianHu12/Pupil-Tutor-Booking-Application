package pupiltutor.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import pupiltutor.model.Pupil;
import pupiltutor.model.PupilResponseJSON;

import java.util.HashMap;
import java.util.Map;

@Service
public class PupilClient {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public PupilClient() {
        this.baseUrl = "https://pmaier.eu.pythonanywhere.com/plt";
        this.restTemplate =  new RestTemplate();
    }

    public Pupil getPupilByMatriculation(String matriculationNumber) {
        if (matriculationNumber == null || matriculationNumber.isEmpty()) {
            return null;
        }

        PupilResponseJSON response;
        try {
            response = restTemplate.getForObject(
                    baseUrl + "/pupil/" + matriculationNumber,
                    PupilResponseJSON.class
            );
        } catch (HttpClientErrorException ex) {
            return null;
        }

        if (response == null || response.pupil == null) {
            return null;
        }

        Map<String, Integer> grades = new HashMap<>();

        for (String instrument : response.pupil.instruments.keySet()) {
            grades.put(instrument,
                    response.pupil.instruments.get(instrument).grade);
        }
        return new Pupil(String.valueOf(response.pupil.matric), response.pupil.name, response.pupil.age,
                grades
        );
    }
}
