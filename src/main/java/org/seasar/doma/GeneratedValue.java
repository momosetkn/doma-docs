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
package org.seasar.doma;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 識別子を自動生成する方法を示します。
 * <p>
 * このアノテーションが注釈されるフィールドは、エンティティクラスのメンバでなければいけません。 このアノテーションは {@link Id}
 * と併わせて使用しなければいけません。
 * <p>
 * {@code strategy} 要素に指定する値によっては追加のアノテーションが必要です。
 * <ul>
 * <li> {@link GenerationType#SEQUENCE} を指定した場合、{@link SequenceGenerator} が必要です。
 * <li>
 * {@link GenerationType#TABLE} を指定した場合、 {@link TableGenerator} が必要です。
 * </ul>
 * 
 * <h3>例:</h3>
 * 
 * <pre>
 * &#064;Entity
 * public class Employee {
 * 
 *     &#064;Id
 *     &#064;GeneratedValue(strategy = GenerationType.SEQUENCE)
 *     &#064;SequenceGenerator(sequence = &quot;EMPLOYEE_SEQ&quot;)
 *     Integer id;
 *     
 *     ...
 * }
 * </pre>
 * 
 * @author taedium
 * @see GenerationType
 * @see SequenceGenerator
 * @see TableGenerator
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GeneratedValue {

    /**
     * 識別子を自動生成する方法を返します。
     * 
     * @return 識別子を自動生成する方法
     */
    GenerationType strategy();
}
