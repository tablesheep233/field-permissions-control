package com.github.tablesheep233.permission.control.field.metadata.factory;

import com.github.tablesheep233.permission.control.field.metadata.*;
import org.reflections.ReflectionUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

@Component
public class MetaDataFactory {

    public static final String PLACEHOLDER = "?";

    private static final Collection<String> BASIC_TYPE = List.of(
            Byte.TYPE.getTypeName(), Byte.class.getTypeName(),
            Boolean.TYPE.getTypeName(), Boolean.class.getTypeName(),
            Character.TYPE.getTypeName(), Character.class.getTypeName(),
            Short.TYPE.getTypeName(), Short.class.getTypeName(),
            Integer.TYPE.getTypeName(), Integer.class.getTypeName(),
            Float.TYPE.getTypeName(), Float.class.getTypeName(),
            Double.TYPE.getTypeName(), Double.class.getTypeName(),
            Long.TYPE.getTypeName(), Long.class.getTypeName(),
            String.class.getTypeName(), BigDecimal.class.getTypeName()
    );

    private final Map<String, RootMetaData> rootMetaDataMap = new HashMap<>(256);
    private final Map<String, MetaData> classMetaDataMap = new HashMap<>(256);
    private final Map<String, Map<String, String>> typeParameterMap = new HashMap<>(128);
    private final Map<String, MetaData> genericParameterMap = new HashMap<>(128);
    private final List<String> resolvingGenericParameter = new ArrayList<>(8);

    public synchronized RootMetaData create(String key, Class<?> clazz) {
        return rootMetaDataMap.computeIfAbsent(key, (k) -> {
            RootMetaData metaData = new RootMetaData(k, clazz, resolveFields(clazz));

            //todo merge metadata
            return metaData;
        });
    }

    private List<MetaData> resolveFields(Class<?> clazz) {
        Set<Field> fields = ReflectionUtils.getAllFields(clazz, field -> !Modifier.isStatic(field.getModifiers()));
        List<MetaData> fieldMetaData = new ArrayList<>(fields.size());
        for (Field field : fields) {
            fieldMetaData.add(resolveField(field.getName(), field.getGenericType()));
        }
        return fieldMetaData;
    }

    private MetaData resolveGenericType(Type genericType) {
        if (genericType instanceof Class) {
            return doResolveClass((Class<?>) genericType);
        }
        if (genericType instanceof ParameterizedType) {
            return doResolveParameterizedType((ParameterizedType) genericType);
        }
        if (genericType instanceof GenericArrayType) {
            return doResolveGenericArrayType((GenericArrayType) genericType);
        }
        if (genericType instanceof TypeVariable) {
            return doResolveTypeVariable((TypeVariable) genericType);
        }
        throw new UnsupportedOperationException();
    }

    private MetaData doResolveGenericArrayType(GenericArrayType genericType) {
        if (genericParameterMap.containsKey(genericType.toString())) {
            return genericParameterMap.get(genericType.toString());
        }

        Type genericComponentType = genericType.getGenericComponentType();
        if (genericComponentType instanceof  ParameterizedType) {
            ArrayMetaData arrayMetaData = new ArrayMetaData(PLACEHOLDER, doResolveParameterizedType((ParameterizedType) genericComponentType));
            genericParameterMap.put(genericType.toString(), arrayMetaData);
            return arrayMetaData;
        }
        throw new RuntimeException();
    }

    private MetaData doResolveTypeVariable(TypeVariable genericType) {
        String resolvingGenericParameterKey = resolvingGenericParameter.get(resolvingGenericParameter.size() - 1);
        String metaDateKey = typeParameterMap.get(resolvingGenericParameterKey).get(genericType.getName());
        MetaData metaData = classMetaDataMap.get(metaDateKey);
        if (metaData != null) {
            return metaData;
        }
        metaData = genericParameterMap.get(metaDateKey);
        if (metaData != null) {
            return metaData;
        }
        throw new RuntimeException();
    }

