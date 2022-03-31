package su.nsk.iae.post.dsm.manager;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.nsk.iae.post.dsm.manager.common.Logger;

@RestController
public class Controller {
    @RequestMapping(value = "")
    public String hello(){
        Logger.info(Controller.class, "request for /");
        return "Hello, this is poST IDE DSM-manager<br>" +
                "My API:<br>" +
                "TODO";
    }
}