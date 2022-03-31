package su.nsk.iae.post.dsm.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import su.nsk.iae.post.dsm.manager.common.Logger;

@SpringBootApplication
public class App {
    public static void main(String[] args){
        Logger.info(
                App.class,
                "use -help to see available running configurations"
        );

        for (String arg : args) {
            if ("-help".equals(arg)) {
                help();
                return;
            }
        }

        SpringApplication.run(App.class);
    }

    private static void help() {
        Logger.info(App.class, "available running configurations:");
        Logger.info(App.class, "java -Dserver.port=<port-number> -jar <jar-name>");
    }
}