    private MetaData doResolveClass(Class<?> clazz) {
        String typeName = clazz.getTypeName();
        if (classMetaDataMap.containsKey(typeName)) {
            return classMetaDataMap.get(typeName);
        }

        if (isBasic(typeName)) {
            GenericMetaData genericMetaData = new GenericMetaData(PLACEHOLDER, clazz);
            classMetaDataMap.put(typeName, genericMetaData);
            return genericMetaData;
        }

        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            ArrayMetaData arrayMetaData = new ArrayMetaData(PLACEHOLDER);
            if (isBasic(componentType.getTypeName())) {
                arrayMetaData.setActualData(classMetaDataMap.computeIfAbsent(componentType.getTypeName(), (k) -> new GenericMetaData(PLACEHOLDER, componentType)));
            } else {
                arrayMetaData.setActualData(resolveClass(PLACEHOLDER, componentType));
            }
            classMetaDataMap.put(typeName, arrayMetaData);
            return arrayMetaData;
        }

        if (isCollection(clazz)) {
            CollectionMetaData collectionMetaData = new CollectionMetaData(PLACEHOLDER, classMetaDataMap.computeIfAbsent(Object.class.getTypeName(), (k) -> new GenericMetaData(PLACEHOLDER, Object.class)));
            classMetaDataMap.put(typeName, collectionMetaData);
            return collectionMetaData;
        }

        MetaData metaData = resolveClass(clazz);
        classMetaDataMap.put(typeName, metaData);
        return metaData;
    }

    private MetaData doResolveParameterizedType(ParameterizedType parameterizedType) {
        if (genericParameterMap.containsKey(parameterizedType.toString())) {
            return genericParameterMap.get(parameterizedType.toString());
        }

        if (isCollection((Class<?>) parameterizedType.getRawType())) {
            MetaData actualMetaData = resolveGenericType(parameterizedType.getActualTypeArguments()[0]);
            CollectionMetaData collectionMetaData = new CollectionMetaData(PLACEHOLDER, actualMetaData);
            genericParameterMap.put(parameterizedType.toString(), collectionMetaData);
            return collectionMetaData;
        }

        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        TypeVariable<? extends Class<?>>[] typeParameters = ((Class<?>) parameterizedType.getRawType()).getTypeParameters();
        Map<String, String> signatureMap = new HashMap<>(actualTypeArguments.length);
        for (int i = 0; i < actualTypeArguments.length; i++) {
            signatureMap.put(typeParameters[i].getName(),
                    actualTypeArguments[i] instanceof Class ? ((Class<?>) actualTypeArguments[i]).getName() : actualTypeArguments[i].toString());
            resolveGenericType(actualTypeArguments[i]);
        }
        typeParameterMap.put(parameterizedType.getTypeName(), signatureMap);
        resolvingGenericParameter.add(parameterizedType.getTypeName());
        MetaData metaData = resolveClass((Class<?>) parameterizedType.getRawType());
        resolvingGenericParameter.remove(resolvingGenericParameter.size() - 1);
        genericParameterMap.put(parameterizedType.toString(), metaData);
        return metaData;
    }

    private MetaData resolveClass(Class<?> type) {
        return resolveClass(PLACEHOLDER, type);
    }

    private MetaData resolveClass(String key, Class<?> type) {
        MultiMetaData metaData = new MultiMetaData(key, type);
        metaData.setMultiData(resolveFields(type));
        return metaData;
    }

    private MetaData resolveField(String key, Type genericType) {
        MetaData metaData = resolveGenericType(genericType);
        if (PLACEHOLDER.equals(metaData.getKey())) {
            return new KeyWrapperMetaData(key, metaData);
        }
        return metaData;
    }

    private boolean isBasic(String typeName) {
        return BASIC_TYPE.contains(typeName);
    }

    private boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    private Class<?> rawType(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType)type).getRawType();
        }
        if (type instanceof GenericArrayType) {
            return rawType(((GenericArrayType)type).getGenericComponentType());
        }
        if (type instanceof Class) {
            return (Class<?>) type;
        }
        throw new UnsupportedOperationException();
    }
}
