package be.helha;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.*;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.FileReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class TestConnectionMongoDB {
    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        String uri = "mongodb://localhost:27017";
        try {
            MongoClient mongoClient = MongoClients.create(uri);
            MongoDatabase database = mongoClient.getDatabase("TestDB");
            MongoCollection collection = database.getCollection("MaCollection");
            System.out.println("Connexion réussie à la base de données");

            // Chemin du fichier JSON
            String jsonFilePath = "src/main/resources/chest.json";

            // Lecture du chest.json et rajout de clui-ci dans la base de données
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(jsonFilePath)).getAsJsonObject();
            // Convert JSON object to BSON Document
            Document document = Document.parse(jsonObject.toString());
            // Insert document into MongoDB collection
            collection.insertOne(document);
            System.out.println("JSON inseré");

            // Lecture des documents (Read)
            System.out.println("Lecture des documents");
            FindIterable<Document> documents = collection.find();
            for (Document d : documents) {
                System.out.println("Document ID: " + d.getObjectId("_id"));
                System.out.println("Content: " + d.toJson());
                System.out.println("----------------------------");
            }
        }
        catch (Exception e) {
            System.out.println("Une erreur est survenue");
            e.printStackTrace();
        }
    }
}