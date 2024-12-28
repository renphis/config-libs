package net.renphis.libs.config;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
class ConfigTest {
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
    void testInitWithoutResource() {
        Config.init(CONFIG_FILE);
        assertTrue(CONFIG_FILE.exists());
        assertNotNull(Json.load(CONFIG_FILE));
    }

    @Test
    void testInitWithResource() {
        Config.init(CONFIG_FILE, "/defaultConfig.json");
        assertTrue(CONFIG_FILE.exists());

        final JsonObject config = Json.load(CONFIG_FILE);
        final JsonObject defaultConfig = Json.loadFromResource("/defaultConfig.json");
        assertEquals(defaultConfig, config);
    }

    @Test
    void testAddingMissingKeys() {
        Config.init(CONFIG_FILE);
        assertTrue(CONFIG_FILE.exists());

        Config.verifyFrom("/defaultConfig.json");
        final JsonObject config = Json.load(CONFIG_FILE);
        final JsonObject defaultConfig = Json.loadFromResource("/defaultConfig.json");
        assertEquals(defaultConfig, config);
    }
}
