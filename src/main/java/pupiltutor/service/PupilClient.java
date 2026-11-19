package pupiltutor.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pupiltutor.model.Pupil;

@Service
public class PupilClient {

    private final String baseUrl;
    private final RestTemplate restTemplate;

    public PupilClient() {
        this.baseUrl = "http://localhost:8080";
        this.restTemplate =  new RestTemplate();
    }

    public Pupil getPupilByMatriculation(String matriculationNumber) {
        if (matriculationNumber == null || matriculationNumber.isEmpty()){
            return null;
        }
        return restTemplate.getForObject(baseUrl + "/pupils/" + matriculationNumber, Pupil.class );
    }
}