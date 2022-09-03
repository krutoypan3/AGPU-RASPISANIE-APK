package ru.agpu.artikproject.background_work.api.GetRaspisForGroup;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;

import ru.agpu.artikproject.background_work.api.ApiData;

public class GetRaspisForGroupFromApi {
    public ArrayList<ArrayList<ArrayList<ArrayList<ParaOnePodgroupItem>>>> get(String groupId) {
        try {
            String url = "https://" + ApiData.SCHEDULE_HOST + "/" + ApiData.JSON_GETTER + "/"
                    + ApiData.GET_RASPIS_FOR_GROUP + "?OwnerId=" + ApiData.CLIENT_ID + "&GroupId=" + groupId + "&RequestType=Group";
            String jsonString = Jsoup.connect(url).ignoreContentType(true).execute().body();
            JSONObject paraWeekItemsJson = new JSONObject(jsonString);
            JSONArray week = paraWeekItemsJson.names();
            ArrayList<ArrayList<ArrayList<ArrayList<ParaOnePodgroupItem>>>> paraWeekItems = new ArrayList<>();
            for (int i = 0; i < week.length(); i++) {
                JSONArray paraDayItemsJson = week.getJSONArray(i);
                ArrayList<ArrayList<ArrayList<ParaOnePodgroupItem>>> paraDayItems = new ArrayList<>();
                for (int j = 0; j < paraDayItemsJson.length(); j++) {
                    JSONArray ParaManyPodgroupItemsJson = paraDayItemsJson.getJSONArray(j);
                    ArrayList<ArrayList<ParaOnePodgroupItem>> paraManyPodgroupItems = new ArrayList<>();
                    for (int k = 0; k < ParaManyPodgroupItemsJson.length(); k++) {
                        JSONArray ParaOnePodgroupItemsJson = ParaManyPodgroupItemsJson.getJSONArray(k);
                        ArrayList<ParaOnePodgroupItem> paraOnePodgroupItems = new ArrayList<>();
                        for (int h = 0; h < ParaOnePodgroupItemsJson.length(); h++) {
                            paraOnePodgroupItems.add(new ParaOnePodgroupItem(
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("Id"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("Auditoriya"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("Gruppa"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("WeekType"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("Podgruppa"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("DisciplineId"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("DisciplineName"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("PrepodFIO"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("Comment"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("ParaTypeColor"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("DayNumber"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("TipZanyatiya"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("ParaNummber"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("IsCollisionClassroom"),
                                    ParaOnePodgroupItemsJson.getJSONObject(h).getString("IsCollisionTeacher")
                            ));
                        }
                        paraManyPodgroupItems.add(paraOnePodgroupItems);
                    }
                    paraDayItems.add(paraManyPodgroupItems);
                }
                paraWeekItems.add(paraDayItems);
            }
            return paraWeekItems;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
