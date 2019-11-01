package builders;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class for building request uri(get photos from VK.Api)
 */
public class GetPhotosURIBuilder {
    private static final String token = ""; //todo: put access_token

    /**
     * Method building request URI(photos.search)
     * @param lat latitude, should be double or short.
     * @param longParam longitude, should be double or short.
     * @param start_time Date of beginning to search in millis, should be long.
     * @param end_time Date of ending to search in millis, should be long.
     * @return Returns builded URI.
     * @throws URISyntaxException
     */
    public static URI buildGetPhotosUrl(String lat, String longParam, String start_time,
                                        String end_time) throws URISyntaxException
    {
        URIBuilder builder = new URIBuilder("https://api.vk.com/method/photos.search");
        builder.setParameter("lat", lat).
                setParameter("long", longParam).
                setParameter("start_time", start_time).
                setParameter("end_time", end_time).
                setParameter("count", "100").
                setParameter("sort", "0").
                setParameter("radius", "100").
                setParameter("access_token", token).
                setParameter("v", "5.103");
        return builder.build();
    }

}
