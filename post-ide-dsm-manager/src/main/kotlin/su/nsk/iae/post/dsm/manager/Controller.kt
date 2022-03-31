package su.nsk.iae.post.dsm.manager

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import su.nsk.iae.post.dsm.manager.common.Logger

@RestController
class Controller {
    @RequestMapping(value = [""])
    fun hello(): String {
        Logger.info(Controller::class.java, "request for /")
        return "Hello, this is poST IDE DSM-manager<br>" +
                "My API:<br>" +
                "TODO"
    }
}