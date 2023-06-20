package ru.agpu.artikproject.background_work.api;

public class ApiData {
    public static final String SCHEDULE_HOST = "www.it-institut.ru";
    public static final String HTTPS = "https://";
    public static final String JSON_GETTER = "JSONGetter";
    public static final String GET_SEMANTIC_GROUP = "GetSemanticGroup";
    public static final String GET_CLIENT_INFO = "GetClientInfo";
    public static final String GET_CALL_RASPIS = "GetCallRaspis";
    public static final String GET_RASPIS_FOR_GROUP = "GetRaspisForGroup";
    public static final String GET_RASPIS_FOR_GROUP_FOR_TWO_WEEKS = "GetRaspisForGroupForTwoWeeks";
    public static final Integer CLIENT_ID = 118;
    public static final String REQUEST_TYPE_GROUP = "Group";
    public static final String REQUEST_TYPE_Teacher = "Teacher";

    public static String BASE_URL = HTTPS + SCHEDULE_HOST + "/"+ JSON_GETTER + "/";
}