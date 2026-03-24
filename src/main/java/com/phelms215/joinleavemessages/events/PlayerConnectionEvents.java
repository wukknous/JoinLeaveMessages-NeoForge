package com.phelms215.joinleavemessages.events;

import com.phelms215.joinleavemessages.JoinLeaveMessages;
import com.phelms215.joinleavemessages.config.JLMConfig;
import com.phelms215.joinleavemessages.config.PlayerDataStore;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.ChatFormatting;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

import java.util.UUID;

public class PlayerConnectionEvents {

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        PlayerDataStore.init(event.getServer());
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        UUID uuid = player.getUUID();
        String currentName = player.getName().getString();
        String lastName = PlayerDataStore.getLastName(uuid);
        boolean firstJoin = PlayerDataStore.isFirstJoin(uuid);
        MinecraftServer server = player.getServer();
        if (server == null) return;

        boolean debug = JLMConfig.DEBUG.get();

        if (debug) {
            JoinLeaveMessages.LOGGER.info("[JLM Debug] Player logged in: {} | firstJoin={} | lastName={}",
                    currentName, firstJoin, lastName);
        }

        if (firstJoin) {
            handleFirstJoin(player, currentName, server, debug);
            PlayerDataStore.markJoined(uuid);
            PlayerDataStore.setLastName(uuid, currentName);
        } else if (lastName != null && !lastName.equals(currentName)) {
            handleRename(player, currentName, lastName, server, debug);
            PlayerDataStore.setLastName(uuid, currentName);
        } else {
            handleJoin(player, currentName, server, debug);
            if (lastName == null) PlayerDataStore.setLastName(uuid, currentName);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        String name = player.getName().getString();
        MinecraftServer server = player.getServer();
        if (server == null) return;

        boolean debug = JLMConfig.DEBUG.get();
        if (debug) JoinLeaveMessages.LOGGER.info("[JLM Debug] Player logged out: {}", name);

        if (!JLMConfig.LEAVE_MESSAGE_ENABLED.get()) return;

        String text = JLMConfig.CUSTOM_LEAVE_MESSAGE.get()
                ? formatMessage(JLMConfig.CUSTOM_LEAVE_MESSAGE_TEXT.get(), name, null)
                : name + " left the game";

        broadcastMessage(server, text);
    }

    // -------------------------------------------------------------------------

    private void handleFirstJoin(ServerPlayer player, String name, MinecraftServer server, boolean debug) {
        if (debug) JoinLeaveMessages.LOGGER.info("[JLM Debug] First join: {}", name);

        if (JLMConfig.FIRST_JOIN_MESSAGE_ENABLED.get()) {
            String text = JLMConfig.CUSTOM_FIRST_JOIN_MESSAGE.get()
                    ? formatMessage(JLMConfig.CUSTOM_FIRST_JOIN_MESSAGE_TEXT.get(), name, null)
                    : name + " joined the game for the first time!";
            broadcastMessage(server, text);
        }

        if (JLMConfig.PRIVATE_FIRST_JOIN_MESSAGE.get()) {
            sendPrivateMessage(player, formatMessage(JLMConfig.PRIVATE_FIRST_JOIN_MESSAGE_TEXT.get(), name, null));
        }
    }

    private void handleRename(ServerPlayer player, String newName, String oldName, MinecraftServer server, boolean debug) {
        if (debug) JoinLeaveMessages.LOGGER.info("[JLM Debug] Rename: {} -> {}", oldName, newName);

        if (JLMConfig.RENAME_MESSAGE_ENABLED.get()) {
            String text = JLMConfig.CUSTOM_RENAME_MESSAGE.get()
                    ? formatMessage(JLMConfig.CUSTOM_RENAME_MESSAGE_TEXT.get(), newName, oldName)
                    : newName + " joined the game (formerly " + oldName + ")";
            broadcastMessage(server, text);
        }

        if (JLMConfig.PRIVATE_JOIN_MESSAGE.get()) {
            sendPrivateMessage(player, formatMessage(JLMConfig.PRIVATE_JOIN_MESSAGE_TEXT.get(), newName, null));
        }
    }

    private void handleJoin(ServerPlayer player, String name, MinecraftServer server, boolean debug) {
        if (debug) JoinLeaveMessages.LOGGER.info("[JLM Debug] Regular join: {}", name);

        if (JLMConfig.JOIN_MESSAGE_ENABLED.get()) {
            String text = JLMConfig.CUSTOM_JOIN_MESSAGE.get()
                    ? formatMessage(JLMConfig.CUSTOM_JOIN_MESSAGE_TEXT.get(), name, null)
                    : name + " joined the game";
            broadcastMessage(server, text);
        }

        if (JLMConfig.PRIVATE_JOIN_MESSAGE.get()) {
            sendPrivateMessage(player, formatMessage(JLMConfig.PRIVATE_JOIN_MESSAGE_TEXT.get(), name, null));
        }
    }

    // -------------------------------------------------------------------------

    private String formatMessage(String template, String playerName, String oldName) {
        return template
                .replace("\\u00a7", "\u00a7")
                .replace("%p", playerName != null ? playerName : "")
                .replace("%o", oldName != null ? oldName : "");
    }

    /**
     * Converts a legacy §-coded string to a Minecraft Component.
     * Parses each character and applies the corresponding ChatFormatting.
     */
    private Component legacyToComponent(String input) {
        MutableComponent root = Component.literal("");
        StringBuilder current = new StringBuilder();
        Style currentStyle = Style.EMPTY;

        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if ((chars[i] == '\u00a7' || chars[i] == '&') && i + 1 < chars.length) {
                if (current.length() > 0) {
                    root.append(Component.literal(current.toString()).withStyle(currentStyle));
                    current = new StringBuilder();
                }
                char code = Character.toLowerCase(chars[i + 1]);
                ChatFormatting formatting = ChatFormatting.getByCode(code);
                if (formatting != null) {
                    if (formatting == ChatFormatting.RESET) {
                        currentStyle = Style.EMPTY;
                    } else if (formatting.isFormat()) {
                        currentStyle = currentStyle.applyFormat(formatting);
                    } else {
                        currentStyle = currentStyle.withColor(formatting);
                    }
                }
                i++; // skip next char
            } else {
                current.append(chars[i]);
            }
        }

        if (current.length() > 0) {
            root.append(Component.literal(current.toString()).withStyle(currentStyle));
        }

        return root;
    }

    private void broadcastMessage(MinecraftServer server, String message) {
        server.getPlayerList().broadcastSystemMessage(legacyToComponent(message), false);
    }

    private void sendPrivateMessage(ServerPlayer player, String message) {
        player.sendSystemMessage(legacyToComponent(message));
    }
}
