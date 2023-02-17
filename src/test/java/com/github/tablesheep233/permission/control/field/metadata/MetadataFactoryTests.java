package com.github.tablesheep233.permission.control.field.metadata;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.tablesheep233.permission.control.field.metadata.factory.MetadataFactory;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class MetadataFactoryTests {

    private final MetadataFactory factory = new MetadataFactory();

    public static enum NumberEnum {
        ONE,
        TWO
    }

    @Data
    public static class BasicVO implements Serializable {
        public static BasicVO INSTANCE = new BasicVO();
        private long id;
        private Long idWrapper;
        private String content;
        private NumberEnum number = NumberEnum.ONE;
    }

    @Test
    public void javaBasic() throws JsonProcessingException {
        Assertions.assertTrue(assertMetadataContent(BasicVO.INSTANCE, factory.create("VO", BasicVO.class)));
    }

    @Data
    public static class ArrayVO implements Serializable {
        public static ArrayVO INSTANCE = new ArrayVO();

        private int[] ints;
        private String[] strings;
        private Set set;
        private List<Double> doubles;
        private List<BigDecimal> bigDecimals;
    }


    @Test
    public void array() throws JsonProcessingException {
        Assertions.assertTrue(assertMetadataContent(ArrayVO.INSTANCE, factory.create("VO", ArrayVO.class)));
    }

    @Data
    public static class MultiVO implements Serializable {
        public static MultiVO INSTANCE = new MultiVO();

        private long id;
        private Item item = Item.INSTANCE;

        @Data
        public static class Item implements Serializable {
            public static Item INSTANCE = new Item();

            private String code = "code";
            private int index = 0;
        }
    }

    @Test
    public void multi() throws JsonProcessingException {
        Assertions.assertTrue(assertMetadataContent(MultiVO.INSTANCE, factory.create("VO", MultiVO.class)));
    }

    @Data
    public static class GenericParam<T, D, W> {

        public GenericParam(T t, D d, W w) {
            this.t1 = t;
            this.t2 = t;
            this.d = d;
            this.w1 = w;
            this.w2 = w;
            this.w3 = w;
        }

        private T t1;
        private T t2;
        private D d;
        private W w1;
        private W w2;
        private W w3;
        private String content;
    }

    @Data
    public static class GenericParamVO {
        public static GenericParamVO INSTANCE = new GenericParamVO();

        private List<MultiVO> multiList = List.of(MultiVO.INSTANCE);
        private GenericParam<String, Integer, MultiVO> genericParam = new GenericParam<>("", 0, MultiVO.INSTANCE);
    }

    @Test
    public void genericParam() throws JsonProcessingException {
        Assertions.assertTrue(assertMetadataContent(GenericParamVO.INSTANCE, factory.create("VO", GenericParamVO.class)));
    }

    @Data
    public static class ComplexVO {
        public static ComplexVO INSTANCE = new ComplexVO();

        private List<GenericParam<Double, BigDecimal, BasicVO>> genericParams = List.of(new GenericParam<>(0d, new BigDecimal("0.0"), BasicVO.INSTANCE));
        private GenericParam<String, Integer, MultiVO>[] genericParamArray = new GenericParam[]{new GenericParam<>("", 0, MultiVO.INSTANCE)};
        private List<List<GenericParam<Double, MultiVO, BasicVO>[]>> wtf = List.of(List.<GenericParam<Double, MultiVO, BasicVO>[]>of((new GenericParam[]{new GenericParam<>(0d, MultiVO.INSTANCE, BasicVO.INSTANCE)})));
    }

    @Test
    public void complex() throws JsonProcessingException {
        Assertions.assertTrue(assertMetadataContent(ComplexVO.INSTANCE, factory.create("VO", ComplexVO.class)));
    }


    private boolean assertMetadataContent(Object object, RootMetadata metadata) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = objectMapper.writeValueAsString(object);
        JsonNode jsonNode = objectMapper.readTree(json);

        RootMetadata.MetaDataContent content = metadata.getContent();
        for (RootMetadata.MetaDataContent field : content.getFields()) {
            if (notExistField(field, jsonNode)) {
                return false;
            }
        }
        return true;
    }

    private boolean notExistField(RootMetadata.MetaDataContent content, JsonNode parentNode) {
        while (parentNode.isArray()) {
            parentNode = parentNode.get(0);
        }
        if (!parentNode.has(content.getKey())) {
            return true;
        }
        JsonNode thisNode = parentNode.get(content.getKey());
        if (content.isMulti()) {
            for (RootMetadata.MetaDataContent field : content.getFields()) {
                if (notExistField(field, thisNode)) {
                    return true;
                }
            }
        }
        return false;
    }
}
