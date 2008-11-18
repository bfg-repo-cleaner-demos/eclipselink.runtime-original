/*******************************************************************************
 * Copyright (c) 1998, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package org.eclipse.persistence.config;

/**
 * The class defines EclipseLink query hints.
 * These query hints allow a JPA Query to be customized or optimized beyond
 * what is available in the JPA specification.
 * 
 * <p>JPA Query Hint Usage:
 * 
 * <p><code>query.setHint(QueryHints.CACHE_USAGE, CacheUsage.CheckCacheOnly);</code>
 * <p>or 
 * <p><code>@QueryHint(name=QueryHints.CACHE_USAGE, value=CacheUsage.CheckCacheOnly)</code>
 * 
 * <p>Hint values are case-insensitive; "" could be used instead of default value.
 * 
 * @see HintValues
 * @see CacheUsage
 * @see PessimisticLock
 * @see QueryType
 */
public class QueryHints {
    /**
     * "eclipselink.jdbc.bind-parameters"
     * <p>Configures parameter binding to be disabled or enabled just for this query (overrides persistent unit setting, which default to true).
     * Valid values are:  HintValues.PERSISTENCE_UNIT_DEFAULT, HintValues.TRUE, HintValues.FALSE,
     * "" could be used instead of default value HintValues.PERSISTENCE_UNIT_DEFAULT
     * @see PersistenceUnitProperties#JDBC_BIND_PARAMETERS
     * @see org.eclipse.persistence.queries.DatabaseQuery#setShouldBindAllParameters(boolean)
     */
    public static final String BIND_PARAMETERS = "eclipselink.jdbc.bind-parameters";

    /**
     * "eclipselink.cache-usage"
     * <p>Configures the query to utilize the EclipseLink cache, by default the cache is not checked on queries before accessing the database.
     * Valid values are all declared in CacheUsage class.
     * For primary key cache hits the QUERY_TYPE hint must also be set to QueryType.ReadObject.
     * @see CacheUsage
     * @see #QUERY_TYPE
     * @see org.eclipse.persistence.queries.ReadObjectQuery
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setCacheUsage(int)
     */
    public static final String CACHE_USAGE = "eclipselink.cache-usage";
    
    /**
     * "eclipselink.query-results-cache"
     * <p>Configures the query to use a results cache.
     * By default the query will cache 100 query results, such that the same named query with the same arguments
     * is re-executed it will skip the database and just return the cached results.
     * Valid values are:  HintValues.PERSISTENCE_UNIT_DEFAULT, HintValues.TRUE, HintValues.FALSE,
     * "" could be used instead of default value HintValues.PERSISTENCE_UNIT_DEFAULT.
     * By default query results are not cached.
     * This is not, and is independent from the object cache.
     * @see org.eclipse.persistence.queries.QueryResultsCachePolicy
     * @see org.eclipse.persistence.queries.ReadQuery#setQueryResultsCachePolicy(org.eclipse.persistence.queries.QueryResultsCachePolicy)
     */
    public static final String QUERY_RESULTS_CACHE = "eclipselink.query-results-cache";

    /**
     * "eclipselink.query-results-cache.size"
     * <p>Configures the size of the query's results cache.
     * By default the query will cache 100 query results, such that the same named query with the same arguments
     * is re-executed it will skip the database and just return the cached results.
     * If the query has no arguments a size of 1 should be used (as there is only a single result).
     * Valid values are Integer or Strings that can be parsed to int values.
     * @see org.eclipse.persistence.queries.QueryResultsCachePolicy#setMaximumCachedResults(int)
     */
    public static final String QUERY_RESULTS_CACHE_SIZE = "eclipselink.query-results-cache.size";

    /**
     * "eclipselink.query-results-cache.expiry"
     * <p>Configures the time to live, or expiry time of the query's results cache.
     * By default the query results cache will not expiry results.
     * Valid values are number of milliseconds, Integer or Strings that can be parsed to int values.
     * @see org.eclipse.persistence.descriptors.invalidation.CacheInvalidationPolicy
     * @see org.eclipse.persistence.queries.QueryResultsCachePolicy#setCacheInvalidationPolicy(org.eclipse.persistence.descriptors.invalidation.CacheInvalidationPolicy)
     */
    public static final String QUERY_RESULTS_CACHE_EXPIRY = "eclipselink.query-results-cache.expiry";
    
