/*
 * Copyright (c) 2010-2013. Axon Framework
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
package org.axonframework.saga.repository.jdbc;

import org.axonframework.serializer.SerializedObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Interface describing the SQL statements that the JdbcSagaRepository needs to execute against the underlying
 * database.
 *
 * @author Kristian Rosenvold
 * @author Allard Buijze
 * @since 2.2
 */
public interface SagaSqlSchema {

    /**
     * Creates a PreparedStatement that loads a single Saga, with given <code>sagaId</code>.
     *
     * @param connection The connection to create the PreparedStatement for
     * @param sagaId     The identifier of the Saga to return
     * @return a statement, that creates a result set to be processed by {@link #readSerializedSaga(java.sql.ResultSet)},
     * when executed
     *
     * @throws SQLException when an error occurs creating the PreparedStatement
     */
    PreparedStatement sql_loadSaga(Connection connection, String sagaId) throws SQLException;

    /**
     * Creates a PreparedStatement that removes an association value for given <code>sagaIdentifier</code>, where the
     * association is identified with given <code>key</code> and <code>value</code>.
     *
     * @param connection     The connection to create the PreparedStatement for
     * @param key            The key of the association to remove
     * @param value          The value of the association to remove
     * @param sagaType       The type of saga to remove the association for
     * @param sagaIdentifier The identifier of the Saga to remove the association for
     * @return a statement that removes the association value, when executed
     *
     * @throws SQLException when an error occurs creating the PreparedStatement
     */
    PreparedStatement sql_removeAssocValue(Connection connection, String key, String value, String sagaType,
                                           String sagaIdentifier) throws SQLException;

    /**
     * Creates a PreparedStatement that stores an association with given <code>key</code> and <code>value</code>, for a
     * Saga of given <code>type</code> and <code>identifier</code>.
     *
     * @param connection     The connection to create the PreparedStatement for
     * @param key            The key of the association to store
     * @param value          The value of the association to store
     * @param sagaType       The type of saga to create the association for
     * @param sagaIdentifier The identifier of the Saga to create the association for
     * @return a statement that inserts the association value, when executed
     *
     * @throws SQLException when an error occurs creating the PreparedStatement
     */
    PreparedStatement sql_storeAssocValue(Connection connection, String key, String value, String sagaType,
                                          String sagaIdentifier) throws SQLException;

    /**
     * Creates a PreparedStatement that finds identifiers of Sagas of given <code>sagaType</code> associated with the
     * given association <code>key</code> and <code>value</code>.
     *
     * @param connection The connection to create the PreparedStatement for
     * @param key        The key of the association
     * @param value      The value of the association
     * @param sagaType   The type of saga to find associations for
     * @return a PreparedStatement that creates a ResultSet containing only saga identifiers when executed
     *
     * @throws SQLException when an error occurs creating the PreparedStatement
     */
    PreparedStatement sql_findAssocSagaIdentifiers(Connection connection, String key, String value, String sagaType)
            throws SQLException;

    /**
     * Creates a PreparedStatement that deletes a Saga with given <code>sagaIdentifier</code>.
     *
     * @param connection     The connection to create the PreparedStatement for
     * @param sagaIdentifier The identifier of the Saga to remove
     * @return a statement that deletes the Saga, when executed
     *
     * @throws SQLException when an error occurs creating the PreparedStatement
     */
    PreparedStatement sql_deleteSagaEntry(Connection connection, String sagaIdentifier) throws SQLException;

    /**
     * Creates a PreparedStatement that deletes all association entires for a Saga with given
     * <code>sagaIdentifier</code>.
     *
     * @param connection     The connection to create the PreparedStatement for
     * @param sagaIdentifier The identifier of the Saga to remove associations for
     * @return a statement that deletes the associations, when executed
     *
     * @throws SQLException when an error occurs creating the PreparedStatement
     */
    PreparedStatement sql_deleteAssociationEntries(Connection connection, String sagaIdentifier) throws SQLException;

    /**
     * Creates a PreparedStatement that updates the serialized form of an existing Saga entry, of given
     * <code>sagaType</code> and with given <code>sagaIdentifier</code>.
     *
     * @param connection     The connection to create the PreparedStatement for
     * @param sagaIdentifier The identifier of the Saga to update
     * @param serializedSaga The serialized form of the saga to update
     * @param sagaType       The serialized type of the saga
     * @param revision       The revision identifier of the serialized form
     * @return a statement that updates a Saga entry, when executed
     *
     * @throws SQLException when an error occurs creating the PreparedStatement
     */
    PreparedStatement sql_updateSaga(Connection connection, String sagaIdentifier, byte[] serializedSaga,
                                     String sagaType, String revision) throws SQLException;

    /**
     * Creates a PreparedStatement that inserts a Saga entry, of given <code>sagaType</code> and with given
     * <code>sagaIdentifier</code>.
     *
     * @param connection     The connection to create the PreparedStatement for
     * @param sagaIdentifier The identifier of the Saga to insert
     * @param serializedSaga The serialized form of the saga to insert
     * @param sagaType       The serialized type of the saga
     * @param revision       The revision identifier of the serialized form
     * @return a statement that inserts a Saga entry, when executed
     *
     * @throws SQLException when an error occurs creating the PreparedStatement
     */
    PreparedStatement sql_storeSaga(Connection connection, String sagaIdentifier, String revision, String sagaType,
                                    byte[] serializedSaga) throws SQLException;

    /**
     * Creates a PreparedStatement that creates the table for storing Association Values for Sagas.
     *
     * @param connection The connection to create the PreparedStatement for
     * @return a Prepared statement that created the Association Value table, when executed
     *
     * @throws SQLException when an error occurs creating the PreparedStatement
     */
    PreparedStatement sql_createTableAssocValueEntry(Connection connection) throws SQLException;

    /**
     * Creates a PreparedStatement that creates the table for storing Sagas.
     *
     * @param connection The connection to create the PreparedStatement for
     * @return a Prepared statement that created the Saga table, when executed
     *
     * @throws SQLException when an error occurs creating the PreparedStatement
     */
    PreparedStatement sql_createTableSagaEntry(Connection connection) throws SQLException;

    /**
     * Reads a SerializedObject from the given <code>resultSet</code>, which has been returned by executing the
     * Statement returned from {@link #sql_loadSaga(java.sql.Connection, String)}
     * <p/>
     * Note: The implementation must not change the resultSet's cursor position
     *
     * @param resultSet The result set to read data from.
     * @return a SerializedObject, containing the serialized data from the resultSet
     *
     * @throws SQLException when an exception occurs reading from the resultSet
     */
    SerializedObject<?> readSerializedSaga(ResultSet resultSet) throws SQLException;
}