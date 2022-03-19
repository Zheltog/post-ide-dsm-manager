package su.nsk.iae.post.dsm.manager;

import org.apache.log4j.Logger;

public class DSMManagerLogger {

    private static final Logger log = Logger.getLogger(DSMManagerLogger.class);

    public static void info(Class c, String message) {
        System.out.println(c.getSimpleName() + ":\t" + message);
        log.info(message);
    }

    public static void error(Class c, String message) {
        System.out.println(c.getSimpleName() + ":\t" + message);
        log.error(message);
    }
}