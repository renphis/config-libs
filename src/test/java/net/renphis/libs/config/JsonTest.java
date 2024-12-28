package net.renphis.libs.config;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("ResultOfMethodCallIgnored")
class JsonTest {
    private static final File CONFIG_FILE = new File("config.json");
    private static final File BACKUP_FILE = new File("config.json.bak");

    @BeforeEach
    void setUp() {
        if (CONFIG_FILE.exists()) {
            CONFIG_FILE.delete();
        }
        if (BACKUP_FILE.exists()) {
            BACKUP_FILE.delete();
        }
    }

    @AfterEach
    void tearDown() {
        CONFIG_FILE.delete();
        BACKUP_FILE.delete();
    }

    @Test
    void testSaveAndLoadJson() throws IOException {
        CONFIG_FILE.createNewFile();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");

        Json.save(jsonObject, CONFIG_FILE, false);
        JsonObject loadedJson = Json.load(CONFIG_FILE);

        assertEquals(jsonObject, loadedJson);
    }

    @Test
    void testCreateBackup() throws IOException {
        CONFIG_FILE.createNewFile();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("key", "value");

        Json.save(jsonObject, CONFIG_FILE);
        assertTrue(BACKUP_FILE.exists());
    }
}
