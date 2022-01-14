package com.example.musicat.service.music;

import com.example.musicat.domain.music.Music;
import com.example.musicat.domain.music.Playlist;
import com.example.musicat.domain.music.PlaylistImage;
import com.example.musicat.exception.RestErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.apache.logging.log4j.core.pattern.MdcPatternConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
`
@Slf4j
@Service
public class MusicApiService {
    private final String URI_MUSICS_ID = "http://13.124.245.202:20000/api/musics/find/{id}";
    private final String URI_MUSICS_UPLOAD = "http://13.124.245.202:20000/api/musics/uploadFile";
    private final String URI_PLAYLIST_CREATE = "http://13.124.245.202:20000/api/playlists/create";
    private final String URI_PLAYLIST_PUSH = "http://13.124.245.202:20000/api/playlists/push";
    private final String URI_PLAYLIST_CHANGE = "http://13.124.245.202:20000/api/playlists/update";
    private final String URI_PLAYLIST_ID = "http://13.124.245.202:20000/api/playlists/{memberNo}";
    private final String URI_PLAYLIST_DETAIL = "http://13.124.245.202:20000/api/playlists/detail/{playlistNo}";
    private final String URI_MUSICS_TEST = "http://13.124.245.202:20000/api/posttest";

    private final RestTemplate restTemplate;

    /*******************************************************************
     * RestTemplate Error Handling
     ******************************************************************/
    public MusicApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        //this.restTemplate.setErrorHandler(new RestErrorHandler());
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

    public Music registerMusic(MultipartFile file, MultipartFile imagefile, String title, int memberNo) throws HttpClientErrorException {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        System.out.println("file : " + file.getOriginalFilename());

        ByteArrayResource byteArray = null;
        try {
            byteArray = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() throws IllegalStateException {
                    return URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        body.add("audio", byteArray);

        System.out.println("imageFile : " + imagefile.getOriginalFilename());
        ByteArrayResource byteArray2 = null;
        try {
            byteArray2 = new ByteArrayResource(imagefile.getBytes()) {
                @Override
                public String getFilename() throws IllegalStateException {
                    return URLEncoder.encode(imagefile.getName(), StandardCharsets.UTF_8);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        body.add("image", byteArray2);
        body.add("title", title);
        body.add("memberNo", memberNo);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Music> response = restTemplate.postForEntity(URI_MUSICS_UPLOAD, requestEntity, Music.class);

        System.out.println("status code : " + response.getStatusCode());

        return response.getBody();
    }

    public void deleteMusicByArticleNo(int articleNo) {
        Map<String, Integer> params = new HashMap<String, Integer>();
        params.put("articleNo", articleNo);
        restTemplate.delete("http://13.124.245.202:20000/api/musics/deleteByArticleNo/{articleNo}", params);
    }

    public void deleteByMusicId(Long musicId) {
        Map<String, Long> params = new HashMap<String, Long>();
        params.put("musicId", musicId);
        restTemplate.delete("http://13.124.245.202:20000/api/musics/deleteById/{musicId}", params);
    }

    public void connectToArticle(Long musicId, int articleNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("musicId", musicId);
        params.put("articleNo", articleNo);
        log.info(params.toString());
        restTemplate.put("http://13.124.245.202:20000/api/musics/connectToArticle/{musicId}/{articleNo}",String.class, params);
    }

    public List<Music> retrieveMusics(int articleNo){

        ResponseEntity<List> response = restTemplate.getForEntity("http://13.124.245.202:20000/api/musics/findMusics/{articleNo}", List.class, articleNo);
       
        log.info("response body : " + response.getBody());


        List<Music> musicList = response.getBody();
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>()
//
//        Map<String, Integer> params = new HashMap<String, Integer>();
//        params.put("articleNo", articleNo);
//
//        ResponseEntity<List<Music>> response = restTemplate.exchange(
//                "http://localhost:20000/api/musics/findMusics/{articleNo}",
//                HttpMethod.GET, Integer.class, new ParameterizedTypeReference<List<Music>>() {});
//        List<Music> musicList = response.getBody();


        log.info("retrieveMusics : " + musicList.toString());

        return musicList;
    }

    // 플레이리스트 추가
    public Playlist createPlaylist(Playlist playlist, MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource byteArray = null;
        try {
            byteArray = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() throws IllegalStateException {
                    return URLEncoder.encode(file.getName(), StandardCharsets.UTF_8);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        body.add("image", byteArray);
        body.add("playlist", playlist);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Playlist> response = restTemplate.postForEntity(URI_PLAYLIST_CREATE, requestEntity, Playlist.class);
        log.info("body : " + response.getBody());
        return response.getBody();
    }

    // 플레이리스트 삭제
    public void deletePlaylist(int memberNo, String playlistKey) {
        Map<String, Object> map = new HashMap<>();
        map.put("memberNo", memberNo);
        map.put("playlistKey", playlistKey);
        log.info("map : " + map);

        restTemplate.delete("http://13.124.245.202:20000/api/playlists/delete/{memberNo}/{playlistNo}" , map);

    }

    // 특정 플레이리스트 안에 곡 넣기
    public List<Music> pushMusic(List<Integer> musicNos, String playlistKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("musicNos", musicNos);
        map.put("playlistKey", playlistKey);
        ResponseEntity<List> response = restTemplate.postForEntity(URI_PLAYLIST_PUSH, map, List.class);
        log.info("res : " + response.getBody());
        return response.getBody();
    }

    // 특정 플레이리스트 안의 곡 빼기
    public void pullMusic(List<Integer> musicNos, String playlistKey) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("musicNos", musicNos);

        map.put("playlistNo", playlistNo);
        restTemplate.delete("http://13.124.245.202:20000/api/playlists/pull/{playlistNo}/{musicNos}", map);
    }

    // 플레이리스트 정보 가져오기
    public Playlist getOnePlaylist(int playlistNo) {
        ResponseEntity<Playlist> response = restTemplate.getForEntity("http://13.124.245.202:20000/api/onePlaylists/{playlistNo}", Playlist.class, playlistNo);
        return response.getBody();
    }

    // 플레이리스트 수정
    public Playlist updatePlaylistName(String playlistKey, String title, MultipartFile imgfile) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        ByteArrayResource byteArray = null;

        try {
            byteArray = new ByteArrayResource(imgfile.getBytes()){
                @Override
                public String getFilename() throws IllegalStateException {
                    return URLEncoder.encode(imgfile.getName(), StandardCharsets.UTF_8);
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        body.add("image", byteArray);
        body.add("playlistKey", playlistKey);
        body.add("title", title);
        log.info("map.playlistKey : " + body.get("playlistKey"));
        log.info("map.title : " + body.get("title"));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Playlist> response = restTemplate.postForEntity(URI_PLAYLIST_CHANGE, requestEntity, Playlist.class);

        log.info("res : ", response.getBody());
        return response.getBody();
    }

    // 플레이리스트 목록 불러오기
    public List<Playlist> showPlaylist(int memberNo) {
        ResponseEntity<List> pl = restTemplate.getForEntity(URI_PLAYLIST_ID, List.class, memberNo);
        //log.info(pl.toString());
        List<Playlist> list = pl.getBody();
        if(list.size() != 0) {
            list.remove(0);
        }
        System.out.println(list);
        return pl.getBody();
    }

    // 플레이리스트 상세 불러오기
    public List<Music> showDetailPlaylist(String playlistKey) {
        ResponseEntity<List> response = restTemplate.getForEntity(URI_PLAYLIST_DETAIL, List.class, playlistKey);
        List<Music> musics = response.getBody();
        log.info("playlist : " + musics);
        return musics;
    }
}