    /**
     * "eclipselink.query-results-cache.expiry-time-of-day"
     * <p>Configures the time of day expiry time of the query's results cache.
     * By default the query results cache will not expiry results.
     * Valid values are String Time format, "HH:MM:SS".
     * @see org.eclipse.persistence.descriptors.invalidation.CacheInvalidationPolicy
     * @see org.eclipse.persistence.queries.QueryResultsCachePolicy#setCacheInvalidationPolicy(org.eclipse.persistence.descriptors.invalidation.CacheInvalidationPolicy)
     */
    public static final String QUERY_RESULTS_CACHE_EXPIRY_TIME_OF_DAY = "eclipselink.query-results-cache.expiry-time-of-day";
    
    /**
     * "eclipselink.query.redirector"
     * <p>Used to provide a QueryRedirector to the executing query.
     * The redirector must implement the QueryRedirector interface.
     * This can be used to perform advanced query operations in code,
     * or dynamically customize the query in code before execution.
     * @see org.eclipse.persistence.queries.QueryRedirector
     * @see org.eclipse.persistence.queries.DatabaseQuery#setRedirector(org.eclipse.persistence.queries.QueryRedirector)
     */
    public static final String QUERY_REDIRECTOR = "eclipselink.query.redirector";

    /**
     * "eclipselink.query-type"
     * <p>Configures the EclipseLink query type to use for the query.
     * By default EclipseLink ReportQuery or ReadAllQuery are used for most JPQL queries, this allows other query types to be used,
     * such as ReadObjectQuery which can be used for queries that are know to return a single object, and has different caching semantics.
     * Valid values are all declared in QueryType class.
     * @see QueryType
     */
    public static final String QUERY_TYPE = "eclipselink.query-type";
    
    /**
     * "eclipselink.pessimistic-lock"
     * <p>Configures  the query to acquire a pessimistic lock (write-lock) on the resulting rows in the database.
     * Valid values are all declared in PessimisticLock class.
     * Pessimistic locking support and behavior may differ on difference database platforms.
     * @see PessimisticLock
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setLockMode(short)
     */
    public static final String PESSIMISTIC_LOCK = "eclipselink.pessimistic-lock";
    
    /**
     * "javax.persistence.lock.timeout"
     * <p>Configures the WAIT timeout used in pessimistic locking, if the database 
     * query exceeds the timeout the database will terminate the query and 
     * return an exception. Valid values are Integer or Strings that can be 
     * parsed to int values.
     * Some database platforms may not support lock timeouts, you may consider
     * setting a JDBC_TIMEOUT hint for these platforms.
     * @see #JDBC_TIMEOUT
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setWaitTimeout(Integer)
     */
    public static final String PESSIMISTIC_LOCK_TIMEOUT = "javax.persistence.lock.timeout";
    
    /**
     * "eclipselink.refresh"
     * <p>Configures the query to refresh the resulting objects in the cache and persistent context with the current state of the database.
     * This will also refresh the objects in the shared cache, unless a flush has occurred.
     * Any unflushed changes made to the objects will be lost (unless this query triggers a flush before execution).
     * The refresh will cascade relationships based on the REFRESH_CASCADE hint value.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE,
     * "" could be used instead of default value HintValues.FALSE
     * @see #REFRESH_CASCADE
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setShouldRefreshIdentityMapResult(boolean)
     */
    public static final String REFRESH = "eclipselink.refresh";
    
    /**
     * "eclipselink.batch"
     * <p>Configures the query to optimize the retrieval of the related objects,
     * the related objects for all the resulting objects will be read in a single query (instead of n queries).
     * Batch reading is normally more efficient than join fetch, especially for collection relationships.
     * Valid values are strings that represent JPQL style navigations to a relationship.
     * <p>e.g. e.manager.phoneNumbers
     * @see org.eclipse.persistence.queries.ReadAllQuery#addBatchReadAttribute(String)
     */
    public static final String BATCH = "eclipselink.batch";
    
    /**
     * "eclipselink.join-fetch"
     * <p>Configures the query to optimize the retrieval of the related objects,
     * the related objects will be joined into the query instead of being queried independently.
     * This allow for nested join fetching which is not supported in JPQL.
     * Valid values are strings that represent JPQL style navigations to a relationship.
     * <p>e.g. e.manager.phoneNumbers
     * @see #BATCH
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#addJoinedAttribute(String)
     */
    public static final String FETCH = "eclipselink.join-fetch";
    
