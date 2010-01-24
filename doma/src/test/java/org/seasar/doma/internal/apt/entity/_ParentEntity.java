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
package org.seasar.doma.internal.apt.entity;

import java.util.List;

import org.seasar.doma.internal.jdbc.entity.BasicPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityPropertyType;
import org.seasar.doma.internal.jdbc.entity.EntityType;
import org.seasar.doma.internal.jdbc.entity.GeneratedIdPropertyType;
import org.seasar.doma.internal.jdbc.entity.VersionPropertyType;
import org.seasar.doma.jdbc.entity.NamingType;
import org.seasar.doma.wrapper.Wrapper;

public class _ParentEntity implements EntityType<ParentEntity> {

    public BasicPropertyType<ParentEntity, Integer> aaa = new BasicPropertyType<ParentEntity, Integer>(
            "aaa", "AAA", true, true) {

        public Wrapper<Integer> getWrapper(ParentEntity entity) {
            return null;
        }
    };

    public BasicPropertyType<ParentEntity, Integer> bbb = new BasicPropertyType<ParentEntity, Integer>(
            "bbb", "BBB", true, true) {

        public Wrapper<Integer> getWrapper(ParentEntity entity) {
            return null;
        }
    };

    private _ParentEntity() {
    }

    @Override
    public void saveCurrentStates(ParentEntity entity) {
    }

    @Override
    public String getCatalogName() {

        return null;
    }

    @Override
    public Class<ParentEntity> getEntityClass() {

        return null;
    }

    @Override
    public EntityPropertyType<ParentEntity, ?> getEntityPropertyType(String name) {

        return null;
    }

    @Override
    public List<EntityPropertyType<ParentEntity, ?>> getEntityPropertyTypes() {

        return null;
    }

    @Override
    public GeneratedIdPropertyType<ParentEntity, ?> getGeneratedIdPropertyType() {

        return null;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public ParentEntity getOriginalStates(ParentEntity entity) {

        return null;
    }

    @Override
    public String getSchemaName() {

        return null;
    }

    @Override
    public String getTableName() {

        return null;
    }

    @Override
    public VersionPropertyType<ParentEntity, ?> getVersionPropertyType() {

        return null;
    }

    @Override
    public ParentEntity newEntity() {

        return null;
    }

    @Override
    public void preDelete(ParentEntity entity) {

    }

    @Override
    public void preInsert(ParentEntity entity) {

    }

    @Override
    public void preUpdate(ParentEntity entity) {

    }

    @Override
    public List<EntityPropertyType<ParentEntity, ?>> getIdPropertyTypes() {
        return null;
    }

    @Override
    public String getQualifiedTableName() {
        return null;
    }

    @Override
    public NamingType getNamingType() {
        return null;
    }

    public static _ParentEntity get() {
        return null;
    }

}
