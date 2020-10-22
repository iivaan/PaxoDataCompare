package com.paxovision.raptor;

import static org.assertj.db.api.Assertions.assertThat;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.assertj.core.util.diff.Delta;
import org.assertj.core.util.diff.DiffUtils;
import org.assertj.core.util.diff.Patch;
import org.assertj.db.type.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class AsserJDBDiffTest {



    @BeforeEach
    public void setUp(){

    }



    @Test
    public void queryTest(){
        DiffUtils diffUtils = new DiffUtils();

        List<String> list1 = new ArrayList<>();
        List<String> list2 = new ArrayList<>();
        list1.add("Iftekhar");
        list1.add("Shehla");
        list1.add("Ameera");

        list2.add("Iftekhar");
        list2.add("Ameer");
        list2.add("Simrohn");
        list2.add("Ameera");

        Patch<String> patch =  DiffUtils.diff(list1,list2);
        List<Delta<String>> deltas =   patch.getDeltas();
        for(Delta<String> d : deltas) {
            System.out.println(d.getType());
            System.out.println(d.toString());
        }

    }


    @AfterEach
    public void tearDown(){

    }


}
