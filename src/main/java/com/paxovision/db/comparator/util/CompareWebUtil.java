package com.paxovision.db.comparator.util;

//import com.epam.reportportal.service.ReportPortal;
//import com.mlp.raptor.RaptorException;
import com.paxovision.db.exception.RaptorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class CompareWebUtil {
    private CompareWebUtil( ) {}

    /**
     *	Uploading link details to RP . we need to upload as attachment link and auto redirect because
     *	rp doesnt render the html link
     *
     * @param comparatorWebUiURL
     */
    public static void uploadDiffResultLinkToRP(String comparatorWebUiURL) {
        String template =
                "< html >< head ></head >< body <hl><a id= 'diffresult' style='display:none' href='%s'>Link</a></body><script> document.getElementById('diffresult').click(); </script></html>";
        String tmpDir = System.getProperty("java.io.tmpdir");
        String uniqueId = UUID.randomUUID().toString();
        try {
            File file = new File(String.format("%s/%s.html", tmpDir, uniqueId));
            com.google.common.io.Files.touch(file);
            Files.write(
                    Paths.get(file.getAbsolutePath()),
                    String.format(template, comparatorWebUiURL).getBytes());
//            ReportPortal.emitLog(
//                    "Uploaded link attachment for diff result ",
//                    "INFO",
//                    new java.util.Date(),
//                    file);
            file.deleteOnExit();
        } catch (IOException ioe) {
            throw new RaptorException("Not able to upload diff result link to RP", ioe);
        }
    }

}


