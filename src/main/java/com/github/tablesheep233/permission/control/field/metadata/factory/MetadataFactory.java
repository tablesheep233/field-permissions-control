package com.github.tablesheep233.permission.control.field.metadata.factory;

import com.github.tablesheep233.permission.control.field.metadata.*;
import org.reflections.ReflectionUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.time.temporal.Temporal;
import java.util.*;

@Component
public class MetadataFactory {

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

    private final Map<String, RootMetadata> rootMetaDataMap = new HashMap<>(256);
    private final Map<String, Metadata> classMetaDataMap = new HashMap<>(256);
    private final Map<String, Map<String, String>> typeParameterMap = new HashMap<>(128);
    private final Map<String, Metadata> genericParameterMap = new HashMap<>(128);
    private final List<String> resolvingGenericParameter = new ArrayList<>(8);

    public synchronized RootMetadata create(String key, Class<?> clazz) {
        return rootMetaDataMap.computeIfAbsent(key, (k) -> new RootMetadata(k, clazz, resolveFields(clazz)));
    }

    private List<Metadata> resolveFields(Class<?> clazz) {
        Set<Field> fields = ReflectionUtils.getAllFields(clazz, field -> !Modifier.isStatic(field.getModifiers()));
        List<Metadata> fieldMetaData = new ArrayList<>(fields.size());
        for (Field field : fields) {
            fieldMetaData.add(resolveField(field.getName(), field.getGenericType()));
        }
        return fieldMetaData;
    }

    private Metadata resolveGenericType(Type genericType) {
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

    private Metadata doResolveGenericArrayType(GenericArrayType genericType) {
        if (genericParameterMap.containsKey(genericType.toString())) {
            return genericParameterMap.get(genericType.toString());
        }

        Type genericComponentType = genericType.getGenericComponentType();
        if (genericComponentType instanceof  ParameterizedType) {
            ArrayMetadata arrayMetaData = new ArrayMetadata(PLACEHOLDER, doResolveParameterizedType((ParameterizedType) genericComponentType));
            genericParameterMap.put(genericType.toString(), arrayMetaData);
            return arrayMetaData;
        }
        throw new RuntimeException();
    }

    private Metadata doResolveTypeVariable(TypeVariable genericType) {
        String resolvingGenericParameterKey = resolvingGenericParameter.get(resolvingGenericParameter.size() - 1);
        String metaDateKey = typeParameterMap.get(resolvingGenericParameterKey).get(genericType.getName());
        Metadata metaData = classMetaDataMap.get(metaDateKey);
        if (metaData != null) {
            return metaData;
        }
        metaData = genericParameterMap.get(metaDateKey);
        if (metaData != null) {
            return metaData;
        }
        throw new RuntimeException();
    }

    private Metadata doResolveClass(Class<?> clazz) {
        String typeName = clazz.getTypeName();
        if (classMetaDataMap.containsKey(typeName)) {
            return classMetaDataMap.get(typeName);
        }

        if (isGeneric(clazz)) {
            GenericMetadata genericMetaData = new GenericMetadata(PLACEHOLDER, clazz);
            classMetaDataMap.put(typeName, genericMetaData);
            return genericMetaData;
        }

        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            ArrayMetadata arrayMetaData = new ArrayMetadata(PLACEHOLDER);
            if (isGeneric(componentType)) {
                arrayMetaData.setActualData(classMetaDataMap.computeIfAbsent(componentType.getTypeName(), (k) -> new GenericMetadata(PLACEHOLDER, componentType)));
            } else {
                arrayMetaData.setActualData(resolveClass(PLACEHOLDER, componentType));
            }
            classMetaDataMap.put(typeName, arrayMetaData);
            return arrayMetaData;
        }

        if (isCollection(clazz)) {
            CollectionMetadata collectionMetaData = new CollectionMetadata(PLACEHOLDER, classMetaDataMap.computeIfAbsent(Object.class.getTypeName(), (k) -> new GenericMetadata(PLACEHOLDER, Object.class)));
            classMetaDataMap.put(typeName, collectionMetaData);
            return collectionMetaData;
        }

        Metadata metaData = resolveClass(clazz);
        classMetaDataMap.put(typeName, metaData);
        return metaData;
    }

    private Metadata doResolveParameterizedType(ParameterizedType parameterizedType) {
        if (genericParameterMap.containsKey(parameterizedType.toString())) {
            return genericParameterMap.get(parameterizedType.toString());
        }

        if (isCollection((Class<?>) parameterizedType.getRawType())) {
            Metadata actualMetadata = resolveGenericType(parameterizedType.getActualTypeArguments()[0]);
            CollectionMetadata collectionMetaData = new CollectionMetadata(PLACEHOLDER, actualMetadata);
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
        Metadata metaData = resolveClass((Class<?>) parameterizedType.getRawType());
        resolvingGenericParameter.remove(resolvingGenericParameter.size() - 1);
        genericParameterMap.put(parameterizedType.toString(), metaData);
        return metaData;
    }

    private Metadata resolveClass(Class<?> type) {
        return resolveClass(PLACEHOLDER, type);
    }

    private Metadata resolveClass(String key, Class<?> type) {
        MultiMetadata metaData = new MultiMetadata(key, type);
        metaData.setMultiData(resolveFields(type));
        return metaData;
    }

    private Metadata resolveField(String key, Type genericType) {
        Metadata metaData = resolveGenericType(genericType);
        if (PLACEHOLDER.equals(metaData.getKey())) {
            return new KeyWrapperMetadata(key, metaData);
        }
        return metaData;
    }

    private boolean isGeneric(Class<?> type) {
        return isBasic(type.getTypeName()) || type.isEnum() || Temporal.class.isAssignableFrom(type) || Date.class.isAssignableFrom(type);
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
