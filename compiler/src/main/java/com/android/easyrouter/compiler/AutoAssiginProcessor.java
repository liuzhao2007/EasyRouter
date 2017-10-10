package com.android.router.compiler;

import com.android.easyrouter.annotation.AutoAssign;
import com.android.easyrouter.compiler.util.ParamTypeKinds;
import com.android.easyrouter.compiler.util.ParamTypeUtils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;


/**
 * Created by liuzhao on 2017/10/09.
 */

@AutoService(Processor.class)
public class AutoAssiginProcessor extends AbstractProcessor {

    private Elements elementUtils;
    private Messager mMessager;
    private Filer mFiler;
    private ParamTypeUtils paramTypeUtils;
    private Types types;
    private boolean hasDone = false;
    private Map<TypeElement, List<Element>> fieldMaps = new HashMap<>();
    private Map<String, Boolean> hashMaps = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        types = processingEnv.getTypeUtils();
        paramTypeUtils = new ParamTypeUtils(types, elementUtils);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(AutoAssign.class.getCanonicalName());
        return ret;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> autoAssigns = roundEnv.getElementsAnnotatedWith(AutoAssign.class);
        try {
            collectData(autoAssigns);
        } catch (Exception e) {
            error("error" + e.toString());
        }
        ParameterSpec objectParamSpec = ParameterSpec.builder(TypeName.OBJECT, "target").build();

        if (MapUtils.isNotEmpty(fieldMaps)) {
            for (Map.Entry<TypeElement, List<Element>> entry : fieldMaps.entrySet()) {
                MethodSpec.Builder autoAssignMethodBuilder = MethodSpec.methodBuilder("autoAssign")
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.STATIC)
                        .addParameter(objectParamSpec);

                TypeElement parent = entry.getKey();
                List<Element> childs = entry.getValue();

                String qualifiedName = parent.getQualifiedName().toString();
                String packageName = qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
                String fileName = parent.getSimpleName() + "_AutoAssign";

                TypeSpec.Builder helper = TypeSpec.classBuilder(fileName)
                        .addModifiers(Modifier.PUBLIC);

                // Generate method body, start inject.
                autoAssignMethodBuilder.addStatement("$T realObject = ($T)target", ClassName.get(parent), ClassName.get(parent));
                for (Element element : childs) {
                    AutoAssign fieldConfig = element.getAnnotation(AutoAssign.class);
                    String fieldName = element.getSimpleName().toString();
                    String originalValue = "realObject." + fieldName;
                    String statment = "realObject." + fieldName + " = realObject.";
                    statment += "getIntent().";

                    statment = buildStatement(originalValue, statment, paramTypeUtils.typeExchange(element), true);

                    autoAssignMethodBuilder.addStatement(statment, StringUtils.isEmpty(fieldConfig.name()) ? fieldName : fieldConfig.name());
                }
                helper.addMethod(autoAssignMethodBuilder.build());
                try {
                    if (!hashMaps.containsKey(fileName) || !hashMaps.get(fileName)) {
                        JavaFile.builder(packageName, helper.build()).build().writeTo(mFiler);
                    }
                    hashMaps.put(fileName, true);
                } catch (Exception e) {
                    error("error: " + e.toString());
                }
            }
            return true;
        }
        return false;
    }


    private String buildStatement(String originalValue, String statement, int type, boolean isActivity) {
        if (type == ParamTypeKinds.BOOLEAN.ordinal()) {
            statement += (isActivity ? ("getBooleanExtra($S, " + originalValue + ")") : ("getBoolean($S)"));
        } else if (type == ParamTypeKinds.BYTE.ordinal()) {
            statement += (isActivity ? ("getByteExtra($S, " + originalValue + "") : ("getByte($S)"));
        } else if (type == ParamTypeKinds.SHORT.ordinal()) {
            statement += (isActivity ? ("getShortExtra($S, " + originalValue + ")") : ("getShort($S)"));
        } else if (type == ParamTypeKinds.INT.ordinal()) {
            statement += (isActivity ? ("getIntExtra($S, " + originalValue + ")") : ("getInt($S)"));
        } else if (type == ParamTypeKinds.LONG.ordinal()) {
            statement += (isActivity ? ("getLongExtra($S, " + originalValue + ")") : ("getLong($S)"));
        } else if (type == ParamTypeKinds.CHAR.ordinal()) {
            statement += (isActivity ? ("getCharExtra($S, " + originalValue + ")") : ("getChar($S)"));
        } else if (type == ParamTypeKinds.FLOAT.ordinal()) {
            statement += (isActivity ? ("getFloatExtra($S, " + originalValue + ")") : ("getFloat($S)"));
        } else if (type == ParamTypeKinds.DOUBLE.ordinal()) {
            statement += (isActivity ? ("getDoubleExtra($S, " + originalValue + ")") : ("getDouble($S)"));
        } else if (type == ParamTypeKinds.STRING.ordinal()) {
            statement += (isActivity ? ("getStringExtra($S)") : ("getString($S)"));
        } else if (type == ParamTypeKinds.PARCELABLE.ordinal()) {
            statement += (isActivity ? ("getParcelableExtra($S)") : ("getParcelable($S)"));
        } else if (type == ParamTypeKinds.OBJECT.ordinal()) {
            statement = "serializationService.json2Object(realObject." + (isActivity ? "getIntent()." : "getArguments().") + (isActivity ? "getStringExtra($S)" : "getString($S)") + ", $T.class)";
        }
        return statement;
    }

    private void collectData(Set<? extends Element> elements) throws IllegalAccessException {
        if (CollectionUtils.isNotEmpty(elements)) {
            for (Element element : elements) {
                TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();

                if (element.getModifiers().contains(Modifier.PRIVATE)) {
                    throw new IllegalAccessException("The autoAssign fields mustn't be decorated with private !!! please check field ["
                            + element.getSimpleName() + "] in class [" + enclosingElement.getQualifiedName() + "]");
                }

                if (fieldMaps.containsKey(enclosingElement)) { // Has categries
                    fieldMaps.get(enclosingElement).add(element);
                } else {
                    List<Element> childs = new ArrayList<>();
                    childs.add(element);
                    fieldMaps.put(enclosingElement, childs);
                }
            }
        }
    }

    private void error(String error) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, error);
    }

}
