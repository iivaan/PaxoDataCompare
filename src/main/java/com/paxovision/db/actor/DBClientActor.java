package com.paxovision.db.actor;

import com.paxovision.db.Source;
import com.paxovision.db.exception.RaptorException;
import com.paxovision.db.request.DBQueryRequestBuilder;
import org.parboiled.common.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

public class DBClientActor {

    public enum DatabaseType {
        H2("org.h2.Driver", "jdbc:h2:%s:%s"),
        MYSQL("com.mysql.jdbc.Driver", "jdbc:mvsql://%s:%d/%s"),
        KDB("kx.jdbc", "jdbc:q:%s:%d"),
        SQLSERVER("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://%s:%d;databaseName=%s"),
        SQLServer_IntegratedSecurity("com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://%s:%d;databaseName=%s;inteqratedSecuritv=true"),
        VERTICA("com.vertica.jdbc.Driver", "jdbc:vertica://%s:%d/%s"),
        ORACLE("oracle.jdbc.OracleDriver", "jdbc:oracle : thin:@%s:%d:%s"),
        SYSBASE("com.Sybase.jdbc4.jdbc.SybDriver", "jdbc: Sybase :Tds:%s:%d/%s"),
        APACHEDRILL("org.apache.drill.jdbc.Driver", "jdbc: drill:zk=%s:%d/drill/%s"),
        HANA("com.sap.db.jdbc.Driver", "jdbc: sap ://%s:%d/%s"),
        REDSHIFT("com.amazon.redshift.jdbc42.Driver", "jdbc: redshift ://%s:%d/%s");

        private final String driverClassName;
        private final String connectionUrlTemplate;

        private DatabaseType(String driverClassName, String connectionUrlTemplate) {
            this.driverClassName = driverClassName;
            this.connectionUrlTemplate = connectionUrlTemplate;
        }

        /**
         * Driver needs to be loaded explicitly as it doesn't work for Drill
         */
        protected void ensureDriverClassIsRegisteredIfFound() {
            try {
                Class.forName(driverClassName);
            } catch (ClassNotFoundException e) {
                throw new RaptorException("Error initialize the driver", e);
            }
        }
    }

    private final Source source;
    private final DatabaseType dbType;

    public DBClientActor(DatabaseType databaseType, String userName,
                         String password, String connectionUrl,
                         String libraryPath) {

        Objects.requireNonNull(connectionUrl, "Connection URL cannot be null");
        if (databaseType != null) {
            databaseType.ensureDriverClassIsRegisteredIfFound();
            connectionUrl = resolveConnectionUrl(databaseType, connectionUrl);
        }

        if (StringUtils.isNotEmpty(libraryPath)) {
            setLibraryPath(libraryPath);
        }

        this.source = new Source(connectionUrl, userName, password);
        this.dbType = databaseType;
    }

    /**
     * This is needed for SQLServerlntegratedSecurity For now , not able to find the best way to
     * load the dll inside the jars , so users need to supply their own path
     *
     * @param path
     */
    private void setLibraryPath(String path) {
        System.setProperty("java.library.path", path);
        try {
            final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch (IllegalAccessException
                | IllegalArgumentException
                | NoSuchFieldException
                | SecurityException e) {
            throw new RaptorException("Failed to set library path ", e);
        }

    }

    private String resolveConnectionUrl(DatabaseType databaseType, String connectionUrl) {
        if (connectionUrl.startsWith("jdbc")) {
            return connectionUrl;
        }
        String connectionUrlResolved = connectionUrl;
        String[] connectionUrlParts = connectionUrl.split(":");

        if (databaseType != null) {
            switch (connectionUrlParts.length) {
                case 2:
                    connectionUrlResolved =
                            String.format(
                                    databaseType.connectionUrlTemplate,
                                    connectionUrlParts[0],
                                    Integer.parseInt(connectionUrlParts[1]));
                    break;

                case 3:
                    connectionUrlResolved =
                            String.format(
                                    databaseType.connectionUrlTemplate,
                                    connectionUrlParts[0],
                                    Integer.parseInt(connectionUrlParts[1]),
                                    connectionUrlParts[2]);
                    break;

                default:
                    throw new RaptorException("Invalid Connection URL");
            }
        }
        return connectionUrlResolved;
    }

    /**
     * Fluent interface for constructing DML SQL request
     *
     * @return DBUpdateRequestBuilder instance
     */
//    public DBUpdateRequestBuilder newUpdate() {
//        return new DBUpdateRequestBuilder(source);
//    }

    /**
     * Fluent interface for constructing Request SQL Query
     *
     * @return DBQueryRequestBuilder instance
     */

    public DBQueryRequestBuilder newRequest() {
        return new DBQueryRequestBuilder(source, dbType);
    }

    /**
     * Fluent interface for constructing KDB SQL Query
     *
     * @return KDBQueryRequestBuilder instance
     */
//    public KDBQueryRequestBuilder newKDBRequest() {
//        return new KDBQueryRequestBuilder(source, dbType);
//    }
}
