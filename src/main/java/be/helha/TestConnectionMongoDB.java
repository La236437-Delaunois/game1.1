package be.helha;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.client.*;
import org.bson.Document;

import java.io.FileReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class TestConnectionMongoDB {
    // ANSI color codes for console output
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        String uri = "mongodb://localhost:27017";

        try {
            MongoClient mongoClient = MongoClients.create(uri);
            MongoDatabase database = mongoClient.getDatabase("TestDB");
            MongoCollection<Document> collection = database.getCollection("MaCollection");
            System.out.println(GREEN + "✅ Connexion réussie à la base de données\n" + RESET);

            // Chemin du fichier JSON
            String jsonFilePath = "src/main/resources/chest.json";

            // Lecture du chest.json et insertion dans la base de données
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(jsonFilePath)).getAsJsonObject();
            Document document = Document.parse(jsonObject.toString());
            collection.insertOne(document);
            System.out.println(CYAN + "📤 JSON inséré avec succès\n" + RESET);

            // Lecture des documents (Read)
            System.out.println(YELLOW + "📚 Lecture des documents :\n" + RESET);
            FindIterable<Document> documents = collection.find();

            int docCount = 1;
            for (Document d : documents) {
                System.out.println(BLUE + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
                System.out.printf(PURPLE + "📄 Document %d :\n" + RESET, docCount++);
                System.out.println(BLUE + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" + RESET);
                System.out.println(GREEN + "🆔 ID : " + YELLOW + d.getObjectId("_id") + RESET);

                Set<String> keys = d.keySet();
                for (String key : keys) {
                    if (!key.equals("_id")) { // Ne pas afficher l'ID deux fois
                        Object value = d.get(key);
                        System.out.print(CYAN + "  " + key + " : " + RESET);
                        printFormattedValue(value, "  ");
                    }
                }
                System.out.println(BLUE + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" + RESET);
            }
        } catch (Exception e) {
            System.out.println(RED + "❌ Une erreur est survenue" + RESET);
            e.printStackTrace();
        }
    }

    // Méthode pour afficher les objets de manière lisible
    private static void printFormattedValue(Object value, String indent) {
        if (value instanceof Document) {
            System.out.println(YELLOW + "{" + RESET);
            Document doc = (Document) value;
            for (String key : doc.keySet()) {
                System.out.print(indent + "  " + CYAN + key + " : " + RESET);
                printFormattedValue(doc.get(key), indent + "  ");
            }
            System.out.println(indent + YELLOW + "}" + RESET);
        } else if (value instanceof List) {
            System.out.println(YELLOW + "[" + RESET);
            List<?> list = (List<?>) value;
            for (Object item : list) {
                System.out.print(indent + "  - ");
                printFormattedValue(item, indent + "  ");
            }
            System.out.println(indent + YELLOW + "]" + RESET);
        } else {
            System.out.println(YELLOW + value + RESET);
        }
    }
}
