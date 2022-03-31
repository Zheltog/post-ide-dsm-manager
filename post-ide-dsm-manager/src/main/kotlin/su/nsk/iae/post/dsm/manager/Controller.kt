package su.nsk.iae.post.dsm.manager

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import su.nsk.iae.post.dsm.manager.common.Logger
import su.nsk.iae.post.dsm.manager.requests.NewModuleRequest
import su.nsk.iae.post.dsm.manager.responses.ModulesListResponse
import su.nsk.iae.post.dsm.manager.responses.NewModuleResponse
import javax.servlet.http.HttpServletRequest

@RestController
class Controller {
    @RequestMapping(value = [""])
    fun hello(): String {
        Logger.info(Controller::class.java, "request for /")
        return "Hello, this is poST IDE DSM-manager<br>" +
                "My API:<br>" +
                "1) /new-module<br>" +
                "request: {\"name\": \"new-dsm-name\"}<br>" +
                "response: {\"freePort\": \"port-for-your-dsm\"}<br>" +
                "2) /modules<br>" +
                "request: empty<br>" +
                "response: connected modules list<br>"
    }

    @PostMapping(value = ["new-module"])
    fun newModule(@RequestBody nmr: NewModuleRequest, r: HttpServletRequest): NewModuleResponse? {
        Logger.info(Controller::class.java, "request for /new-module")
        val dsmName = nmr.name
        if (dsmName == null) {
            Logger.info(Controller::class.java, "request should contain module name")
            return null
        }
        val dsmHost = r.remoteHost
        return NewModuleResponse(freePort = Manager.registerModule(dsmName, dsmHost))
    }

    @GetMapping(value = ["modules"])
    fun modulesList(): ModulesListResponse {
        Logger.info(Controller::class.java, "request for /modules")
        return ModulesListResponse(Manager.getModules())
    }
}