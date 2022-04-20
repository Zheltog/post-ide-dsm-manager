package su.nsk.iae.post.dsm.manager.domain

data class AvailableModules(
    var directory: String? = null,
    var modulesJarNames: List<String>? = null
)