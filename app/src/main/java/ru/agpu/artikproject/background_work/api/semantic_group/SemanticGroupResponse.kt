package ru.agpu.artikproject.background_work.api.semantic_group

data class SemanticGroupResponse(
    val Id: Int,
    val Name: String,
    val Groups: List<SemanticGroup>,
)
