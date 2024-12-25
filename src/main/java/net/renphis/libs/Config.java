package net.renphis.libs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Config {
    private static File configFile;
    private static JsonObject config;

    public static void init(@NotNull final File file) {
        init(file, null);
    }

    public static void init(@NotNull final File file, @Nullable final String fromResource) {
        if (!file.getName().endsWith(".json")) {
            throw new IllegalArgumentException("Config file must be a JSON file");
        }

        synchronized (Config.class) {
            if (!file.exists()) {
                try {
                    final File parent = file.getParentFile();
                    if (parent != null && !parent.exists() && !parent.mkdirs()) {
                        throw new IOException("Failed to create parent directories for file \"" + file + "\"");
                    }
                    if (!file.createNewFile()) {
                        throw new IOException("Failed to create config file");
                    }
                    Json.save(new JsonObject(), file, false);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to initialize config file", e);
                }
            }

            config = Json.load(file);
            if (config == null) {
                throw new RuntimeException("Failed to load config file");
            }
            configFile = file;

            if (fromResource != null) {
                verifyFrom(fromResource);
            }
        }
    }

    public static void verifyFrom(@NotNull final String resourceLocation) {
        requireJsonObject(config);
        final JsonObject defaultConfig = Json.loadFromResource(resourceLocation);
        requireJsonObject(defaultConfig);

        final boolean dirty = Json.Utils.addMissingKeys(defaultConfig, config);
        if (dirty) {
            Json.save(config, configFile);
        }
    }

    public static void save() {
        requireJsonObject(config);
        Json.save(config, configFile);
    }

    public static void reload() {
        requireJsonObject(config);
        config = Json.load(configFile);
    }

    public static @NotNull JsonObject get() {
        requireJsonObject(config);
        return config;
    }

    public static @Nullable JsonObject get(@NotNull final String key) {
        requireJsonObject(config);

        String[] keys = key.split("\\.");
        JsonElement currentElement = config;

        for (String k : keys) {
            if (currentElement == null) {
                return null;
            }
            else if (currentElement.isJsonObject()) {
                currentElement = currentElement.getAsJsonObject().get(k);
            } else {
                return null;
            }
        }

        return currentElement.getAsJsonObject();
    }

    public static @Nullable String getString(@NotNull final String key) {
        final JsonObject object = get(key);
        return object == null ? null : object.getAsString();
    }

    public static @Nullable Boolean getBoolean(@NotNull final String key) {
        final JsonObject object = get(key);
        return object == null ? null : object.getAsBoolean();
    }

    public static @Nullable Number getNumber(@NotNull final String key) {
        final JsonObject object = get(key);
        return object == null ? null : object.getAsNumber();
    }

    public static void set(@NotNull final String key, @NotNull final JsonElement value) {
        set(key, value, true);
    }

    public static void set(@NotNull final String key, @NotNull final JsonElement value, final boolean save) {
        requireJsonObject(config);

        String[] keys = key.split("\\.");
        JsonObject currentObject = config;

        for (int i = 0; i < keys.length - 1; i++) {
            final String k = keys[i];
            if (!currentObject.has(k)) {
                currentObject.add(k, new JsonObject());
            }
            currentObject = currentObject.getAsJsonObject(k);
        }

        currentObject.add(keys[keys.length - 1], value);
        if (save) {
            save();
        }
    }

    public static void setString(@NotNull final String key, @NotNull final String value) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value));
    }

    public static void setString(@NotNull final String key, @NotNull final String value, final boolean save) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value), save);
    }

    public static void setChar(@NotNull final String key, final char value) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value));
    }

    public static void setChar(@NotNull final String key, final char value, final boolean save) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value), save);
    }

    public static void setBoolean(@NotNull final String key, final boolean value) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value));
    }

    public static void setBoolean(@NotNull final String key, final boolean value, final boolean save) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value), save);
    }

    public static void setInt(@NotNull final String key, final int value) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value));
    }

    public static void setInt(@NotNull final String key, final int value, final boolean save) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value), save);
    }

    public static void setDouble(@NotNull final String key, final double value) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value));
    }

    public static void setDouble(@NotNull final String key, final double value, final boolean save) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value), save);
    }

    public static void setFloat(@NotNull final String key, final float value) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value));
    }

    public static void setFloat(@NotNull final String key, final float value, final boolean save) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value), save);
    }

    public static void setLong(@NotNull final String key, final long value) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value));
    }

    public static void setLong(@NotNull final String key, final long value, final boolean save) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value), save);
    }

    public static void setShort(@NotNull final String key, final short value) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value));
    }

    public static void setShort(@NotNull final String key, final short value, final boolean save) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value), save);
    }

    public static void setByte(@NotNull final String key, final byte value) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value), true);
    }

    public static void setByte(@NotNull final String key, final byte value, final boolean save) {
        requireJsonObject(config);
        set(key, new JsonPrimitive(value));
    }

    public static void delete(@NotNull final String key) {
        delete(key, true);
    }

    public static void delete(@NotNull final String key, final boolean save) {
        requireJsonObject(config);

        String[] keys = key.split("\\.");
        JsonObject currentObject = config;

        for (int i = 0; i < keys.length - 1; i++) {
            final String k = keys[i];
            if (!currentObject.has(k)) {
                return;
            }
            currentObject = currentObject.getAsJsonObject(k);
        }

        currentObject.remove(keys[keys.length - 1]);
        if (save) {
            save();
        }
    }

    public static boolean has(@NotNull final String key) {
        requireJsonObject(config);

        String[] keys = key.split("\\.");
        JsonElement currentElement = config;

        for (String k : keys) {
            if (currentElement == null) {
                return false;
            }
            else if (currentElement.isJsonObject()) {
                currentElement = currentElement.getAsJsonObject().get(k);
            } else {
                return false;
            }
        }

        return currentElement != null;
    }

    public static boolean isInitialized() {
        return config != null;
    }

    private static @NotNull JsonObject requireJsonObject(final JsonObject object) {
        Objects.requireNonNull(object);
        return object;
    }
}
