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
package org.seasar.doma.internal.apt.dao;

import java.util.Iterator;
import java.util.List;
import org.seasar.doma.Dao;

/** @author taedium */
@Dao(config = MyConfig.class)
public interface SqlValidationDao {

  void testBindVariable(String name);

  void testBindVariable_list(List<String> names);

  void testEmbeddedVariable(String orderBy);

  void testEmbeddedVariable_unsupportedType(Integer orderBy);

  void testFor(List<String> names);

  void testFor_notIterable(Iterator<String> names);

  void testFor_noTypeArgument(@SuppressWarnings("rawtypes") List names);

  void testExpand(String name);

  void testPopulate(String name);
}
