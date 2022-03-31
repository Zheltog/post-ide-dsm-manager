package su.nsk.iae.post.dsm.manager;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.nsk.iae.post.dsm.manager.common.Logger;

@RestController
public class DemoController {
    @RequestMapping(value = "hello")
    public String hello(){
        Logger.info(DemoController.class, "ab");
        return "i'm alive";
    }
}