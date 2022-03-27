package su.nsk.iae.post.dsm.manager.common;

public class Logger {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Logger.class);

    public static void info(Class c, String message) {
        System.out.println(c.getSimpleName() + ": " + message);
        log.info(c.getSimpleName() + ": " + message);
    }

    public static void error(Class c, String message) {
        System.out.println(c.getSimpleName() + ": " + message);
        log.error(c.getSimpleName() + ": " + message);
    }
}