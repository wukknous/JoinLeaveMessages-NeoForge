package com.phelms215.joinleavemessages.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.phelms215.joinleavemessages.JoinLeaveMessages;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Stores persistent player data (first-join tracking, last known names).
 * Saved as JSON in the world/data directory.
 */
public class PlayerDataStore {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File dataFile;

    // Map<UUID, lastName>
    private static Map<String, String> playerNames = new HashMap<>();
    // Set of UUIDs that have joined before
    private static Map<String, Boolean> hasJoined = new HashMap<>();

    public static void init(MinecraftServer server) {
        File configDir = new File(server.getServerDirectory().toFile(), "config");
        if (!configDir.exists()) configDir.mkdirs();
        dataFile = new File(configDir, "joinleavemessages_playerdata.json");
        load();
    }

    @SuppressWarnings("unchecked")
    private static void load() {
        if (dataFile == null || !dataFile.exists()) return;
        try (FileReader reader = new FileReader(dataFile)) {
            Type type = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = GSON.fromJson(reader, type);
            if (data != null) {
                Object names = data.get("playerNames");
                Object joined = data.get("hasJoined");
                if (names instanceof Map) playerNames = (Map<String, String>) names;
                if (joined instanceof Map) {
                    Map<String, Boolean> j = new HashMap<>();
                    ((Map<String, Object>) joined).forEach((k, v) -> j.put(k, Boolean.TRUE.equals(v)));
                    hasJoined = j;
                }
            }
        } catch (IOException e) {
            JoinLeaveMessages.LOGGER.error("Failed to load player data: {}", e.getMessage());
        }
    }

    public static void save() {
        if (dataFile == null) return;
        try (FileWriter writer = new FileWriter(dataFile)) {
            Map<String, Object> data = new HashMap<>();
            data.put("playerNames", playerNames);
            data.put("hasJoined", hasJoined);
            GSON.toJson(data, writer);
        } catch (IOException e) {
            JoinLeaveMessages.LOGGER.error("Failed to save player data: {}", e.getMessage());
        }
    }

    public static boolean isFirstJoin(UUID uuid) {
        return !hasJoined.getOrDefault(uuid.toString(), false);
    }

    public static void markJoined(UUID uuid) {
        hasJoined.put(uuid.toString(), true);
        save();
    }

    public static String getLastName(UUID uuid) {
        return playerNames.get(uuid.toString());
    }

    public static void setLastName(UUID uuid, String name) {
        playerNames.put(uuid.toString(), name);
        save();
    }
}
