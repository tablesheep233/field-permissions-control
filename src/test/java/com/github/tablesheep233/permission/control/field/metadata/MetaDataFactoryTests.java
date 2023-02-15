package com.github.tablesheep233.permission.control.field.metadata;

import com.github.tablesheep233.permission.control.field.metadata.factory.MetaDataFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class MetaDataFactoryTests {

    private final MetaDataFactory factory = new MetaDataFactory();

    public static class BasicVO {
        private long id;
        private Long idWrapper;
        private String content;
    }

    @Test
    public void javaBasic() {
        RootMetaData metaData = factory.create("VO", BasicVO.class);
    }

    public static class ArrayVO {
        private int[] ints;
        private String[] strings;
        private Set set;
        private List<Double> doubles;
        private List<BigDecimal> bigDecimals;
    }


    @Test
    public void array() {
        RootMetaData metaData = factory.create("VO", ArrayVO.class);
    }

    public static class MultiVO {
        private long id;
        private Item item;

        public static class Item {
            private String code;
            private int index;
        }
    }

    @Test
    public void multi() {
        RootMetaData metaData = factory.create("VO", MultiVO.class);
    }

    public static class GenericParam<T, D, W> {
        private T t1;
        private T t2;
        private D d;
        private W w1;
        private W w2;
        private W w3;
        private String content;
    }

    public static class GenericParamVO {
        private List<MultiVO> multiList;
        private GenericParam<String, Integer, MultiVO> genericParam;

    }

    @Test
    public void genericParam() {
        RootMetaData metaData = factory.create("VO", GenericParamVO.class);
    }

    public static class ComplexVO {
        private List<GenericParam<Double, BigDecimal, BasicVO>> genericParams;
        private GenericParam<Double, BigDecimal, BasicVO>[] genericParamArray;
        private List<List<List<String>>> string3d;
        private List<List<GenericParam<Double, BigDecimal, BasicVO>[]>> wtf;
    }

    @Test
    public void complex() {
        RootMetaData metaData = factory.create("VO", ComplexVO.class);
    }
}
