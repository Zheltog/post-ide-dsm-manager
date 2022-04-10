package su.nsk.iae.post.dsm.manager.infrastructure

import org.springframework.web.bind.annotation.*
import su.nsk.iae.post.dsm.manager.application.Logger
import su.nsk.iae.post.dsm.manager.application.Manager
import su.nsk.iae.post.dsm.manager.infrastructure.api.DsmManagerApi
import su.nsk.iae.post.dsm.manager.infrastructure.requests.DsmRequestBody
import su.nsk.iae.post.dsm.manager.infrastructure.requests.NewModuleRequestBody
import su.nsk.iae.post.dsm.manager.infrastructure.responses.ModulesListContent
import su.nsk.iae.post.dsm.manager.infrastructure.responses.ResponseCode.ERROR
import su.nsk.iae.post.dsm.manager.infrastructure.responses.ResponseCode.OK
import su.nsk.iae.post.dsm.manager.infrastructure.responses.ResponseBody
import javax.servlet.http.HttpServletRequest

@RestController
class Controller {
    @RequestMapping(value = [""])
    fun hello(): String {
        Logger.info(Controller::class.java, "request for /")
        return "Hello, this is poST IDE DSM-manager<br><br>" +
                "My API:<br><br>" +
                DsmManagerApi.asHtml()
    }

    @PostMapping(value = ["new-module"])
    fun newModule(@RequestBody nmr: NewModuleRequestBody, r: HttpServletRequest) {
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
    fun modulesList(): ResponseBody {
        Logger.info(Controller::class.java, "request for /modules")
        return ResponseBody(OK, ModulesListContent(Manager.getModules(ViaHttp.isModuleAlive)))
    }

    @PostMapping(value = ["run/{moduleName}"])
    fun runModule(
        @PathVariable moduleName: String,
        @RequestBody requestBody: DsmRequestBody
    ): ResponseBody {
        Logger.info(Controller::class.java, "request for /run/$moduleName")
        val result =  Manager.runModule(
            moduleName = moduleName,
            request = requestBody.toJsonString(),
            isModuleAlive = ViaHttp.isModuleAlive,
            runModule = ViaHttp.runModule
        )
        return if (result.isFailure) {
            ResponseBody(ERROR, result.exceptionOrNull()?.message)
        } else {
            ResponseBody(OK, result.getOrNull())
        }
    }
}