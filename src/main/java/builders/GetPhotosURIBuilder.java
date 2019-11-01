package builders;

import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class GetPhotosURIBuilder {
    private static final String token = "";

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
