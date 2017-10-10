package com.android.easyrouter.compiler.util;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.android.easyrouter.compiler.constant.CompilerConstant.BYTE;
import static com.android.easyrouter.compiler.constant.CompilerConstant.SHORT;
import static com.android.easyrouter.compiler.constant.CompilerConstant.*;

/**
 * Created by liuzhao on 2017/10/9.
 */
public class ParamTypeUtils {

    private Types types;
    private Elements elements;

    public ParamTypeUtils(Types types, Elements elements) {
        this.types = types;
        this.elements = elements;
    }

    /**
     * Diagnostics out the true java type
     *
     * @param element Raw type
     * @return Type class of java
     */
    public int typeExchange(Element element) {
        TypeMirror typeMirror = element.asType();

        // Primitive
        if (typeMirror.getKind().isPrimitive()) {
            return element.asType().getKind().ordinal();
        }

        switch (typeMirror.toString()) {
            case BYTE:
                return ParamTypeKinds.BYTE.ordinal();
            case SHORT:
                return ParamTypeKinds.SHORT.ordinal();
            case INTEGER:
                return ParamTypeKinds.INT.ordinal();
            case LONG:
                return ParamTypeKinds.LONG.ordinal();
            case FLOAT:
                return ParamTypeKinds.FLOAT.ordinal();
            case DOUBEL:
                return ParamTypeKinds.DOUBLE.ordinal();
            case BOOLEAN:
                return ParamTypeKinds.BOOLEAN.ordinal();
            case STRING:
                return ParamTypeKinds.STRING.ordinal();
            default:
                return ParamTypeKinds.OBJECT.ordinal();
        }
    }
}
