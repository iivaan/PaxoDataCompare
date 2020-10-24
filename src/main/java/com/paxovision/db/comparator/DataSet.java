package com.paxovision.db.comparator;

import com.paxovision.db.exception.RaptorException;
import com.paxovision.db.comparator.csv.CsvLinesToMapListConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.karneim.pojobuilder.GeneratePojoBuilder;

@GeneratePojoBuilder
public class DataSet<K> {

    private final List<K> data = new ArrayList<>();

    public DataSet(List<K> data, K dataAsMap, K dataAsJson, String filePath, String fileDelimiter) {
        this.data.clear();

        if (data != null) {
            this.data.addAll(data);
            data.clear();
        } else if (dataAsMap != null) {
            this.data.add(dataAsMap);
        } else if (dataAsJson != null) {
            this.data.add(dataAsJson);
        } else if (filePath != null && fileDelimiter != null) {
            CsvLinesToMapListConverter csvLinesToMapListConverter =
                    new CsvLinesToMapListConverter(fileDelimiter);
            try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
                List<String> linesList = lines.collect(Collectors.toList());
                List dataList = csvLinesToMapListConverter.convert(linesList);
                this.data.addAll(dataList);
            } catch (IOException ex) {
                throw new RaptorException("Failed to read file! ", ex);
            }
        }
    }


    public List<K> getDataChunk(int start, int chunkSize) {
        if (start > data.size()) {
            return new ArrayList<>();
        }
        int toIndex = start + chunkSize;
        if (toIndex > data.size()) {
            toIndex = data.size();
        }
        return data.subList(start, toIndex);
    }

    public void sort(Comparator comparator) {
        Collections.sort(data, comparator);
    }

    public int getSize() {
        return data.size();
    }

    public void clear() {
        data.clear();
    }

    public List<K> getData() {
        return data;
    }
}
