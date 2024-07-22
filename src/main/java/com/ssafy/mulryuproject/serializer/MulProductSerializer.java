package com.ssafy.mulryuproject.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ssafy.mulryuproject.entity.MulProduct;

// 0722 LHJ 현재로써는 사용하지 않는 메소드
public class MulProductSerializer extends JsonSerializer<MulProduct> {

    @Override
    public void serialize(MulProduct product, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeNumber(product.getProductId());
    }
}