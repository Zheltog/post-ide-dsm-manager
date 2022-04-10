package su.nsk.iae.post.dsm.manager.infrastructure.responses

import su.nsk.iae.post.dsm.manager.domain.Module

data class ModulesListContent(
    val modules: List<Module>
)