package com.paxovision.db.comparator.csv;

import com.paxovision.db.exception.RaptorException;
import com.paxovision.db.comparator.util.CollectionUtil;
import com.opencsv.CSVParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class CsvLinesToMapListConverter {

    private String enclosingCharacter;
    private final String delimiter;
    private final List<String> headers;

    public CsvLinesToMapListConverter(String delimiter) {
        this(delimiter, null);
    }

    public CsvLinesToMapListConverter(String delimiter, String enclosingCharacter) {
        this(delimiter, null, enclosingCharacter);
    }

    public CsvLinesToMapListConverter(String delimiter, List<String> headers, String enclosingCharacter) {
        this.delimiter = delimiter;
        this.headers = headers;
        this.enclosingCharacter = enclosingCharacter;
        if (StringUtils.isBlank(enclosingCharacter)) {
            this.enclosingCharacter = "\"";
        }
    }

    private String[] getHeaderVal(String line) {
        CSVParser parser = new CSVParser(getDelimiter().charAt(0), enclosingCharacter.charAt(0));
        try {
            return parser.parseLine(line);
        } catch (IOException ioe) {
            String errorMsg =
                    "Error parsing the header line:"
                            + "\nline="
                            + line
                            + "; delimiters"
                            + this.getDelimiter()
                            + "\nenclosingCharacter= "
                            + enclosingCharacter;
            throw new RaptorException(errorMsg);
        }
    }

    public String getDelimiter() {
        return this.delimiter;
    }


    public List<String> getHeaders() {
        return headers;
    }

    public static List<String> toList(String[] args) {
        List<String> list = new ArrayList<>();
        if (args != null) {
            return Arrays.asList(args);
        }
        return list;
    }

    public List<Map<String, String>> convert(List<String> lines) {

        List<Map<String, String>> mapList = new ArrayList<>();
        if (!CollectionUtil.isEmptyList(lines)) {
            int linelndex = 0;
            List<String> headerCols = null;
            if (this.getHeaders() == null || this.getHeaders().isEmpty()) {
                String hLine = lines.get(linelndex++);
                headerCols = toList(getHeaderVal(hLine));
            } else {
                headerCols = this.getHeaders();
            }
            CsvToMapConverter csvToMapConverter =
                    new CsvToMapConverter(getDelimiter(), headerCols, enclosingCharacter);
            for (; linelndex < lines.size(); linelndex++) {
                String line = lines.get(linelndex);
                Map<String, String> map = csvToMapConverter.convert(line);
                mapList.add(map);
            }
        }
        return mapList;
    }
}