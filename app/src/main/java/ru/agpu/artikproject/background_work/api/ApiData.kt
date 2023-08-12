package ru.agpu.artikproject.background_work.api

object ApiData {
    const val SCHEDULE_HOST = "www.it-institut.ru"
    const val HTTPS = "https://"
    const val JSON_GETTER = "JSONGetter"
    const val GET_SEMANTIC_GROUP = "GetSemanticGroup"
    const val GET_CLIENT_INFO = "GetClientInfo"
    const val GET_CALL_RASPIS = "GetCallRaspis"
    const val GET_RASPIS_FOR_GROUP = "GetRaspisForGroup"
    const val GET_RASPIS_FOR_GROUP_FOR_TWO_WEEKS = "GetRaspisForGroupForTwoWeeks"
    const val CLIENT_ID = 118
    const val REQUEST_TYPE_GROUP = "Group"
    const val REQUEST_TYPE_Teacher = "Teacher"

    var BASE_URL = "$HTTPS$SCHEDULE_HOST/$JSON_GETTER/"
}