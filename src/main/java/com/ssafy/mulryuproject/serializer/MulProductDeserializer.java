package com.ssafy.mulryuproject.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.ssafy.mulryuproject.entity.MulProduct;

// 0722 LHJ 현재로써는 사용하지 않는 메소드: JSON 역직렬화를 위해 만듦.
public class MulProductDeserializer extends JsonDeserializer<MulProduct> {

    @Override
    public MulProduct deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        // JSON에서 숫자 값을 읽어오기
        int productId = p.getIntValue();
        
        // MulProduct 객체를 생성하고 ID를 설정
        MulProduct mulProduct = MulProduct.builder().productId(productId).build();
        
        return mulProduct;
    }
}