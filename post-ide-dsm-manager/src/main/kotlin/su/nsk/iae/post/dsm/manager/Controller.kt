package su.nsk.iae.post.dsm.manager

import org.springframework.web.bind.annotation.*
import su.nsk.iae.post.dsm.manager.common.Logger
import su.nsk.iae.post.dsm.manager.requests.DsmRequestBody
import su.nsk.iae.post.dsm.manager.requests.NewModuleRequest
import su.nsk.iae.post.dsm.manager.responses.ModulesListResponse
import javax.servlet.http.HttpServletRequest

@RestController
class Controller {
    @RequestMapping(value = [""])
    fun hello(): String {
        Logger.info(Controller::class.java, "request for /")
        return "Hello, this is poST IDE DSM-manager<br>" +
                "My API:<br>" +
                "TODO"
    }

    @PostMapping(value = ["new-module"])
    fun newModule(@RequestBody nmr: NewModuleRequest, r: HttpServletRequest) {
        Logger.info(Controller::class.java, "request for /new-module")
        val dsmName = nmr.name
        if (dsmName == null) {
            Logger.info(Controller::class.java, "request should contain module name")
            return
        }
        val dsmPort = nmr.port
        if (dsmPort == null) {
            Logger.info(Controller::class.java, "request should contain module port")
            return
        }
        Manager.registerModule(dsmName, r.remoteHost, dsmPort)
    }

    @GetMapping(value = ["modules"])
    fun modulesList(): ModulesListResponse {
        Logger.info(Controller::class.java, "request for /modules")
        return ModulesListResponse(Manager.getModules())
    }

    @GetMapping(value = ["run/{moduleName}"])
    fun runModule(
        @PathVariable moduleName: String,
        @RequestBody request: DsmRequestBody
    ): String {
        Logger.info(Controller::class.java, "request for /run/$moduleName")
        return Manager.runModule(moduleName, request)
    }
}