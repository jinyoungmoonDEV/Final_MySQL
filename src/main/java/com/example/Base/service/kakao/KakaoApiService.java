package com.example.Base.service.kakao;

import com.example.Base.domain.dto.user.UserDTO;
import com.example.Base.service.user.UserServiceImpl;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Service
public class KakaoApiService {
    private final UserServiceImpl userService;
    UserDTO userDTO = new UserDTO();

    public String getAccessToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            //  일반적으로 클라이언트 프로그램은 URL을 통해 서버와 통신할때 다음 단계를 따른다.
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // URLConnection 클래스의 인스턴스를 HttpUrlConnection으로 casting 후 반환
            // URL connection 구성
            conn.setRequestMethod("POST"); // http 메소드 방식 설정
            conn.setDoOutput(true); // 데이터 사용 여부 체크

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            sb.append("&client_id=080a03e6b451205bd005c2fd5af0b258"); // rest api 키
            // 080a03e6b451205bd005c2fd5af0b258 <-- 낭범 rest api key
//            sb.append("&redirect_uri=http://localhost:3000/kakaoLogin"); // redirectURL
            sb.append("&redirect_uri=http://13.209.99.47:3000/kakaoLogin"); // redirectURL
            sb.append("&code="+code);
            /*--------------------------------------------------------------------------------*/
            // url 형식 -> "https://kauth.kakao.com/oauth/authorize?client_id=a217377ee3f4b5124de42e804382f03e&redirect_uri=http://localhost:10200/login&response_type=code"
//            String kakaoUrl = "grant_type=authorization_code&client_id=a217377ee3f4b5124de42e804382f03e&redirect_uri=http://localhost:10200/login&code=" + code;


            /*--------------------------------------------------------------------------------*/
            bw.write(sb.toString()); // <-- 기존 코드
//            bw.write(kakaoUrl);
            bw.flush();  // flush()는 현재 버퍼에 저장되어 있는 내용을 클라이언트로 전송하고 버퍼를 비운다. (JSP)

            int responseCode = conn.getResponseCode(); // 응답코드 확인 용도
            log.info("🔥 log response code = " + responseCode);

            // bw.flush(); 로 코드 형식에 맞춰 인증코드가 잘 갔다면 토큰을 받을 객체 버퍼 리더 객체를 생성한다.
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            // readLine 함수를 사용해서 라인 단위로 끊어서 String line 변수에 담고 그 값을 차곡차곡 Strgin result 변수에 받는다.
            while((line = br.readLine())!=null) {
                result += line;
            }
            /*Resource server 로 부터 전달 받은 access token 과 refresh token 을 String result에 잘 담았다면
            파싱 후 JSON 객체에 담는다.*/

            log.info("🔥 log : response body = " + result);

            JsonElement element = JsonParser.parseString(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken;
    }


    public Map<String, String> getUserInfo(String accessToken) {
        Map<String, String> userInfoMap = new HashMap<String, String>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken); // 일단 요청 속성 설정
            int responseCode = conn.getResponseCode(); // 상태코드 확인
            log.info("log : responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            // Resource Server가 전달한 회원 정보 br 객체가 받아서
            String line = "";
            String result = "";

            // readLine() 메소드를 사용해서 서버로 들어온 회원 정보를 라인 단위로 끊고 String line 변수에 담는다.
            while((line = br.readLine()) != null) { // line 변수에 값이 없을때 까지 계속 String result 변수에 이어서 담는다.
                result += line;
            }
            log.info("log : response body = " + result);

//            JsonParser parser = new JsonParser();  // JSON 객체를 파싱하기 위한 객체 생성
            JsonElement element = JsonParser.parseString(result); // JSON 객체에 담기

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject(); // getAsJsonObject를 이용해서 원하는 타입을 이용해서 값을 받아오면 된다.
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String name = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();
            String gender = kakaoAccount.getAsJsonObject().get("gender").getAsString();
            String profileImageURL = kakaoAccount.get("profile").getAsJsonObject().get("profile_image_url").getAsString();

            log.info("name from  kaka service : " + name);
            log.info("email from  kaka service : " + email);
            log.info("img : " + profileImageURL);

            userInfoMap.put("name", name);
            userInfoMap.put("email", email);
//            userInfoMap.put("profileImageURL", profileImageURL);


            // 회원 존재 여부 확인
            if ( userService.getUser(email) == null ) {
                // 회원 DB에 없다면 유저 생성 메소드 실행
                userDTO.setRole("ROLE_USER");
                userDTO.setName(name);
                userDTO.setEmail(email);
                userDTO.setPassword("tempPassword");
                userDTO.setGender(gender);
                userDTO.setProfileImageURL(profileImageURL);
                userService.saveUser(userDTO);
                return userInfoMap;  // 방금 생성된 유저 정보 (name, email)

            }
            else {
                log.info("존재하는 회원입니다. -> " + userInfoMap.get("name"));
                // 이미 우리 회원이면 저장 x -> 정보를 controller 로 넘긴다. -> login 처리 로직 실행한다.
                return userInfoMap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return userInfoMap;
    }
//

    public void kakaoLogout(String accessToken) {
        String reqURL = "http://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";

            while((line = br.readLine()) != null) {
                result+=line;
            }
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
