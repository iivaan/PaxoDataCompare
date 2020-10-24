package com.paxovision.db.comparator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class JvmUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JvmUtil.class);

    private JvmUtil() {}

    @SuppressWarnings("squid:S1215")
    public static void forceGc() {
        Runtime.getRuntime().gc();
    }

    public static void performGcAndPrintMemoryStats() {
        forceGc();
        printMemoryStats() ;
    }

    public static void printMemoryStats() {
        int mb = 1024 * 1024;
        // get Runtime instance
        Runtime instance = Runtime.getRuntime();
        long totalMemory = (instance.totalMemory() / mb);
        long freeMemory = instance.freeMemory() / mb;
        long usedMemory = (instance.totalMemory() - instance.freeMemory()) /mb;
        long maxMemory = instance.maxMemory() / mb;
        // available memory

        LOGGER.debug(
                "*****Total Memory (MB): {} , Free Memory (MB)= {} , Used Memory (MB)= {} , Max Memory (MB)= {}*****",
                totalMemory,
                freeMemory,
                usedMemory,
                maxMemory);
    }

}
