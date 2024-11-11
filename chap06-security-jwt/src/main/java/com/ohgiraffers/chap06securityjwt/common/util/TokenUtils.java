package com.ohgiraffers.chap06securityjwt.common.util;

import com.ohgiraffers.chap06securityjwt.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 토큰관련 - 토큰 생성/유효성검사 등
@Component
public class TokenUtils {

    // 토큰 관련 수행 클래스

    private static String jwtSecretKey;

    private static long tokenValidateTime;

    @Value("${jwt.key}") // yml 에 만든거 불러오는 것
    public void setJwtSecretKey(String jwtSecretKey) {
        TokenUtils.jwtSecretKey = jwtSecretKey;
    }
    @Value("${jwt.time}")
    public void setTokenValidateTime(long tokenValidateTime) {
        TokenUtils.tokenValidateTime = tokenValidateTime;
    }


    /**
     * header 의 token 을 분리하는 메소드
     * @Param header : Authorization 의 header 값을 가져온다.
     * @return token : Authorization 의 token 을 반환한다.
     * */

    public static String splitHeader(String header){
        if(!header.equals("")){
            return header.split(" ")[1]; // BEARER 를 제외한 토큰 값만 반환
        }else {
            return null;
        }
    }

    // 핵심
    /**
     * 유효한 토큰인지 확인하는 메소드
     * @Param token : 토큰
     * @return boolean : 유효 여부
     * */

    public static boolean isValidToken(String token){

        try{
            Claims claims = getClaimsFromToken(token);
            return true;
        }catch(Exception e){
            return false;
        }

    }


    /**
     * 토큰을 복호화 하는 메소드
     * @Param token
     * @return Claims
     * */
    public static Claims getClaimsFromToken(String token) {
        // jwt.parser 와 parseClaimsJws 를 이용해 JWT 의 유효성을 검증한다.
        // 만약 JWT 가 유효하지 않으면 예외가 발생한다. (예외발생 시 isValidToken() 이 false 반환)

        // 입력한 키(jwtSecretKey) 를 기준으로 암호화된 토큰을 복호화한다.
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey)).parseClaimsJws(token).getBody();
    }


    // 핵심
    /**
     * 토큰을 생성하는 메소드
     * @param user userEntity
     * @return String token
     * */
    public static String generateJwtToken(User user){
        // 토큰 만료 시간을 현재 시간에서 지정된 유효 시간 이후로 설정
        Date expireTime = new Date((System.currentTimeMillis() + tokenValidateTime));
        // JwtBuilder 를 사용해 JWT 토큰을 생성하는 객체 초기화
        JwtBuilder builder = Jwts.builder()
                .setHeader(createHeader()) // 토큰의 헤더 설정(헤더에는 토큰의 타입 및 알고리즘 정보가 담김)
                .setClaims(createClaims(user)) // 토큰의 클레임 설정(사용자 정보와 같은 데이터를 담음)
                .setSubject("ohgiraffers token : " + user.getUserNo()) // 토큰의 설명 정보를 담아줌
                .signWith(SignatureAlgorithm.HS256, createSignature()) // 토큰 암호화 방식 정의
                .setExpiration(expireTime); // 만료시간 설정

        // 최종적으로 생성된 토큰을 문자열 형태로 반환
        return builder.compact();
    }


    /**
     * token 의 header 를 설정하는 메소드
     * @return Map<String, Object> header 의 설정 정보
     * */
    private static Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        // 헤더의 토큰 타입을 JWT 로 설정
        header.put("type", "jwt");
        // 헤더의 토큰의 알고리즘을 HS25 으로 설정
        header.put("alg", "HS256");
        // 헤더에 토큰 생성 시간을 밀리초 단위로 추가
        header.put("date", System.currentTimeMillis());

        return header;
    }

    /**
     * 사용자 정보를 기반으로 클레임을 생성해주는 메소드
     * @param user 사용자정보
     * @return Map<String, Object> Claims 정보
     * */
    // Claims : 토큰에 포함되는 실질적인 데이터
    private static Map<String, Object> createClaims(User user) {
        // 클라이언트가 보관할 것이기 때문에 비밀번호는 담지 않는다.
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getUserId());
        claims.put("role", user.getRole());
        claims.put("userEmail", user.getUserEmail());
        return claims;
    }


    /**
     * Jwt 서명을 발급해주는 메소드
     * @return key
     * */
    private static Key createSignature() {
        // 비밀 키 문자열을 Base64 로 디코딩하여 바이트 배열로 반환
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);
        // 변환된 바이트 배열을 알고리즘을 사용해 key 객체로 반환
        // HS256 으로 암호화 후 반환함.
        return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }

}
