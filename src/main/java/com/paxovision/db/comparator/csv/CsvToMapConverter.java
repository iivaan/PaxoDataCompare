package com.paxovision.db.comparator.csv;

import com.paxovision.db.exception.RaptorException;
import com.opencsv.CSVParser;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CsvToMapConverter extends BaseCsvToMapConverter{

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvToMapConverter.class);
    private final String enclosingCharacter;

    public CsvToMapConverter(String delimiter, List<String> headers, String enclosingCharacter) {
        super(delimiter, headers);
        this.enclosingCharacter = enclosingCharacter;
    }

    @Override
    public Map<String, String> convert(String line) {
        String delimiter = getDelimiter();
        // open csv cannot handle null character as delimiter for now
        if (delimiter.charAt(0) == CSVParser.NULL_CHARACTER) {
            return super.convert(line);
        }

        try {
            CSVParser parser = new CSVParser(delimiter.charAt(0), enclosingCharacter.charAt(0));
            String[] valueList = parser.parseLine(line);
            super.checkNumberOfAttributesInLineAgainstHeaders(line, valueList);
            return super.getMapFromValueList(valueList);
        } catch (IOException ioe) {
            String errorMsg =
                    "Error parsing the line:"
                            + "\nline="
                            + line
                            + "; delimiter="
                            + super.getDelimiter()
                            + "\nenclosingCharacter= "
                            + enclosingCharacter;

            LOGGER.error(errorMsg);
            throw new RaptorException(errorMsg);
        }
    }

}
