package com.paxovision.db.comparator.csv;

import com.paxovision.db.exception.RaptorException;
import com.paxovision.db.comparator.util.CollectionUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseCsvToMapConverter {
    private static	final	Logger LOGGER = LoggerFactory.getLogger(BaseCsvToMapConverter.class);
    private static	final	Map<String,	String>	ASCII_ESCAPE_CHARACTERS;
    private static	final	Map<String,	String>	ASCII_CARET_NOTATION_CHARACTERS;
    private final String	delimiter;
    private final List<String> headers;

    public String getDelimiter() {
        return delimiter;
    }

    static {
        ASCII_ESCAPE_CHARACTERS = new HashMap<>();
        ASCII_ESCAPE_CHARACTERS.put ( "\\0" ,"\0");
        ASCII_ESCAPE_CHARACTERS.put("\\a","\u0007");
        ASCII_ESCAPE_CHARACTERS.put("\\b","\u0008");
        ASCII_ESCAPE_CHARACTERS.put("\\t","\u0009");
        ASCII_ESCAPE_CHARACTERS.put("\\n" ,"\n");
        ASCII_ESCAPE_CHARACTERS.put("\\v","\u000B");
        ASCII_ESCAPE_CHARACTERS.put("\\f","\u000C");
        ASCII_ESCAPE_CHARACTERS.put("\\r","\r");
        ASCII_ESCAPE_CHARACTERS.put("\\e","\u001B");

        ASCII_CARET_NOTATION_CHARACTERS = new HashMap<>();
        ASCII_CARET_NOTATION_CHARACTERS.put("^@", "\0");
        for (char c = 'A', i = 1; c <= 'Z'; c++, i++) {
            ASCII_CARET_NOTATION_CHARACTERS.put("^" + c, String.valueOf((char) i));
        }
        ASCII_CARET_NOTATION_CHARACTERS.put("^[","\u001B");
        ASCII_CARET_NOTATION_CHARACTERS.put("^\\","\u001C");
        ASCII_CARET_NOTATION_CHARACTERS.put("^]","\u001D");
        ASCII_CARET_NOTATION_CHARACTERS.put("^^","\u001E");
        ASCII_CARET_NOTATION_CHARACTERS.put("^_","\u001F");
        ASCII_CARET_NOTATION_CHARACTERS.put("^?","\u001F");
    }

    public BaseCsvToMapConverter(String delimiter, List<String> headers) {
        delimiter = handleSpecialCharacters(delimiter);
        this.delimiter = delimiter;
        this.headers = headers;
    }

    public static String handleSpecialCharacters(String delimiter) {
        if ("|".equals(delimiter)) { // IMPORTANT
            delimiter = Pattern.quote(delimiter);
        } else if (ASCII_ESCAPE_CHARACTERS.containsKey(delimiter)) {
            delimiter = ASCII_ESCAPE_CHARACTERS.get(delimiter);
        } else if (ASCII_CARET_NOTATION_CHARACTERS.containsKey(delimiter)) {
            delimiter = ASCII_CARET_NOTATION_CHARACTERS.get(delimiter);
        }
        return delimiter;
    }

    public Map<String, String> convert(String line) {

        String[] valueList = line.split(delimiter, -1);
        checkNumberOfAttributesInLineAgainstHeaders(line, valueList);
        return getMapFromValueList(valueList);
    }

    protected void checkNumberOfAttributesInLineAgainstHeaders(String line, String[] valueList) {
        if (valueList.length != headers.size()) {
            String errorMsg =
                    "CSV line has different number of fields from header:"
                            + "\nheader.length="
                            + headers.size()
                            + "; line.length="
                            + valueList.length
                            + "\ndelimiter="
                            + delimiter
                            + "\nheader="
                            + CollectionUtil.joinCollection(headers, "\t")
                            + "\nline="
                            + line;
            LOGGER.error(errorMsg);
            throw new RaptorException(errorMsg);
        }
    }

    protected Map<String, String> getMapFromValueList(String[] valueList) {
        Map<String, String> map = new TreeMap<>();
        for (int index = 0; index < headers.size(); index++) {
            String key = headers.get(index);
            String value = valueList[index].intern();
            map.put(key, value);
        }
        return map;
    }

}
