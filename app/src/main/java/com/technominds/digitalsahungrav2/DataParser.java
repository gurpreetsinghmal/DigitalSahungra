package com.technominds.digitalsahungrav2;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";
        try {
            if (!googlePlaceJson.isNull("name")) {

                placeName = googlePlaceJson.getString("name");

            }
            if (!googlePlaceJson.isNull("vicinity"))
            {
                vicinity=googlePlaceJson.getString("vicinity");
            }
            latitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference=googlePlaceJson.getString("reference");

            googlePlacesMap.put("place_name",placeName);
            googlePlacesMap.put("vicinity",vicinity);
            googlePlacesMap.put("lat",latitude);
            googlePlacesMap.put("lng",longitude);
            googlePlacesMap.put("reference",reference);



        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googlePlacesMap;

    }

    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson)
    {
        HashMap<String, String> googleDirectionsMap = new HashMap<>();
        String duration="";
        String distances="";
        Log.d("FullArray",googleDirectionsJson.toString());
        try {

            duration=googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");
            distances=googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");
            googleDirectionsMap.put("duration",duration);
            googleDirectionsMap.put("distance",distances);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return googleDirectionsMap;
    }

    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray)
    {
        int count=jsonArray.length();
        List<HashMap<String,String>> placeList=new ArrayList<>();
        HashMap<String,String> placeMap=null;
        for(int i=0;i<count;i++)
        {
            try {
                placeMap=getPlace((JSONObject) jsonArray.get(i));
                placeList.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placeList;

    }

    public List<HashMap<String,String>> parse(String jsonData)
    {
        JSONArray jsonArray=null;
        JSONObject jsonObject;
        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPlaces(jsonArray);
    }
    public HashMap<String,String> parseDirections(String jsonData)
    {  JSONArray jsonArray=null;
        JSONObject jsonObject;
        try {
            jsonObject=new JSONObject(jsonData);
            jsonArray=jsonObject.getJSONArray("routes");//.getJSONObject(0).getJSONArray("legs");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getDuration(jsonArray);
    }


}
