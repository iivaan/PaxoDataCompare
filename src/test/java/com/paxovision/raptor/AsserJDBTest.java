package com.paxovision.raptor;

//import static org.assertj.db.api.Assertions.assertThat;
//
//import com.mysql.cj.jdbc.MysqlDataSource;
//import org.assertj.db.type.*;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import javax.sql.DataSource;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.List;
//import java.util.Properties;


public class AsserJDBTest {
//
//    private DataSource dataSource = null;
//    //private FluentJdbc fluentJdbc = null;
//
//    @BeforeEach
//    public void setUp(){
//        dataSource = getMySQLDataSource();
//    }
//
//    @Test
//    public void tableTest(){
//        Table table = new Table(dataSource,"Customer" );
//
//        List<String> columnNames =  table.getColumnsNameList();
////        for(String name : columnNames){
////            System.out.println(name);
////        }
//
//        List<Row> rows = table.getRowsList();
//        for(Row row : rows){
//            List<Value> values = row.getValuesList();
//            for(Value v : values){
//                System.out.println(v.getColumnName() + ": " + v.getValue());
//            }
//            System.out.println("************************************");
//        }
//
//
//
//
////        assertThat(table)
////                .column("customerName")
////                .value().isEqualTo("Atelier graphique")
////                .value().isEqualTo("Signal Gift Stores")
////        ;
//
//    }
//
//    @Test
//    public void queryTest(){
//        String sql = "Select customerNumber,customerName,contactLastName,contactFirstName from Customer where state = 'NY'";
//        Request request = new Request(dataSource,sql);
//
//        List<String> columnNames =  request.getColumnsNameList();
////        for(String name : columnNames){
////            System.out.println(name);
////        }
//
//        List<Row> rows = request.getRowsList();
//        for(Row row : rows){
//            List<Value> values = row.getValuesList();
//            for(Value v : values){
//                System.out.println(v.getColumnName() + ": " + v.getValue());
//            }
//            System.out.println("************************************");
//        }
//
//
//    }
//
//
//    @AfterEach
//    public void tearDown(){
//
//    }
//
//    public  DataSource getMySQLDataSource() {
//        Properties props = new Properties();
//        FileInputStream fis = null;
//        MysqlDataSource mysqlDS = null;
//        try {
//            fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/" +  "db.properties");
//            props.load(fis);
//            mysqlDS = new MysqlDataSource();
//            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
//            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
//            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return mysqlDS;
//    }

}
