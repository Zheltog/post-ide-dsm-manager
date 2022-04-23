package su.nsk.iae.post.dsm.manager.infrastructure

import com.google.gson.Gson
import org.springframework.web.bind.annotation.*
import su.nsk.iae.post.dsm.manager.application.Logger
import su.nsk.iae.post.dsm.manager.application.Manager
import su.nsk.iae.post.dsm.manager.infrastructure.api.DsmManagerApi
import su.nsk.iae.post.dsm.manager.infrastructure.requests.NewModuleRequestBody
import su.nsk.iae.post.dsm.manager.infrastructure.responses.AliveModulesContent
import su.nsk.iae.post.dsm.manager.infrastructure.responses.AvailableModulesContent
import su.nsk.iae.post.dsm.manager.infrastructure.responses.ResponseBody
import su.nsk.iae.post.dsm.manager.infrastructure.responses.ResponseCode.ERROR
import su.nsk.iae.post.dsm.manager.infrastructure.responses.ResponseCode.OK
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

    @GetMapping(value = ["alive-modules"])
    fun aliveModules(): ResponseBody {
        Logger.info(Controller::class.java, "request for /alive-modules")
        return ResponseBody(OK, AliveModulesContent(Manager.getAliveModules(ViaHttp.isModuleAlive)))
    }

    @GetMapping(value = ["available-modules"])
    fun availableModules(): ResponseBody {
        Logger.info(Controller::class.java, "request for /available-modules")
        return ResponseBody(OK, AvailableModulesContent(Manager.getAvailableModules()))
    }

    @PostMapping(value = ["run/{moduleName}"])
    fun runModule(
        @PathVariable moduleName: String,
        @RequestBody requestBody: LinkedHashMap<String, Any>
    ): ResponseBody {
        Logger.info(Controller::class.java, "request for /run/$moduleName")
        return resultToResponse(Manager.runModule(
            moduleName = moduleName,
            request = Gson().toJson(requestBody, Map::class.java),
            isModuleAlive = ViaHttp.isModuleAlive,
            runModule = ViaHttp.runModule
        ))
    }

    @GetMapping(value = ["start-all"])
    fun startModules(): ResponseBody {
        Logger.info(Controller::class.java, "request for /start-all")
        return resultToResponse(Manager.startAvailableModules(null))
    }

    @GetMapping(value = ["start/{moduleName}"])
    fun startModule(
        @PathVariable moduleName: String,
    ): ResponseBody {
        Logger.info(Controller::class.java, "request for /start/$moduleName")
        return resultToResponse(Manager.startModule(moduleName, null))
    }

    @GetMapping(value = ["stop/{moduleName}"])
    fun stopModule(
        @PathVariable moduleName: String,
    ): ResponseBody {
        Logger.info(Controller::class.java, "request for /stop/$moduleName")
        return resultToResponse(Manager.stopModule(moduleName, ViaHttp.stopModule))
    }

    private fun resultToResponse(result: Result<String>) =
        if (result.isFailure) ResponseBody(ERROR, result.exceptionOrNull()?.message)
        else ResponseBody(OK, result.getOrNull())
}