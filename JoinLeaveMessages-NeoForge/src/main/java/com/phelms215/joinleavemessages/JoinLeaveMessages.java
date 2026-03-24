package com.phelms215.joinleavemessages;

import com.mojang.logging.LogUtils;
import com.phelms215.joinleavemessages.config.JLMConfig;
import com.phelms215.joinleavemessages.events.PlayerConnectionEvents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(JoinLeaveMessages.MOD_ID)
public class JoinLeaveMessages {

    public static final String MOD_ID = "joinleavemessages";
    public static final Logger LOGGER = LogUtils.getLogger();

    public JoinLeaveMessages(IEventBus modEventBus, ModContainer modContainer) {
        // Register server config (generates config/joinleavemessages-server.toml)
        modContainer.registerConfig(ModConfig.Type.SERVER, JLMConfig.SPEC, "joinleavemessages-server.toml");

        // Register game events
        modEventBus.addListener(this::commonSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.register(new PlayerConnectionEvents());
        LOGGER.info("[JoinLeaveMessages] Mod initialized for NeoForge 1.21.1");
    }
}
