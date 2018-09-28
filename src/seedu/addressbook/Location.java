package seedu.addressbook;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.javatuples.Pair;
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

    /**
     * Returns Google Maps API URL for HTTP GET request.
     *
     * @param locations calculate all locations ETA
     * @return Google Maps URL String
     */
    private String getMapsDistanceURL(ArrayList<Location> locations) {
        String originCoordinatesString = locations.get(0).latitude + "," + locations.get(0).longitude;

        for (int i = 1; i < locations.size(); i++) {
            originCoordinatesString += "|" + locations.get(i).latitude + "," + locations.get(i).longitude;
        }

        String GOOGLE_ETA_URL = DISTANCE_MATRIX_BASE_URL + "origins=" + originCoordinatesString
                + "&destinations=" + this.latitude + "," + this.longitude + "&key=" + GOOGLE_MAPS_API_KEY;

        return GOOGLE_ETA_URL;
    }

    /**
     *
     * @param jsonData
     * @return
     */
    private ArrayList<Pair<Integer, String>> getEtaListFromJsonObject(JSONObject jsonData) {
        ArrayList<Pair<Integer, String>> etaList = new ArrayList<>();

        try {
            JSONArray etaRows = jsonData.getJSONArray("rows");

            for (int i = 0; i < etaRows.length(); i++) {
                JSONArray EtaTimeElements = etaRows.getJSONObject(i).getJSONArray("elements");
                JSONObject etaDuration = EtaTimeElements.getJSONObject(0).getJSONObject("duration");

                Pair<Integer, String> durationPair = new Pair<>(etaDuration.getInt("value"), etaDuration.getString("text"));
                etaList.add(durationPair);
            }

            return etaList;
        } catch(JSONException jsonE) {
            jsonE.printStackTrace();
        }

        return etaList;
    }

    /**
     * Returns ETA for this location from multiple locations
     *
     * @param locations Arraylist is of Locations objects
     * @return ArrayList of Pair of Number of seconds of ETA and text description of ETA
     */

    public ArrayList<Pair<Integer, String>> getEtaFromMultipleLocations(ArrayList<Location> locations) {
        ArrayList<Pair<Integer, String>> etaList = new ArrayList<>();

        try {
            HttpRestClient httpRestClient = new HttpRestClient();
            HttpResponse response = httpRestClient.requestGetResponse(getMapsDistanceURL(locations));

            if (response != null) {
                String jsonString = IOUtils.toString(response.getEntity().getContent());
                JSONObject jsonData = new JSONObject(jsonString);

                etaList = getEtaListFromJsonObject(jsonData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return etaList;
    }

    public String getGooglemMapsURL() {
        return GOOGLE_MAPS_BASE_URL + this.getLatitude() + "," + this.getLongitude();
    }

    public static void main(String[] args) {
        Location location = new Location(-6.206968,106.751365);
        Location origin = new Location(-6.189482, 106.733902);
        ArrayList<Location> locationList = new ArrayList<>();
        locationList.add(origin);

        ArrayList<Pair<Integer, String>> ETATiming = location.getEtaFromMultipleLocations(locationList);
        for (Pair<Integer, String> eta: ETATiming) {
            System.out.println(eta.getValue0() + " " + eta.getValue1());
        }
    }
}
