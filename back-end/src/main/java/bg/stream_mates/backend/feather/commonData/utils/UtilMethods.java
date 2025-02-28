package bg.stream_mates.backend.feather.commonData.utils;

import bg.stream_mates.backend.feather.commonData.entities.Actor;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class UtilMethods {

    public static List<Actor> extractActors(String actorsURL, HttpClient httpClient, String TMDB_BASE_URL, String TMDB_API_KEY) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(actorsURL))
                .build();

        List<Actor> allCast = new ArrayList<>();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                JsonArray resultsArray = jsonObject.getAsJsonArray("cast");

                for (JsonElement jsonElement : resultsArray) {
                    JsonObject jsonObj = jsonElement.getAsJsonObject();

                    String actorId = getJsonValue(jsonObj, "id");
                    if (actorId.isEmpty()) continue;

                    Actor responseActor = extractDeepActorInfo(actorId, TMDB_BASE_URL, TMDB_API_KEY, httpClient);
                    if (responseActor != null) allCast.add(responseActor);
                }

            } else {
                System.out.println("Something is wrong with extractActors()");
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        return allCast;
    }

    public static Actor extractDeepActorInfo(String actorId, String TMDB_BASE_URL, String TMDB_API_KEY, HttpClient httpClient) {
        // https://api.themoviedb.org/3/person/{person_id}?api_key=YOUR_API_KEY&append_to_response=external_ids
        String searchQuery = TMDB_BASE_URL + "/3/person/" + actorId + "?api_key=" + TMDB_API_KEY + "&append_to_response=external_ids";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(searchQuery)).build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);

                String name = getJsonValue(jsonObject, "name");
                String actorImage = getJsonValue(jsonObject, "profile_path");
                String biography = getJsonValue(jsonObject, "biography");
                String birthday = getJsonValue(jsonObject, "birthday");
                String knownFor = getJsonValue(jsonObject, "known_for_department");
                String popularity = getJsonValue(jsonObject, "popularity");
                String placeOfBirth = getJsonValue(jsonObject, "place_of_birth");
                String gender = getJsonValue(jsonObject, "gender");

                JsonObject externalIds = jsonObject.has("external_ids") ? jsonObject.getAsJsonObject("external_ids") : null;
                String facebookUsername = getExternalId(externalIds, "facebook_id");
                String instagramUsername = getExternalId(externalIds, "instagram_id");
                String twitterUsername = getExternalId(externalIds, "twitter_id");
                String youtubeChannel = getExternalId(externalIds, "youtube_id");
                String imdbId = getExternalId(externalIds, "imdb_id");

                return new Actor().setNameInRealLife(name).setImageURL(actorImage).setBiography(biography).setFacebookUsername(facebookUsername)
                        .setInstagramUsername(instagramUsername).setTwitterUsername(twitterUsername).setYoutubeChannel(youtubeChannel)
                        .setImdbId(imdbId).setBirthday(birthday).setKnownFor(knownFor).setPopularity(popularity).setPlaceOfBirth(placeOfBirth)
                        .setGender(gender);

            } else {
                System.out.println("Wrong with DeepActorInfo!");
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        return null;
    }

    public static String getJsonValue(JsonObject jsonObj, String key) {
        return (jsonObj != null && jsonObj.has(key) && !jsonObj.get(key).isJsonNull())
                ? jsonObj.get(key).getAsString()
                : "";
    }

    private static String getExternalId(JsonObject externalIds, String key) {
        return (externalIds != null && externalIds.has(key) && !externalIds.get(key).isJsonNull())
                ? externalIds.get(key).getAsString()
                : "";
    }
}