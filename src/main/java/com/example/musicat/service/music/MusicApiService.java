package com.example.musicat.service.music;

import com.example.musicat.exception.RestErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class MusicApiService {
    private final String URI_MUSICS_ID = "http://localhost:20000/api/musics/find/{id}";
    private final String URI_USERS_ID = "http://localhost:10000/api/users/{id}";//

    private final RestTemplate restTemplate;

    /*******************************************************************
     * RestTemplate Error Handling
     ******************************************************************/
    public MusicApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.restTemplate.setErrorHandler(new RestErrorHandler());
    }

    public void retrieveMusicById(Long id) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("id", id);

        System.out.println("call getforentity");
        ResponseEntity<String> responseEntity =  restTemplate.getForEntity(
                URI_MUSICS_ID, String.class, params);
        String fileName = responseEntity.getBody();
        System.out.println("respise Entity : " + responseEntity);
        System.out.println("fileName : " + fileName);
    }
}
