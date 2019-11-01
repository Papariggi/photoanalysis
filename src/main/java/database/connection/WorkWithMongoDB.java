package database.connection;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import entities.CountAndItems;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class for work and insert data in mongoDB.
 */
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

	/**
	 * Method for insert month data of type : ({"date" : "date", "countOfPhoto" : "count"}) in MongoDB.
	 * @param countAndItems Object, which contains Items and counts from JSON response.
	 * @param start_date Date of beginning to add data to database.
	 * @param end_date Date of ending to add data to database.
	 */
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


	/**
	 * Full clear of table 'dates_photo';
	 */
	public void clear() {
		collection.deleteMany(new BasicDBObject());
	}

	/**
	 * Checking connection for closing.
	 * @return Returns true if connection is closed, otherwise returns false;
	 */
	public boolean isClose() {
		return isClose;
	}

	public void setClose(boolean close) {
		isClose = close;
	}

	public MongoCollection getCollection(String collection) {
		return database.getCollection(collection);
	}

	public void setCollection(MongoCollection collection) {
		this.collection = collection;
	}

	public MongoDatabase getDatabase() {
		return database;
	}

	public void setDatabase(MongoDatabase database) {
		this.database = database;
	}
}