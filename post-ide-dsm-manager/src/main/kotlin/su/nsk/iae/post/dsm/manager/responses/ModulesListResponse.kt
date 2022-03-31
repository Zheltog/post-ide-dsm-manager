package su.nsk.iae.post.dsm.manager.responses

import su.nsk.iae.post.dsm.manager.Module

data class ModulesListResponse(
    val modules: List<Module>
)