    /**
     * "eclipselink.read-only"
     * <p>Configures the query to return shared (read-only) objects from the cache,
     * instead of objects registered with the persistence context.
     * This improves performance by avoiding the persistence context registration and change tracking overhead to read-only objects.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE,
     * "" could be used instead of default value HintValues.FALSE
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setIsReadOnly(boolean)
     */
    public static final String READ_ONLY = "eclipselink.read-only";
    
    /**
     * "eclipselink.jdbc.timeout"
     * <p>Configures the JDBC timeout of the query execution, if the database query exceeds the timeout
     * the database will terminate the query and return an exception.
     * Valid values are Integer or Strings that can be parsed to int values.
     * @see org.eclipse.persistence.queries.DatabaseQuery#setQueryTimeout(int)
     */
    public static final String JDBC_TIMEOUT = "eclipselink.jdbc.timeout";
    
    /**
     * "eclipselink.jdbc.fetch-size"
     * <p>Configures the JDBC fetch-size for the queries result-set.
     * This can improve the performance for queries that return large result-sets.
     * Valid values are Integer or Strings that can be parsed to int values.
     * @see org.eclipse.persistence.queries.ReadQuery#setFetchSize(int)
     */
    public static final String JDBC_FETCH_SIZE = "eclipselink.jdbc.fetch-size";
    
    /**
     * "eclipselink.jdbc.max-rows"
     * <p>Configures the JDBC max-rows, if the query returns more rows than the max-rows
     * the trailing rows will not be returned by the database.
     * This is the same as JPA Query setMaxResults(), but can be set in meta-data for NamedQuerys.
     * Valid values are Integer or Strings that can be parsed to int values.
     * @see javax.persistence.Query#setMaxResults(int)
     * @see org.eclipse.persistence.queries.ReadQuery#setMaxRows(int)
     */
    public static final String JDBC_MAX_ROWS = "eclipselink.jdbc.max-rows";
    
    /**
     * "eclipselink.jdbc.first-result"
     * <p>Configures the query to skip the firstResult number of rows.
     * This is the same as JPA Query setFirstResults(), but can be set in meta-data for NamedQuerys.
     * Valid values are Integer or Strings that can be parsed to int values.
     * @see javax.persistence.Query#setFirstResult(int)
     * @see org.eclipse.persistence.queries.ReadQuery#setFirstResult(int)
     */
    public static final String JDBC_FIRST_RESULT = "eclipselink.jdbc.first-result";
    
    /**
     * "eclipselink.result-collection-type"
     * <p>Configures the collection class implementation for the queries result.
     * The fully qualified class name must be used, without the .class.
     * Valid values are a Class representing a collection type or a String representing the class' name 
     * of the collection type.
     * If a Collection type that is not a List is used, getResultCollection() or getSingleResult() must be used instead of
     * getResultList().
     * <p>e.g. "java.util.ArrayList"
     * @see org.eclipse.persistence.jpa.JpaQuery#getResultCollection()
     * @see org.eclipse.persistence.queries.ReadAllQuery#useCollectionClass(Class)
     */
    public static final String RESULT_COLLECTION_TYPE = "eclipselink.result-collection-type";
    
    /**
     * "eclipselink.refresh.cascade"
     * <p>Defines if a refresh query should cascade the refresh to relationships.
     * Valid values are all declared in CascadePolicy class.
     * The REFRESH hint should also be set to cause a refresh.
     * @see CascadePolicy
     * @see #REFRESH
     * @see org.eclipse.persistence.queries.DatabaseQuery#setCascadePolicy(int)
     */
    public static final String REFRESH_CASCADE = "eclipselink.refresh.cascade";
    
    /**
     * "eclipselink.maintain-cache"
     * <p>Configures the query to not use both the shared cache, and the transactional cache/persistence context.
     * Resulting objects will be read and built directly from the database, and not registered in the persistence context.
     * Changes made to the objects will not be updated unless merged, object identity will not be maintained.
     * This can be used to read the current state of the database, without affecting the current persistence context.
     * By default the cache is always maintained.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE,
     * "" could be used instead of default value HintValues.FALSE
     * @see org.eclipse.persistence.queries.DatabaseQuery#setShouldMaintainCache(boolean)
     */
    public static final String MAINTAIN_CACHE = "eclipselink.maintain-cache";

