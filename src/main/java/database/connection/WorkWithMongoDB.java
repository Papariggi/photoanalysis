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
	private MongoCollection photoCollection;
	private MongoClient mongoClient;
	private MongoCollection dateCountCollection;
	private boolean isClose = false;

	public WorkWithMongoDB() {
		mongoClient = MongoClients.create("mongodb://localhost:27017");
		database = mongoClient.getDatabase("photoanalysis");
		photoCollection = database.getCollection("photos");
		dateCountCollection = database.getCollection("dates_photos");
	}

	/**
	 * Choosing month for correct output.
	 * @param start Begin date.
	 * @param end End date.
	 * @return Returns End date as long if it has day of month over 15,
	 * otherwise returns begin date.
	 */
	private long chooseMonth(Date start, Date end) {
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(start);
		Calendar calEnd = Calendar.getInstance();
		calEnd.setTime(end);
		if (calStart.get(Calendar.DAY_OF_MONTH) > 15) {
			return end.getTime();
		}
		else
			return start.getTime();
	}
	/**
	 * Method for insert month data of type : ({"date" : "date", "countOfPhoto" : "count"}) in MongoDB.
	 * @param count Count of photo in this period.
	 * @param start_date Date of beginning to add data to database.
	 * @param end_date Date of ending to add data to database.
	 */
	public void insertDataForMonth(int count, Date start_date, Date end_date) {
		Document document = new Document("mouthY",
				new SimpleDateFormat("MMMM_YY").
						format(chooseMonth(start_date, end_date))).
				append("count", count);
		dateCountCollection.insertOne(document);
	}

	/**
	 * Method closes connection with MongoDB.
	 */
	@Override
	public void close() {
		mongoClient.close();
		isClose = true;
	}

	public void photoInsertFromJSON(String json) {
		Document document = Document.parse(json);
		photoCollection.insertOne(document);
	}

	/**
	 * Method clears up collection.
	 * @param collectionName Name of collection to clear up.
	 */
	public void clear(String collectionName) {
		database.getCollection(collectionName).deleteMany(new BasicDBObject());
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

	public void setPhotoCollection(MongoCollection photoCollection) {
		this.photoCollection = photoCollection;
	}

	public MongoDatabase getDatabase() {
		return database;
	}

	public void setDatabase(MongoDatabase database) {
		this.database = database;
	}
}