package com.ohgiraffers.chap06securityjwt.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ConvertUtil {

    // 전달받은 객체를 JSON 형태로 변환 후 파싱하는 메소드
    // 토큰 -> json


    public static Object convertObjectToJsonObject(Object obj){

        // 자바 객체를 json 문자열로 반환하는 라이브러리
        ObjectMapper mapper = new ObjectMapper();
        // json 문자열을 json 객체로 파싱하는 객체
        JSONParser parser = new JSONParser();

        String convertJsonString; // 반환될 json 문자열을 저장할 변수
        Object convertObj; // 최종적으로 json 객체로 변환될 결과를 저장할 변수

        try {
            // 객체를 json 문자열로 변환
            convertJsonString = mapper.writeValueAsString(obj);
            // json 문자열을 json 객체로 파싱
            convertObj = parser.parse(convertJsonString);

        } catch(JsonProcessingException e){ // 문자열 변환 실패 시
            throw new RuntimeException(e);
        } catch(ParseException e){ // json 파싱 실패 시
            throw new RuntimeException(e);
        }

        return convertObj;
    }
}