    /**
     * "eclipselink.prepare"
     * <p>Configures the query to not prepare its SQL.
     * By default queries generate their SQL the first time they are executed,
     * and avoid the cost of generating the SQL on subsequent executions.
     * This can be used to generate the SQL on every execution if the 
     * query requires usage of dynamic SQL.
     * This only effects the SQL generation, not parameter binding or statement caching.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE,
     * "" could be used instead of default value HintValues.FALSE
     * @see org.eclipse.persistence.queries.DatabaseQuery#setShouldPrepare(boolean)
     */
    public static final String PREPARE = "eclipselink.prepare";

    /**
     * "eclipselink.jdbc.cache-statement"
     * <p>Configures if the query will cache its JDBC statement.
     * This allows queries to use parameterized SQL with statement caching.
     * It also allows a specific query to not cache its statement,
     * if statement caching is enable for the persistence unit.
     * If statement caching is desired, it should normally be set for the entire persistence unit
     * in the persistence.xml, not in each query.
     * If a DataSource is used statement caching must be set in the DataSource configuration,
     * not in EclipseLink.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE,
     * "" could be used instead of default value HintValues.FALSE
     * @see PersistenceUnitProperties#CACHE_STATEMENTS
     * @see org.eclipse.persistence.queries.DatabaseQuery#setShouldCacheStatement(boolean)
     */
    public static final String CACHE_STATMENT = "eclipselink.jdbc.cache-statement";
    
    /**
     * "eclipselink.flush"
     * <p>Configures if the query should trigger a flush of the persistence context before execution.
     * If the query may access objects that have been changed in the persistence context,
     * trigger a flush is required for the query to see these changes.
     * If the query does not require seeing the changes, then avoid the flush can improve performance.
     * The default flush-mode can be set on the EntityManager or configured as a persistence unit property.
     * By default the flush-mode is AUTO, which requires a flush before any query execution.
     * Conforming can also be used to query changes without requiring a flush,
     * refer to the CACHE_USAGE query hint for conforming.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE,
     * "" could be used instead of default value HintValues.FALSE
     * @see javax.persistence.EntityManager#setFlushMode(javax.persistence.FlushModeType)
     * @see PersistenceUnitProperties#PERSISTENCE_CONTEXT_FLUSH_MODE
     * @see org.eclipse.persistence.queries.DatabaseQuery#setFlushOnExecute(Boolean)
     */
    public static final String FLUSH = "eclipselink.flush";
    
    /**
     * "eclipselink.sql.hint"
     * <p>Sets an SQL hint string into the query.
     * An SQL hint can be used on certain database platforms to define how the query uses indexes
     * and other such low level usages.
     * This should be the full hint string including the comment delimiters.
     * @see org.eclipse.persistence.queries.DatabaseQuery#setHintString(String)
     */
    public static final String HINT = "eclipselink.sql.hint";

    /**
     * "eclipselink.jdbc.native-connection"
     * <p>Configures if the query requires a native JDBC connection.
     * This may be required for some queries on some server platforms that
     * have DataSource implementations that wrap the JDBC connection in their own proxy.
     * If the query requires custom JDBC access, it may require a native connection.
     * A ServerPlatform is required to be set as a persistence property to be able to use a native connection.
     * For features that are known by EclipseLink to require native connections this
     * will default to true, otherwise is false.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE,
     * "" could be used instead of default value HintValues.FALSE
     * @see PersistenceUnitProperties#TARGET_SERVER
     * @see org.eclipse.persistence.queries.DatabaseQuery#setIsNativeConnectionRequired(boolean)
     */
    public static final String NATIVE_CONNECTION = "eclipselink.jdbc.native-connection";
    
    /**
     * "eclipselink.cursor"
     * <p>Configures the query to return a CursoredStream.
     * A cursor is a stream of the JDBC ResultSet.
     * Cursor implements Enumeration, when the each next() will fetch the next from the JDBC ResultSet,
     * and build the resulting Object or value.
     * A Cursor requires and will keep a live JDBC connection, close() must be called
     * to free the Cursor's resources.
     * A Cursor can be accessed from a JPA Query through getSingleResult(), or from JpaQuery using getResultCursor().
     * Cursors are useful for large results sets, and if only the first few results are desired.
     * MAX_ROWS and FIRST_RESULT can also be used instead of cursors to obtain a page of results.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE,
     * "" could be used instead of default value HintValues.FALSE
     * @see org.eclipse.persistence.jpa.JpaQuery#getSingleResult()
     * @see org.eclipse.persistence.jpa.JpaQuery#getResultCursor()
     * @see org.eclipse.persistence.queries.Cursor
     * @see org.eclipse.persistence.queries.CursoredStream
     * @see org.eclipse.persistence.queries.ReadAllQuery#useCursoredStream()
     */
    public static final String CURSOR = "eclipselink.cursor";

