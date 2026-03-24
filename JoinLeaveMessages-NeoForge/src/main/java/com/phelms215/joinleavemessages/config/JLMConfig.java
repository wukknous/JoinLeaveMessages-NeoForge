package com.phelms215.joinleavemessages.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class JLMConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // -------------------------------------------------------------------------
    // General
    // -------------------------------------------------------------------------
    public static final ModConfigSpec.BooleanValue DEBUG;

    // -------------------------------------------------------------------------
    // First join (broadcast)
    // -------------------------------------------------------------------------
    public static final ModConfigSpec.BooleanValue FIRST_JOIN_MESSAGE_ENABLED;
    public static final ModConfigSpec.BooleanValue CUSTOM_FIRST_JOIN_MESSAGE;
    public static final ModConfigSpec.ConfigValue<String> CUSTOM_FIRST_JOIN_MESSAGE_TEXT;

    // -------------------------------------------------------------------------
    // Rename detection (broadcast)
    // -------------------------------------------------------------------------
    public static final ModConfigSpec.BooleanValue RENAME_MESSAGE_ENABLED;
    public static final ModConfigSpec.BooleanValue CUSTOM_RENAME_MESSAGE;
    public static final ModConfigSpec.ConfigValue<String> CUSTOM_RENAME_MESSAGE_TEXT;

    // -------------------------------------------------------------------------
    // Regular join (broadcast)
    // -------------------------------------------------------------------------
    public static final ModConfigSpec.BooleanValue JOIN_MESSAGE_ENABLED;
    public static final ModConfigSpec.BooleanValue CUSTOM_JOIN_MESSAGE;
    public static final ModConfigSpec.ConfigValue<String> CUSTOM_JOIN_MESSAGE_TEXT;

    // -------------------------------------------------------------------------
    // Leave (broadcast)
    // -------------------------------------------------------------------------
    public static final ModConfigSpec.BooleanValue LEAVE_MESSAGE_ENABLED;
    public static final ModConfigSpec.BooleanValue CUSTOM_LEAVE_MESSAGE;
    public static final ModConfigSpec.ConfigValue<String> CUSTOM_LEAVE_MESSAGE_TEXT;

    // -------------------------------------------------------------------------
    // Private first join (whisper to joining player)
    // -------------------------------------------------------------------------
    public static final ModConfigSpec.BooleanValue PRIVATE_FIRST_JOIN_MESSAGE;
    public static final ModConfigSpec.ConfigValue<String> PRIVATE_FIRST_JOIN_MESSAGE_TEXT;

    // -------------------------------------------------------------------------
    // Private join (whisper to joining player — NOT sent on first join)
    // -------------------------------------------------------------------------
    public static final ModConfigSpec.BooleanValue PRIVATE_JOIN_MESSAGE;
    public static final ModConfigSpec.ConfigValue<String> PRIVATE_JOIN_MESSAGE_TEXT;

    static {
        BUILDER.comment("JoinLeaveMessages — NeoForge 1.21.1 Configuration",
                "Use %p to replace the player's name.",
                "Use %o for the old player name (only available on rename).",
                "Colors work with the § (section sign) system, e.g. §a for green, §6 for gold.");

        // --- General ---
        BUILDER.push("general");
        DEBUG = BUILDER
                .comment("Enable debug logging to server console")
                .define("debug", false);
        BUILDER.pop();

        // --- First Join ---
        BUILDER.push("first_join");
        FIRST_JOIN_MESSAGE_ENABLED = BUILDER
                .comment("Send a message when a player joins a server for the first time")
                .define("first_join_message_enabled", false);
        CUSTOM_FIRST_JOIN_MESSAGE = BUILDER
                .comment("Use the custom text below instead of the default message")
                .define("custom_first_join_message", false);
        CUSTOM_FIRST_JOIN_MESSAGE_TEXT = BUILDER
                .comment("Custom first join message. Supports §color codes and %p placeholder.")
                .define("custom_first_join_message_text", "§5§lWelcome to our server for the first time %p!");
        BUILDER.pop();

        // --- Rename ---
        BUILDER.push("rename");
        RENAME_MESSAGE_ENABLED = BUILDER
                .comment("Send a message the first time a player joins after changing their Minecraft username")
                .define("rename_message_enabled", false);
        CUSTOM_RENAME_MESSAGE = BUILDER
                .comment("Use the custom text below instead of the default message")
                .define("custom_rename_message", false);
        CUSTOM_RENAME_MESSAGE_TEXT = BUILDER
                .comment("Custom rename message. Supports §color codes, %p (new name), %o (old name).")
                .define("custom_rename_message_text", "Hello %o or should I say %p!");
        BUILDER.pop();

        // --- Join ---
        BUILDER.push("join");
        JOIN_MESSAGE_ENABLED = BUILDER
                .comment("Send a message anytime a player joins (NOT sent alongside the first join message)")
                .define("join_message_enabled", false);
        CUSTOM_JOIN_MESSAGE = BUILDER
                .comment("Use the custom text below instead of the default message")
                .define("custom_join_message", false);
        CUSTOM_JOIN_MESSAGE_TEXT = BUILDER
                .comment("Custom join message. Supports §color codes and %p placeholder.")
                .define("custom_join_message_text", "Hello again %p!");
        BUILDER.pop();

        // --- Leave ---
        BUILDER.push("leave");
        LEAVE_MESSAGE_ENABLED = BUILDER
                .comment("Send a message anytime a player leaves the server")
                .define("leave_message_enabled", false);
        CUSTOM_LEAVE_MESSAGE = BUILDER
                .comment("Use the custom text below instead of the default message")
                .define("custom_leave_message", false);
        CUSTOM_LEAVE_MESSAGE_TEXT = BUILDER
                .comment("Custom leave message. Supports §color codes and %p placeholder.")
                .define("custom_leave_message_text", "§e§l%p signs off!");
        BUILDER.pop();

        // --- Private Messages ---
        BUILDER.push("private_messages");
        PRIVATE_FIRST_JOIN_MESSAGE = BUILDER
                .comment("Send a private message to the player the very first time they join")
                .define("private_first_join_message", false);
        PRIVATE_FIRST_JOIN_MESSAGE_TEXT = BUILDER
                .comment("Private first join message text. Supports §color codes and %p placeholder.")
                .define("private_first_join_message_text",
                        "Glad you are here %p! If you need any help just let us know!");

        PRIVATE_JOIN_MESSAGE = BUILDER
                .comment("Send a private message to the player each time they join (NOT sent on first join if private_first_join_message is enabled)")
                .define("private_join_message", false);
        PRIVATE_JOIN_MESSAGE_TEXT = BUILDER
                .comment("Private join message text. Supports §color codes and %p placeholder.")
                .define("private_join_message_text",
                        "Welcome back %p! If you need any help just let us know!");
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
