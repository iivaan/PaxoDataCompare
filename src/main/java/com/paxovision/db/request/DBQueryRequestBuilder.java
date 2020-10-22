package com.paxovision.db.request;

import com.paxovision.db.Request;
import com.paxovision.db.Source;
import com.paxovision.db.actor.DBClientActor.DatabaseType;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** DB Query Request Builder that is accepting DataSource and query */
public class DBQueryRequestBuilder extends DBGenericRequestBuilder<DBQueryRequestBuilder> {
    private Request request;

    public DBQueryRequestBuilder(Source dataSource, DatabaseType type) {
        super(dataSource, type);
    }

    private void executeRequest() {
        Objects.requireNonNull(query, "Query cannot be null");
        if (request == null) {
            this.request = new Request(source, query);
        }
    }

    /**
     *   Store the SQL result to output stream
    *   @param outStream Output Stream
    *   @param delimiter Delimiter for the column and row values
    */
//    public void storeResult(OutputStream outStream, String delimiter) {
//        executeRequest();
//        Outputs.output(request)
//                .withType(RawDataOutput.INSTANCE.withDelimiter(delimiter))
//                .toStream(outStream);
//    }

    /**
    *	Assertion for the SQL request result
    *	@param checkers all the checks to be performed for the response
    */
//    public <T> List<Map<String, T>> expect(
//            Function<DBResponseMatchers, DBResponseAsserter> checkers) {
//
//        AtomicReference<ResponseExtractor> extractor = new AtomicReference<>();
//        executeRequest();
//        checkers.apply(new DBResponseMatchers(request, this.renderAsString, extractor)).assertAll();
//        return extractor.get() != null
//                ? (List<java.util.Map<String, T>>) extractor.get().getValue()
//                : null;
//    }


    @Override
    protected DBQueryRequestBuilder self(){
        return this;
    }
}
