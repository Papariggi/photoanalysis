package database.connection;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entities.CountAndItems;
import entities.Item;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

public class WorkWithMongoDB implements AutoCloseable{
	private MongoDatabase database;
	private MongoCollection collection;
	private MongoClient mongoClient;
	private boolean isClose = false;

	public WorkWithMongoDB() {
		mongoClient = MongoClients.create("mongodb://localhost:27017");
		database = mongoClient.getDatabase("photoanalysis");
		collection = database.getCollection("dates_photos");
	}

	public void insertDataForMonth(CountAndItems countAndItems, Date start_date, Date end_date) {
		Document document = new Document("mouthY",
				new SimpleDateFormat("MMM_YY").format(end_date.getTime())).
				append("count", countAndItems.getCount());
		collection.insertOne(document);
	}

	@Override
	public void close() {
		mongoClient.close();
		isClose = true;
	}

	public void clear() {
		collection.deleteMany(new BasicDBObject());
	}

	public boolean isClose() {
		return isClose;
	}

	public void setClose(boolean close) {
		isClose = close;
	}
}