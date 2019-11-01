package main;

import builders.GetPhotosURIBuilder;
import com.google.gson.Gson;
import com.mongodb.client.MongoClients;
import database.connection.WorkWithMongoDB;
import entities.CountAndItems;
import entities.Item;
import menu.ConsoleMenu;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        try(Scanner sc = new Scanner(System.in)) {
            ConsoleMenu.menu(sc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

//    setParameter("lat", "55.804744").
////    setParameter("long", "37.767228").
////    setParameter("start_time", "1554843600").
////    setParameter("end_time", "1557435600").
}
