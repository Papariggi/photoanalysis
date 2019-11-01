package menu;

import builders.GetPhotosURIBuilder;
import com.google.gson.Gson;
import database.connection.WorkWithMongoDB;
import entities.CountAndItems;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConsoleMenu {
    private static Scanner sc;
    private static WorkWithMongoDB mongoDB;

    private static CountAndItems parsingToJsonFromResponse(HttpGet photosReq,
                                                           CloseableHttpClient httpClient) throws IOException {
        try (CloseableHttpResponse resp = httpClient.execute(photosReq)) {
            HttpEntity entity = resp.getEntity();
            String json = EntityUtils.toString(entity, "UTF-8");

            JSONObject jsonObject = new JSONObject(json);
            JSONObject itemsAndCountJson = jsonObject.getJSONObject("response");

            Gson gson = new Gson();
            CountAndItems countItems = gson.fromJson(itemsAndCountJson.toString(),
                    CountAndItems.class);
            return countItems;
        }

    }

    private static boolean exit(String e) {
        if (e.equals("e")) {
            try {
                System.out.println("------------- Bye! ------------");
                if (!mongoDB.isClose())
                    mongoDB.close();
                System.exit(1);
                return true;
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return false;

    }

    private static void showDatabase() {

    }


    public static void menu(Scanner scanner) throws InterruptedException {
        mongoDB = new WorkWithMongoDB();
        Thread.sleep(1000);
        sc = scanner;
        String lat; //"55.804571"
        String longParam; //"37.766999"
        String start_time; //"1554843600"
        String end_time; //"1557435600"
        Date start_date;
        Date end_date;

        System.out.println("---------------- Welcome to photo analysis!---------------------");
        System.out.println("                                        Made by Vladislav Papariga");
        System.out.println("                                                       Group: 4111");
        System.out.println("------------------Type 'e' for exit!");

        while (true) {
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
                start_time = String.valueOf(start_date.getTime() / 1000L);


                System.out.println("\nEnter end of period to analize(format - dd/MM/yyyy, example 09/01/2019):");
                end_date = df.parse(sc.nextLine());
                end_time = String.valueOf(end_date.getTime() / 1000L);

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

                mongoDB.clear();

                while (start.compareTo(totalEnd) < 0 &
                        start.get(Calendar.MONTH) != totalEnd.get(Calendar.MONTH)) {
                    photosReq = new HttpGet(GetPhotosURIBuilder.buildGetPhotosUrl(lat, longParam,
                            String.valueOf(start.getTime().getTime() / 1000L),
                            String.valueOf(end.getTime().getTime() / 1000L)));
                    countItems = parsingToJsonFromResponse(photosReq, httpClient);
                    mongoDB.insertDataForMonth(countItems, start.getTime(), end.getTime());
                    start = (Calendar)end.clone(); //todo: просмотреть правильно ли
                    end.add(Calendar.MONTH, 1);
                }
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