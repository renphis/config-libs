package net.renphis.libs;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

class Json {
    private static final Logger LOGGER = LogManager.getLogger();
    public static final Gson GSON = new GsonBuilder()
            .enableComplexMapKeySerialization()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    public static void save(@NotNull final JsonObject json, @NotNull final File file) {
        save(json, file, true);
    }

    public static void save(@NotNull final JsonObject json, @NotNull final File file, final boolean backup) {
        if (!file.exists()) {
            LOGGER.warn("Could not save JSON object, file does not exist: {}", file.getAbsoluteFile());
            return;
        }

        if (backup) {
            Utils.createBackup(file);
        }

        try(final OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            GSON.toJson(json, writer);
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to save JSON object to file: {}", file.getAbsoluteFile());
        }
    }

    public static @Nullable JsonObject load(@NotNull final File file) {
        if (!file.exists()) {
            LOGGER.warn("Could not load JSON object, file does not exist: {}", file.getAbsoluteFile());
            return null;
        }

        try(final InputStreamReader reader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)) {
            final JsonElement element = JsonParser.parseReader(new JsonReader(reader));

            if (!element.isJsonObject()) {
                LOGGER.warn("Failed to load JSON object from file, invalid JSON object: {}", file.getAbsoluteFile());
                return null;
            }

            return element.getAsJsonObject();
        } catch (JsonParseException | IOException e) {
            LOGGER.error("Failed to load JSON object from file: {}", file.getAbsoluteFile(), e);
            return null;
        }
    }

    public static @Nullable JsonObject loadFromResource(@NotNull String resource) {
        if (!resource.startsWith("/")) {
            resource = "/" + resource;
        }
        
        final InputStream stream = Json.class.getResourceAsStream(resource);
        if (stream == null) {
            LOGGER.warn("Resource not found: {}", resource);
            return null;
        }

        try (final InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            final JsonElement element = JsonParser.parseReader(new JsonReader(reader));

            if (!element.isJsonObject()) {
                LOGGER.warn("Failed to load JSON object from resource, invalid JSON object: {}", resource);
                return null;
            }

            return element.getAsJsonObject();
        } catch (JsonParseException | IOException e) {
            LOGGER.error("Failed to load JSON object from resource: {}", resource, e);
            return null;
        }
    }

    static class Utils {
        public static boolean addMissingKeys(@NotNull final JsonObject source, @NotNull final JsonObject target) {
            boolean dirty = false;

            for (String key : source.keySet()) {
                if (!target.has(key)) {
                    target.add(key, source.get(key));
                    dirty = true;
                } else if (source.get(key).isJsonObject() && target.get(key).isJsonObject()) {
                    dirty |= addMissingKeys(source.getAsJsonObject(key), target.getAsJsonObject(key));
                }
            }

            return dirty;
        }

        public static void createBackup(@NotNull final File file) {
            try {
                File fileBackup = new File(file.getAbsolutePath() + ".bak");
                if (fileBackup.exists()) {
                    final boolean deleted = fileBackup.delete();
                    if (!deleted) {
                        LOGGER.warn("Failed to delete existing backup file: {}", fileBackup.getAbsoluteFile());
                    }
                }

                Files.copy(file.toPath(), fileBackup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                LOGGER.warn("Failed to create a backup file: {}", file.getAbsoluteFile());
            }
        }
    }
}
