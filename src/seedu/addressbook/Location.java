package seedu.addressbook;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import seedu.addressbook.common.HttpRestClient;

import java.util.ArrayList;

public class Location {
    public static final String GOOGLE_MAPS_API_KEY = "AIzaSyBC7___BJc9QTTTzvZ9BHl2_7kx2FgrP8c";
    public static final String DISTANCE_MATRIX_BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    public static final String GOOGLE_MAPS_BASE_URL = "https://www.google.com/maps/place/";

    private double longitude;
    private double latitude;

    public Location(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    private String getMapsDistanceURL(ArrayList<Location> locations) {
        String originCoordinatesString = locations.get(0).latitude + "," + locations.get(0).longitude;

        for (int i = 1; i < locations.size(); i++) {
            originCoordinatesString += "|" + locations.get(i).latitude + "," + locations.get(i).longitude;
        }

        String GOOGLE_ETA_URL = DISTANCE_MATRIX_BASE_URL + "origins=" + originCoordinatesString
                + "&destinations=" + this.latitude + "," + this.longitude + "&key=" + GOOGLE_MAPS_API_KEY;

        return GOOGLE_ETA_URL;
    }

    private ArrayList<String> getEtaListFromJsonObject(JSONObject jsonData) {
        ArrayList<String> etaList = new ArrayList<>();

        try {
            JSONArray etaRows = jsonData.getJSONArray("rows");

            for (int i = 0; i < etaRows.length(); i++) {
                JSONArray EtaTimeElements = etaRows.getJSONObject(i).getJSONArray("elements");
                String etaDuration = EtaTimeElements.getJSONObject(0).getJSONObject("duration")
                        .getString("text");

                etaList.add(etaDuration);
            }

            return etaList;
        } catch(JSONException jsonE) {
            jsonE.printStackTrace();
        }

        return etaList;
    }

    public ArrayList<String> getEtaFromMultipleLocations(ArrayList<Location> locations) {
        ArrayList<String> etaTimeList = new ArrayList<>();

        try {
            HttpRestClient httpRestClient = new HttpRestClient();
            HttpResponse response = httpRestClient.requestGetResponse(getMapsDistanceURL(locations));

            if (response != null) {
                String jsonString = IOUtils.toString(response.getEntity().getContent());
                JSONObject jsonData = new JSONObject(jsonString);

                etaTimeList = getEtaListFromJsonObject(jsonData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return etaTimeList;
    }

    public String getLocationURL() {
        return GOOGLE_MAPS_BASE_URL + this.getLatitude() + "," + this.getLongitude();
    }

    public static void main(String[] args) {
        Location location = new Location(-6.206968,106.751365);
        Location origin = new Location(-6.189482, 106.733902);
        ArrayList<Location> locationList = new ArrayList<>();
        locationList.add(origin);

        ArrayList<String> ETATiming = location.getEtaFromMultipleLocations(locationList);
        for (String eta: ETATiming) {
            System.out.println(eta);
        }
    }
}
