package com.paxovision.db.request;

import com.paxovision.db.Source;
import com.paxovision.db.actor.DBClientActor;
import com.paxovision.db.actor.DBClientActor.DatabaseType;
import java.util.Objects;

public abstract class DBGenericRequestBuilder <S extends DBGenericRequestBuilder<S>> {

    protected String query;
    protected DatabaseType type;
    protected Source source;
    private static final String KDB_Q_PREFIX = "q)";
    protected int queryTimeout;
    protected boolean renderAsString = false;

    public DBGenericRequestBuilder(Source source, DatabaseType type) {
        this.source = source;
        this.type = type;
    }


    /* Indicate to query timeout when executing jdbc query
     *   @param queryTimeout in seconds
     *   @return self
     */
    public S withQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
        return self();
    }

    /**
     *  Indicate to render all result as String. Use this options if you do want to deal with native
     *	data type
    *	@param renderAsString
    *	@return self
    */
    public S withRenderAsString(boolean renderAsString) {
        this.renderAsString = renderAsString;
        return self();
    }

    public S withQuery(String query) {
        Objects.requireNonNull(query, "Query cannot be null");
        if (type == DatabaseType.KDB) {
            this.query = query.startsWith(KDB_Q_PREFIX) ? query : KDB_Q_PREFIX + query;
        } else {
            this.query = query;
        }
        return self();
    }

    protected abstract S self();
}