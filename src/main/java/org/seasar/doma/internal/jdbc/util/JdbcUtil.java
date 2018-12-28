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
package org.seasar.doma.internal.jdbc.util;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.message.Message;

/** @author taedium */
public final class JdbcUtil {

  public static Connection getConnection(DataSource dataSource) {
    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      throw new JdbcException(Message.DOMA2015, e, e);
    }
  }

  public static Statement createStatement(Connection connection) {
    try {
      return connection.createStatement();
    } catch (SQLException e) {
      throw new JdbcException(Message.DOMA2032, e, e);
    }
  }

  public static PreparedStatement prepareStatement(Connection connection, Sql<?> sql) {
    try {
      return connection.prepareStatement(sql.getRawSql());
    } catch (SQLException e) {
      throw new JdbcException(Message.DOMA2016, e, sql.getSqlFilePath(), sql.getRawSql(), e);
    }
  }

  public static PreparedStatement prepareStatementForAutoGeneratedKeys(
      Connection connection, Sql<?> sql) {
    try {
      return connection.prepareStatement(sql.getRawSql(), Statement.RETURN_GENERATED_KEYS);
    } catch (SQLException e) {
      throw new JdbcException(Message.DOMA2016, e, sql.getSqlFilePath(), sql.getRawSql(), e);
    }
  }

  public static PreparedStatement prepareStatementForAutoGeneratedKeysOfFirstColumn(
      Connection connection, Sql<?> sql) {
    try {
      return connection.prepareStatement(sql.getRawSql(), new int[] {1});
    } catch (SQLException e) {
      throw new JdbcException(Message.DOMA2016, e, sql.getSqlFilePath(), sql.getRawSql(), e);
    }
  }

  public static CallableStatement prepareCall(Connection connection, Sql<?> sql) {
    try {
      return connection.prepareCall(sql.getRawSql());
    } catch (SQLException e) {
      throw new JdbcException(Message.DOMA2025, e, sql.getRawSql(), e);
    }
  }

  public static void close(Connection connection, JdbcLogger logger) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        if (logger != null) {
          logger.logConnectionClosingFailure(JdbcUtil.class.getName(), "close", e);
        }
      }
    }
  }

  public static void close(Statement statement, JdbcLogger logger) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        if (logger != null) {
          logger.logStatementClosingFailure(JdbcUtil.class.getName(), "close", e);
        }
      }
    }
  }

  public static void close(ResultSet resultSet, JdbcLogger logger) {
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (SQLException e) {
        if (logger != null) {
          logger.logResultSetClosingFailure(JdbcUtil.class.getName(), "close", e);
        }
      }
    }
  }
}