    /**
     * "eclipselink.cursor.initial-size"
     * <p>Configures the query to return a CursoredStream with the initial threshold size.
     * The initial size is the initial number of objects that are prebuilt for the stream before a next() is called.
     * Valid values are Integer or Strings that can be parsed to int values.
     * @see org.eclipse.persistence.queries.ReadAllQuery#useCursoredStream(int, int)
     */
    public static final String CURSOR_INITIAL_SIZE = "eclipselink.cursor.initial-size";

    /**
     * "eclipselink.cursor.page-size"
     * <p>Configures the query to return a CursoredStream with the page size.
     * The page size is the number of objects that are fetched from the stream on a next() called,
     * if the buffer of objects is empty.
     * Valid values are Integer or Strings that can be parsed to int values.
     * @see org.eclipse.persistence.queries.ReadAllQuery#useCursoredStream(int, int)
     */
    public static final String CURSOR_PAGE_SIZE = "eclipselink.cursor.page-size";

    /**
     * "eclipselink.cursor.scrollable"
     * <p>Configures the query to return a ScrollableCursor.
     * A cursor is a stream of the JDBC ResultSet.
     * ScrollableCursor implements ListIterator, when the each next() will fetch the next from the JDBC ResultSet,
     * and build the resulting Object or value.
     * ScrollableCursor can scroll forwards and backwards and position into the ResultSet.
     * A Cursor requires and will keep a live JDBC connection, close() must be called
     * to free the Cursor's resources.
     * A Cursor can be accessed from a JPA Query through getSingleResult(), or from JpaQuery using getResultCursor().
     * Cursors are useful for large results sets, and if only some of the results are desired.
     * MAX_ROWS and FIRST_RESULT can also be used instead of cursors to obtain a page of results.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE,
     * "" could be used instead of default value HintValues.FALSE
     * @see org.eclipse.persistence.jpa.JpaQuery#getSingleResult()
     * @see org.eclipse.persistence.jpa.JpaQuery#getResultCursor()
     * @see org.eclipse.persistence.queries.Cursor
     * @see org.eclipse.persistence.queries.ScrollableCursor
     * @see org.eclipse.persistence.queries.ReadAllQuery#useScrollableCursor()
     */
    public static final String SCROLLABLE_CURSOR = "eclipselink.cursor.scrollable";

    /**
     * "eclipselink.cursor.size-sql"
     * <p>Configures the SQL string for the size query of a Cursor query.
     * This is only required for cursor queries that use native SQL or procedures.
     * The size query is only used if the size() is called on the Cursor.
     * The SQL should perform a COUNT of the rows returned by the original query.
     * @see #CURSOR
     * @see org.eclipse.persistence.queries.Cursor#size()
     * @see org.eclipse.persistence.queries.ReadAllQuery#useCursoredStream(int, int, org.eclipse.persistence.queries.ValueReadQuery)
     */
    public static final String CURSOR_SIZE = "eclipselink.cursor.size-sql";

    /**
     * "eclipselink.fetch-group.name"
     * <p>Configures the query to use a named fetch group defined for the result class.
     * This is the name of the fetch group, as defined on the ClassDescriptor.
     * Currently FetchGroups can only be defined on the ClassDescriptor using a DescriptorCustomizer.
     * The query will only fetch the attributes defined in the fetch group, if any other attribute is accessed
     * it will cause the object to be refreshed.
     * FetchGroups are only supported for queries returning objects (only a single alias can be the select clause).
     * Weaving is required to allow usage of fetch groups.
     * @see #FETCH_GROUP_ATTRIBUTE
     * @see #FETCH_GROUP_DEFAULT
     * @see org.eclipse.persistence.descriptors.FetchGroupManager
     * @see org.eclipse.persistence.queries.FetchGroup
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setFetchGroupName(String)
     */
    public static final String FETCH_GROUP_NAME = "eclipselink.fetch-group.name";
    
