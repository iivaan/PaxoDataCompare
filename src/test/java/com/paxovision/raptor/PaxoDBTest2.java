package com.paxovision.raptor;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.paxovision.db.*;
import com.paxovision.db.lettercase.CaseComparisons;
import com.paxovision.db.lettercase.CaseConversions;
import com.paxovision.db.lettercase.LetterCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class PaxoDBTest2 {

    private DataSource dataSource = null;
    //private FluentJdbc fluentJdbc = null;

    @BeforeEach
    public void setUp(){
        LetterCase columnLetterCase = LetterCase.getLetterCase(CaseConversions.LOWER, CaseComparisons.IGNORE);
        dataSource = getMySQLDataSource();

    }

    @Test
    public void tableTest(){
        Table table = new Table(dataSource,"Customer" );

        List<String> columnNames =  table.getColumnsNameList();
//        for(String name : columnNames){
//            System.out.println(name);
//        }

        List<Row> rows = table.getRowsList();
        for(Row row : rows){
            List<Value> values = row.getValuesList();
            for(Value v : values){
                System.out.println(v.getColumnName() + ": " + v.getValue());
            }
            System.out.println("************************************");
        }




//        assertThat(table)
//                .column("customerName")
//                .value().isEqualTo("Atelier graphique")
//                .value().isEqualTo("Signal Gift Stores")
//        ;

    }

    @Test
    public void queryTest(){
        LetterCase tableLetterCase = LetterCase.getLetterCase(CaseConversions.LOWER, CaseComparisons.IGNORE);
        LetterCase columnLetterCase = LetterCase.getLetterCase(CaseConversions.NO, CaseComparisons.IGNORE);
        LetterCase pkLetterCase = LetterCase.getLetterCase(CaseConversions.LOWER, CaseComparisons.IGNORE);
        DataSource dataSourceWithLC = new DataSourceWithLetterCase(dataSource,
                tableLetterCase,
                columnLetterCase,
                pkLetterCase);

        String sql = "Select customerNumber,customerName,contactLastName,contactFirstName from Customer where state = 'NY'";
        //Request request = new Request(dataSource,sql);
        Request request = new Request(dataSourceWithLC,sql);

        //List<String> columnNames =  request.getColumnsNameList();
//        for(String name : columnNames){
//            System.out.println(name);
//        }

        List<Row> rows = request.getRowsList();
        for(Row row : rows){
            List<Value> values = row.getValuesList();
            for(Value v : values){
                System.out.println(v.getColumnName() + ": " + v.getValue());
            }
            System.out.println("************************************");
        }


    }

    @Test
    public void queryTest2(){
        String sql = "Select customerNumber,customerName,contactLastName,contactFirstName from Customer where state = ?";
        Request request = new Request(dataSource,sql,"NY");

        List<String> columnNames =  request.getColumnsNameList();
//        for(String name : columnNames){
//            System.out.println(name);
//        }

        List<Row> rows = request.getRowsList();
        for(Row row : rows){
            List<Value> values = row.getValuesList();
            for(Value v : values){
                System.out.println(v.getColumnName() + ": " + v.getValue());
            }
            System.out.println("************************************");
        }


    }


    @AfterEach
    public void tearDown(){

    }

    public  DataSource getMySQLDataSource() {
        Properties props = new Properties();
        FileInputStream fis = null;
        MysqlDataSource mysqlDS = null;
        try {
            fis = new FileInputStream(System.getProperty("user.dir") + "/src/test/resources/" +  "db.properties");
            props.load(fis);
            mysqlDS = new MysqlDataSource();
            mysqlDS.setURL(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }

}
