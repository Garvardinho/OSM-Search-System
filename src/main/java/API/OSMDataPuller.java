package API;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class OSMDataPuller {
    String stringInQuery;
    HttpResponse<String> response;
    Object objParsed;
    JSONArray jsonParsed;
    ArrayList<String> resultSet;
    HashMap<String, Coordinate> coordinates;

    public ArrayList<String> getData(String query)
    {
        try {
            stringInQuery = URLEncoder.encode(query, "UTF-8");
            response = Unirest.get("http://nominatim.openstreetmap.org/search?q=" + stringInQuery + "&format=json").asString();
            objParsed = new JSONParser().parse(response.getBody());
            jsonParsed = (JSONArray) objParsed;
            resultSet = new ArrayList<>();
            coordinates = new HashMap<>();

            for (Object o : jsonParsed) {
                JSONObject element = (JSONObject) o;
                resultSet.add(element.get("display_name").toString());
                coordinates.put(element.get("display_name").toString(),
                        (new Coordinate(Double.parseDouble(element.get("lat").toString()),
                        Double.parseDouble(element.get("lon").toString()))));
            }

        } catch (UnirestException | ParseException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public Coordinate getCoordinates(String point) {
        return coordinates.get(point);
    }

}