    /**
     * "eclipselink.fetch-group.attribute"
     * <p>Configures the query to use a dynamic fetch group that includes a list of attributes.
     * Each attribute must be defined using a separate hint.
     * The primary key and version are always included.
     * The query will only fetch the attributes defined in the fetch group, if any other attribute is accessed
     * it will cause the object to be refreshed.
     * FetchGroups are only supported for queries returning objects (only a single alias can be the select clause).
     * Weaving is required to allow usage of fetch groups.
     * Only local attributes are supported, nested attributes are not supported.
     * @see #FETCH_GROUP_NAME
     * @see #FETCH_GROUP_DEFAULT
     * @see org.eclipse.persistence.queries.FetchGroup
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setFetchGroup(org.eclipse.persistence.queries.FetchGroup)
     */
    public static final String FETCH_GROUP_ATTRIBUTE = "eclipselink.fetch-group.attribute";
    
    /**
     * "eclipselink.fetch-group.default"
     * <p>Configures the query not to use the default fetch group.
     * The default fetch group is defined by all non-lazy Basic mappings.
     * If set to FALSE all attributes will be fetched, including lazy Basics,
     * this still excludes lazy relationships, they will fetch their foreign keys, but not their values.
     * FetchGroups are only supported for queries returning objects (only a single alias can be the select clause).
     * Weaving is required to allow usage of fetch groups.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE,
     * "" could be used instead of default value HintValues.TRUE
     * @see #FETCH_GROUP_NAME
     * @see #FETCH_GROUP_ATTRIBUTE
     * @see org.eclipse.persistence.queries.FetchGroup
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setShouldUseDefaultFetchGroup(boolean)
     */
    public static final String FETCH_GROUP_DEFAULT = "eclipselink.fetch-group.default";

    /**
     * "eclipselink.exclusive-connection"
     * <p>Configures the query to use the exclusive (transactional/write) connection.
     * This is only relevant if a EXCLUSIVE_CONNECTION_MODE property has been set for the persistence unit (such as VPD).
     * If an EXCLUSIVE_CONNECTION_MODE has been configured,
     * this will ensure that the query is executed through the exclusive connection.
     * This may be required in certain cases.  An example being
     * where database security will prevent a query joining to a secure table
     * from returning the correct results when executed through the shared
     * connection.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE.
     * @see PersistenceUnitProperties#EXCLUSIVE_CONNECTION_MODE
     * @see org.eclipse.persistence.sessions.server.ConnectionPolicy#setExclusiveMode(org.eclipse.persistence.sessions.server.ConnectionPolicy.ExclusiveMode)
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setShouldUseExclusiveConnection(boolean)
     */
    public static final String EXCLUSIVE_CONNECTION = "eclipselink.exclusive-connection";
    
    /**
     * "eclipselink.inheritance.outer-join"
     * <p>Configures the query to outer-join all subclasses.
     * This is only relevant for queries to root or branch inherited classes.
     * By default a separate query is executed for each subclass.
     * This can also be configured for the class using a DescriptorCustomizer.
     * This is required for correct ordering, firstResult, maxResult, and cursors.
     * Valid values are:  HintValues.FALSE, HintValues.TRUE.
     * @see org.eclipse.persistence.descriptors.InheritancePolicy#setShouldOuterJoinSubclasses(boolean)
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setShouldOuterJoinSubclasses(boolean)
     */
    public static final String INHERITANCE_OUTER_JOIN = "eclipselink.inheritance.outer-join";

    /**
     * "eclipselink.history.as-of"
     * <p>Configures the query to query the state of the object as-of a point in time.
     * This can only be used if the class has been configured with historical support,
     * or if Oracle Flashback is used.
     * Valid values are timestamps in the form "YYYY/MM/DD HH:MM:SS.n".
     * @see org.eclipse.persistence.descriptors.HistoryPolicy
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setAsOfClause(org.eclipse.persistence.history.AsOfClause)
     */
    public static final String AS_OF = "eclipselink.history.as-of";
    
    /**
     * "eclipselink.history.as-of.scn"
     * <p>Configures the query to query the state of the object as-of a database SCN (System Change Number).
     * This can only be used with Oracle Flashback support.
     * Valid values are Integer SCN values.
     * @see org.eclipse.persistence.queries.ObjectLevelReadQuery#setAsOfClause(org.eclipse.persistence.history.AsOfClause)
     */
    public static final String AS_OF_SCN = "eclipselink.history.as-of.scn";

}
