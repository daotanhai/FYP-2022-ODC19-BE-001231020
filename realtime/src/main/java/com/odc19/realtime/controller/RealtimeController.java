package com.odc19.realtime.controller;

import io.openvidu.java.client.ConnectionProperties;
import io.openvidu.java.client.ConnectionType;
import io.openvidu.java.client.OpenVidu;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;
import io.openvidu.java.client.Session;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController()
@RequestMapping("/api-sessions")
public class RealtimeController {
    private OpenVidu openVidu;

    private Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    private Map<String, String> sessionNameToken = new ConcurrentHashMap<>();

    private String OPENVIDU_SERVER_URL;

    private String SECRET_KEY;

    public RealtimeController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
        this.SECRET_KEY = secret;
        this.OPENVIDU_SERVER_URL = openviduUrl;
        this.openVidu = new OpenVidu(OPENVIDU_SERVER_URL, SECRET_KEY);
    }

    @PostMapping(value = "/get-token")
    public ResponseEntity<JSONObject> generateToken(@RequestBody String sessionParam) throws ParseException {
        JSONObject sessionJSON = (JSONObject) new JSONParser().parse(sessionParam);
        String sessionName = (String) sessionJSON.get("sessionName");
        ConnectionProperties connectionProperties = new ConnectionProperties.Builder().type(ConnectionType.WEBRTC).build();
        JSONObject jsonObjectResponse = new JSONObject();

        if (this.sessionMap.get(sessionName) != null) {
            try {
                String token = this.sessionMap.get(sessionName).createConnection(connectionProperties).getToken();
                this.sessionNameToken.replace(sessionName, token);
                jsonObjectResponse.put(0, token);
                return new ResponseEntity<>(jsonObjectResponse, HttpStatus.OK);
            } catch (OpenViduJavaClientException | OpenViduHttpException e) {
                this.sessionMap.remove(sessionName);
                this.sessionNameToken.remove(sessionName);
                throw new RuntimeException(e);
            }
        }
        try {
            Session session = this.openVidu.createSession();
            String token = session.createConnection(connectionProperties).getToken();
            this.sessionMap.put(sessionName, session);
            this.sessionNameToken.put(sessionName, token);
            jsonObjectResponse.put(0, token);
            return new ResponseEntity<>(jsonObjectResponse, HttpStatus.OK);
        } catch (OpenViduJavaClientException e) {
            throw new RuntimeException(e);
        } catch (OpenViduHttpException e) {
            throw new RuntimeException(e);
        }

    }
}
