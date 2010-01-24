/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.mirror;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.Domain;
import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.internal.apt.util.ElementUtil;

/**
 * @author taedium
 * 
 */
public class DomainMirror {

    protected final AnnotationMirror annotationMirror;

    protected AnnotationValue valueType;

    protected AnnotationValue accessorMethod;

    protected DomainMirror(AnnotationMirror annotationMirror) {
        assertNotNull(annotationMirror);
        this.annotationMirror = annotationMirror;
    }

    public static DomainMirror newInstance(TypeElement clazz,
            ProcessingEnvironment env) {
        assertNotNull(env);
        AnnotationMirror annotationMirror = ElementUtil.getAnnotationMirror(
                clazz, Domain.class, env);
        if (annotationMirror == null) {
            return null;
        }
        DomainMirror result = new DomainMirror(annotationMirror);
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : env
                .getElementUtils().getElementValuesWithDefaults(
                        annotationMirror).entrySet()) {
            String name = entry.getKey().getSimpleName().toString();
            AnnotationValue value = entry.getValue();
            if ("valueType".equals(name)) {
                result.valueType = value;
            }
            if ("accessorMethod".equals(name)) {
                result.accessorMethod = value;
            }
        }
        return result;
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getValueType() {
        return valueType;
    }

    public AnnotationValue getAccessorMethod() {
        return accessorMethod;
    }

    public TypeMirror getValueTypeValue() {
        TypeMirror value = AnnotationValueUtil.toType(valueType);
        if (value == null) {
            throw new AptIllegalStateException("valueType");
        }
        return value;
    }

    public String getAccessorMethodValue() {
        String value = AnnotationValueUtil.toString(accessorMethod);
        if (value == null) {
            throw new AptIllegalStateException("accessorMethod");
        }
        return value;
    }

}
