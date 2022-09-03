package ru.agpu.artikproject.background_work.api.SemanticGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;

import ru.agpu.artikproject.background_work.api.ApiData;


public class GetSemanticGroupFromApi {

    public ArrayList<SemanticGroupItem> get() {
        try {
            String url = "https://" + ApiData.SCHEDULE_HOST + "/" + ApiData.JSON_GETTER + "/"
                    + ApiData.GET_SEMANTIC_GROUP + "?ClientId=" + ApiData.CLIENT_ID;
            String jsonString = Jsoup.connect(url).ignoreContentType(true).execute().body();
            JSONArray SemanticGroupItems = new JSONArray(jsonString);

            ArrayList<SemanticGroupItem> semanticGroupItemList = new ArrayList<>();
            for (int i = 0; i < SemanticGroupItems.length(); i++) {
                for (int j = 0; j < SemanticGroupItems.getJSONObject(i).getJSONArray("Groups").length(); j++) {
                    SemanticGroupItem SemanticGroupItem = new SemanticGroupItem(
                            SemanticGroupItems.getJSONObject(i).getString("Id"),
                            SemanticGroupItems.getJSONObject(i).getString("Name"),
                            SemanticGroupItems.getJSONObject(i).getJSONArray("Groups").getJSONObject(j).getString("Id"),
                            SemanticGroupItems.getJSONObject(i).getJSONArray("Groups").getJSONObject(j).getString("IsArchive"),
                            SemanticGroupItems.getJSONObject(i).getJSONArray("Groups").getJSONObject(j).getString("NumberOfStudents"),
                            SemanticGroupItems.getJSONObject(i).getJSONArray("Groups").getJSONObject(j).getString("Name"),
                            SemanticGroupItems.getJSONObject(i).getJSONArray("Groups").getJSONObject(j).getString("IsRaspis")
                    );
                    semanticGroupItemList.add(SemanticGroupItem);
                }
            }
            return semanticGroupItemList;
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
