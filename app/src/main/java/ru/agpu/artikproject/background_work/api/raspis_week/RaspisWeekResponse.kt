package ru.agpu.artikproject.background_work.api.raspis_week

data class RaspisWeekResponse(
    val Monday: List<PodgrupsInPar>,
    val Tuesday: List<PodgrupsInPar>,
    val Wednesday: List<PodgrupsInPar>,
    val Thursday: List<PodgrupsInPar>,
    val Friday: List<PodgrupsInPar>,
    val Saturday: List<PodgrupsInPar>,
    val Sunday: List<PodgrupsInPar>,
)