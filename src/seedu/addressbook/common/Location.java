//@@author andyrobert3
package seedu.addressbook.common;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.javatuples.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Location {
    public static final String DISTANCE_MATRIX_BASE_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    public static final String GOOGLE_MAPS_BASE_URL = "https://www.google.com/maps/place/";

    private String GOOGLE_MAPS_API_KEY;
    private double longitude;
    private double latitude;

    public Location(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        GOOGLE_MAPS_API_KEY = getGoogleMapsApiKey();
    }

    public double getLongitude() {
        return longitude;
    }
    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude;
    }
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public String getGoogleMapsApiKey()  {

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("env"));
            return bufferedReader.readLine();
        } catch(IOException ioe) {
            ioe.getMessage();
        }

        return null;
    }

    private ArrayList<Pair<Integer, String>> sortEta(ArrayList<Pair<Integer, String>> etaList) {
        Collections.sort(etaList, Comparator.comparing(Pair::getValue0));
        return etaList;
    }

    /**
     * Returns Google Maps API URL for HTTP GET request from multiple origins.
     *
     * @param origins multiple origins ETA to this location
     * @return Google Maps URL String
     */
    private String getMapsDistanceUrl(ArrayList<Location> origins) {
        String originCoordinatesString = origins.get(0).latitude + "," + origins.get(0).longitude;

        for (int i = 1; i < origins.size(); i++) {
            originCoordinatesString += "|" + origins.get(i).latitude + "," + origins.get(i).longitude;
        }

        String googleEtaUrl = DISTANCE_MATRIX_BASE_URL + "origins=" + originCoordinatesString
                + "&destinations=" + this.latitude + "," + this.longitude + "&key=" + GOOGLE_MAPS_API_KEY;

        return googleEtaUrl;
    }

    /**
     * Returns List of Pairs of Estimated Time of Arrival (ETA) from JSON ETA data
     *
     * @param jsonData from Google Maps GET Request
     * @return ArrayList of Pair<ETA in seconds, ETA in natural text>
     */
    private ArrayList<Pair<Integer, String>> getEtaFromJsonObject(JSONObject jsonData) {
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
            // TODO: Handle exception better
            jsonE.printStackTrace();
        }

        return etaList;
    }

    /**
     * Returns ETA for this location from multiple locations
     *
     * @param locations list of origins to this destination
     * @return ArrayList of Pair<Number of seconds ETA, Text description of ETA>
     */

    public ArrayList<Pair<Integer, String>> getEtaFrom(ArrayList<Location> locations) throws IOException, JSONException{
        ArrayList<Pair<Integer, String>> etaList = new ArrayList<>();

//        try {
        HttpRestClient httpRestClient = new HttpRestClient();
        HttpResponse response = httpRestClient.requestGetResponse(getMapsDistanceUrl(locations));

        if (response != null) {
            String jsonString = IOUtils.toString(response.getEntity().getContent(), "UTF-8");
            etaList = getEtaFromJsonObject(new JSONObject(jsonString));
        } // TODO: Change this exception
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return sortEta(etaList);
    }

    public String getGoogleMapsURL() {
        return GOOGLE_MAPS_BASE_URL + this.getLatitude() + "," + this.getLongitude();
    }

}
