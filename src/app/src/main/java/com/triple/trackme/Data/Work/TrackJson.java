package com.triple.trackme.Data.Work;

import com.triple.trackme.Data.Storage.Position;
import com.triple.trackme.Data.Storage.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

public class TrackJson {

    public static boolean isTrackFileInitialize(final String trackJsonFileName) {
        return TextFilesUtility.isFileExists(trackJsonFileName);
    }

    public static void deleteTrackFile(final String trackJsonFileName) {
        TextFilesUtility.deleteFile(trackJsonFileName);
    }

    public static void writeTrackToJsonFile(final String trackJsonFileName, final Track track)
            throws WorkWithDataException {

        JSONObject trackJson = new JSONObject();

        try {
            trackJson.put("id", track.id);
            trackJson.put("dateTime", track.dateTime);
            trackJson.put("distance", track.distance);
            trackJson.put("time", track.time);
            trackJson.put("avgSpeed", track.avgSpeed);
            trackJson.put("mapImagePath", track.mapImagePath);

            JSONArray positionsJson = new JSONArray();
            for (Position position : track.positions) {
                JSONObject positionJson = new JSONObject();
                positionJson.put("longitude", position.longitude);
                positionJson.put("latitude", position.latitude);
                positionsJson.put(positionJson);
            }
            trackJson.put("positions", positionsJson);

            String jsonString = trackJson.toString();
            TextFilesUtility.writeTextToFile(trackJsonFileName, jsonString);
        }
        catch (IOException | JSONException exception) {
            exception.printStackTrace();
            throw (WorkWithDataException)(new WorkWithDataException().initCause(exception));
        }
    }

    public static Track readTrackFromJsonFile(final String trackJsonFileName)
            throws WorkWithDataException {

        try {
            String jsonString = TextFilesUtility.readTextFromFile(trackJsonFileName);
            JSONObject trackJson = new JSONObject(jsonString);
            long id = trackJson.getLong("id");
            String dateTime = trackJson.getString("dateTime");
            double distance = trackJson.getDouble("distance");
            int time = trackJson.getInt("time");
            double avgSpeed = trackJson.getDouble("avgSpeed");
            String mapImagePath = trackJson.getString("mapImagePath");
            ArrayList<Position> positions = new ArrayList<Position>();

            JSONArray positionsJson = trackJson.getJSONArray("positions");
            for (int i = 0; i < positionsJson.length(); i++) {
                JSONObject positionJson = positionsJson.getJSONObject(i);
                double longitude = positionJson.getDouble("longitude");
                double latitude = positionJson.getDouble("latitude");
                Position position = new Position(longitude, latitude);
                positions.add(position);
            }

            return new Track(id, dateTime, distance, time, avgSpeed, mapImagePath, positions);
        }
        catch (IOException | JSONException exception) {
            exception.printStackTrace();
            throw (WorkWithDataException)(new WorkWithDataException().initCause(exception));
        }
    }
}
