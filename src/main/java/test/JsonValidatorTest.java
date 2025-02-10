package test;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

import org.junit.jupiter.api.TestMethodOrder;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonValidatorTest {

    private static final String JSON_FILE_PATH = "config.json"; // Chemin du fichier JSON

    @Test
    @Order(1)
    void testJsonFileIsValid() {
        try (FileReader reader = new FileReader(JSON_FILE_PATH)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            assertNotNull(jsonElement, "Le fichier JSON ne doit pas être null.");
            System.out.println("✅ Le fichier JSON est valide !");
        } catch (JsonSyntaxException e) {
            fail("❌ Erreur : Le JSON est mal formé ! " + e.getMessage());
        } catch (FileNotFoundException e) {
            fail("❌ Erreur : Fichier JSON introuvable !");
        } catch (Exception e) {
            fail("❌ Une erreur inattendue est survenue : " + e.getMessage());
        }
    }
}
