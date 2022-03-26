package su.nsk.iae.post.dsm.manager.common;

import org.apache.log4j.Logger;

public class DSMMLogger {

    private static final Logger log = Logger.getLogger(DSMMLogger.class);

    public static void info(Class c, String message) {
        System.out.println(c.getSimpleName() + ":\t" + message);
        log.info(c.getSimpleName() + ":\t" + message);
    }

    public static void error(Class c, String message) {
        System.out.println(c.getSimpleName() + ":\t" + message);
        log.error(c.getSimpleName() + ":\t" + message);
    }
}