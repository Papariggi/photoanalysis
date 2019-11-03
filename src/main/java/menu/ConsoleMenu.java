package menu;

import builders.GetPhotosURIBuilder;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import database.connection.WorkWithMongoDB;
import entities.CountAndItems;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bson.Document;
import org.json.JSONObject;

import javax.print.Doc;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.mongodb.client.model.Projections.*;

/**
 * Class represents user interface as console application.
 */
public class ConsoleMenu {
    private static Scanner sc;
    private static WorkWithMongoDB mongoDB;

    /**
     * Method getting json from response and returns it. Uses GSON and Json-simple.
     *
     * @param photosReq  Request.
     * @param httpClient Opened HttpClient.
     * @return JSON type string.
     * @throws IOException
     */
    private static String getJsonFromResponse(HttpGet photosReq,
                                              CloseableHttpClient httpClient) throws IOException {
        try (CloseableHttpResponse resp = httpClient.execute(photosReq)) {
            HttpEntity entity = resp.getEntity();
            String json = EntityUtils.toString(entity, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONObject itemsAndCountJson = jsonObject.getJSONObject("response");

            return itemsAndCountJson.toString();
        }

    }

    /**
     * Method checks if character 'e' was found, then exit from console app.
     *
     * @param e Inputted string.
     * @return Returns true if 'e' was found and closing console, otherwise returns false.
     */
    private static boolean exit(String e) {
        if (e.equals("e")) {
            try {
                System.out.println("------------- Bye! ------------");
                if (!mongoDB.isClose())
                    mongoDB.close();
                System.exit(1);
                return true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;

    }

    /**
     * Simple output collection in console.
     */
    private static void showDatabase() {
        MongoCollection<Document> collection = mongoDB.getCollection("dates_photos");
        try (MongoCursor<Document> cur = collection.find().iterator()) {

            while (cur.hasNext()) {
                var doc = cur.next();
                var photo = new ArrayList<>(doc.values());
                System.out.printf("%s: %s%n", photo.get(1), photo.get(2));
            }
        }
    }

    /**
     * Represents user interface and dialog in console application.
     *
     * @param scanner Scanner.
     * @throws InterruptedException
     */
    public static void menu(Scanner scanner) throws InterruptedException {
        sc = scanner;
        String lat = "55.804571";
        String longParam ="37.766999";
        Date start_date;
        Date end_date;

        System.out.println("---------------- Welcome to photo analysis!---------------------");
        System.out.println("                                        Made by Vladislav Papariga");
        System.out.println("                                                       Group: 4111");
        System.out.println("------------------Type 'e' for exit!");

        while (true) {
            mongoDB = new WorkWithMongoDB();
            Thread.sleep(1500);
            try {
                System.out.println("\nEnter latitude of place:");
                if (exit(lat = sc.nextLine()))
                    break;

                Double.parseDouble(lat);
                System.out.println("\nEnter longitude of place:");
                if (exit(longParam = sc.nextLine()))
                    break;

                Double.parseDouble(longParam);

                System.out.println("\nEnter start of period to analize(format - dd/MM/yyyy, example 09/01/2019):");
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                start_date = df.parse(sc.nextLine());


                System.out.println("\nEnter end of period to analize(format - dd/MM/yyyy, example 09/01/2019):");
                end_date = df.parse(sc.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Incorrect values, please try again!");
                continue;
            } catch (NoSuchElementException e) {
                System.out.println("Incorrect values, please try again!");
                continue;
            } catch (ParseException e) {
                System.out.println("Incorrect values, please try again!");
                continue;
            }

        Calendar totalEnd = Calendar.getInstance();
        totalEnd.setTime(end_date);

        Calendar start = Calendar.getInstance();
        start.setTime(start_date);

        Calendar end = Calendar.getInstance();
        end.setTime(start_date);
        end.add(Calendar.MONTH, 1);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet photosReq;
            CountAndItems countItems;
            JSONObject itemsAndCountJson;

            mongoDB.clear("photos");
            mongoDB.clear("dates_photos");

            while (start.compareTo(totalEnd) < 0) {
                if (end.compareTo(totalEnd) > 0)
                        end = (Calendar)totalEnd.clone();
                    Thread.sleep(1500);
                    //Send http GET request
                    photosReq = new HttpGet(GetPhotosURIBuilder.buildGetPhotosUrl(lat, longParam,
                            String.valueOf(start.getTime().getTime() / 1000L),
                            String.valueOf(end.getTime().getTime() / 1000L)));

                    //getting response
                    try (CloseableHttpResponse resp = httpClient.execute(photosReq)) {
                        HttpEntity entity = resp.getEntity();
                        String json = EntityUtils.toString(entity, "UTF-8");

                        JSONObject jsonObject = new JSONObject(json);
                        itemsAndCountJson = jsonObject.getJSONObject("response");

                        //parsing into mongoDB
                        mongoDB.photoInsertFromJSON(itemsAndCountJson.toString());
                    }
                    //Parsing into objects
                    countItems = new Gson().fromJson(itemsAndCountJson.toString(), CountAndItems.class);
                    mongoDB.insertDataForMonth(countItems.getCount(), start.getTime(), end.getTime());

                    start = (Calendar) end.clone();
                    end.add(Calendar.MONTH, 1);
            }

            showDatabase();
            } catch (URISyntaxException e) {
                System.out.println("Wrong url for request!");
                break;
            } catch (IOException e) {
                System.out.println("Something wrong with stream, sorry");
                break;
            }
            finally {
            mongoDB.close();
            }
        }

    }
}
