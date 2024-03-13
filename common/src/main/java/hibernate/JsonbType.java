/*
 * Copyright 2023-2024 the leader
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hibernate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Objects;

public class JsonbType implements UserType {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int getSqlType() {
        // Postgres specific type for jsonb
        return Types.OTHER;
    }

    @Override
    public Class returnedClass() {
        return ArrayList.class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        assert (x != null);
        return x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException{
        try {
            final String json = rs.getString(position);
            return json == null ? null : objectMapper.readValue(json, ArrayList.class);
        } catch (IOException e) {
            throw new HibernateException("Error converting jsonb to ArrayList", e);
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        try {
            if (value == null) {
                st.setNull(index, Types.OTHER);
            } else {
                st.setObject(index, objectMapper.writeValueAsString(value), Types.OTHER);
            }
        } catch (JsonProcessingException e) {
            throw new HibernateException("Error converting ArrayList to jsonb", e);
        }
    }

    @Override
    public Object deepCopy(Object value) {
        try {
            final String json = objectMapper.writeValueAsString(value);
            return objectMapper.readValue(json, ArrayList.class);
        } catch (IOException e) {
            throw new HibernateException("Error deep copying jsonb", e);
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) {
        return  (Serializable) this.deepCopy(value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) {
        return this.deepCopy(cached);
    }

    @Override
    public Object replace(Object detached, Object managed, Object owner) {
        return this.deepCopy(detached);
    }

}