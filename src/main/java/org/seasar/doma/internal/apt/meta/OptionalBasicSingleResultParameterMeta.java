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
package org.seasar.doma.internal.apt.meta;

import static org.seasar.doma.internal.util.AssertionUtil.*;

import org.seasar.doma.internal.apt.cttype.BasicCtType;

/** @author taedium */
public class OptionalBasicSingleResultParameterMeta implements SingleResultParameterMeta {

  protected final BasicCtType basicCtType;

  public OptionalBasicSingleResultParameterMeta(BasicCtType basicCtType) {
    assertNotNull(basicCtType);
    this.basicCtType = basicCtType;
  }

  public BasicCtType getBasicCtType() {
    return basicCtType;
  }

  @Override
  public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
    return visitor.visitOptionalBasicSingleResultParameterMeta(this, p);
  }
}
