# JoinLeave Messages — NeoForge 1.21.1 Port

Port of [Phelms215/JoinLeaveMessages](https://github.com/Phelms215/JoinLeaveMessages) for **NeoForge 1.21.1**.

> Basic mod that by default disables the Join/Leave messages on a server.
> Configuration allows a lot of flexibility to messages sent.

---

## Requirements

- Minecraft **1.21.1**
- NeoForge **21.1.x** (tested with 21.1.172)
- Java **21**

---

## Configuration

Config file is generated at:
```
config/joinleavemessages-server.toml
```

### Options

```toml
[general]
    # Enable debug logging to server console
    debug = false

[first_join]
    # Send a message when a player joins for the first time
    first_join_message_enabled = false
    # Use custom text below instead of default
    custom_first_join_message = false
    # Use %p for player name. Supports §color codes.
    custom_first_join_message_text = "§5§lWelcome to our server for the first time %p!"

[rename]
    # Send a message the first time a player joins after changing their username
    rename_message_enabled = false
    custom_rename_message = false
    # %p = new name, %o = old name
    custom_rename_message_text = "Hello %o or should I say %p!"

[join]
    # Send a message anytime a player joins (NOT sent with first join message)
    join_message_enabled = false
    custom_join_message = false
    custom_join_message_text = "Hello again %p!"

[leave]
    # Send a message anytime a player leaves
    leave_message_enabled = false
    custom_leave_message = false
    custom_leave_message_text = "§e§l%p signs off!"

[private_messages]
    # Send a private message to the player the first time they join
    private_first_join_message = false
    private_first_join_message_text = "Glad you are here %p! If you need any help just let us know!"
    # Send a private message each time the player joins (NOT sent on first join)
    private_join_message = false
    private_join_message_text = "Welcome back %p! If you need any help just let us know!"
```

### Color Codes

Use the standard Minecraft `§` (section sign) color codes:

| Code | Color/Format |
|------|-------------|
| §0   | Black        |
| §1   | Dark Blue    |
| §2   | Dark Green   |
| §3   | Dark Aqua    |
| §4   | Dark Red     |
| §5   | Dark Purple  |
| §6   | Gold         |
| §7   | Gray         |
| §8   | Dark Gray    |
| §9   | Blue         |
| §a   | Green        |
| §b   | Aqua         |
| §c   | Red          |
| §d   | Light Purple |
| §e   | Yellow       |
| §f   | White        |
| §l   | **Bold**     |
| §o   | *Italic*     |
| §n   | Underline    |
| §m   | Strikethrough|
| §r   | Reset        |

---

## Building

Requires Java 21 and internet access (downloads NeoForge gradle toolchain).

```bash
# Linux/macOS
./gradlew build

# Windows
gradlew.bat build
```

Output JAR: `build/libs/JoinLeaveMessages-2.0.0.jar`

---

## Player Data

Player join history (first-join tracking and last known username) is stored in:
```
config/joinleavemessages_playerdata.json
```

This file is created automatically and updated as players join.

---

## Differences from Original Forge Version

| Feature | Original Forge | This Port (NeoForge 1.21.1) |
|---|---|---|
| Config format | `.properties` | `.toml` (NeoForge ModConfigSpec) |
| Config location | `config/joinleavemessages.properties` | `config/joinleavemessages-server.toml` |
| Loader | Forge (MinecraftForge) | NeoForge |
| Java version | 17 | 21 |
| Minecraft | 1.18.2 / 1.19.3+ | 1.21.1 |

---

## License

MIT — original mod by [Phelms215](https://github.com/Phelms215).
