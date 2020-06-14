package ru.vova_2009.saveinventory;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import ru.vova_2009.saveinventory.service.CommandService;

@Plugin(
        id = "save-inventory",
        name = "Save Inventory",
        description = "Plugin for saving player inventory",
        authors = {
                "Vova 2009"
        }
)
public class SaveInventory {

    private final Logger logger;


    private final CommandService commandService;

    @Inject
    public SaveInventory(Logger logger, CommandService commandService) {
        this.logger = logger;
        this.commandService = commandService;
    }


    @Listener
    public void onServerStart(GameInitializationEvent event) {
        logger.info("Initialize save inventory plugin!");
        commandService.initialize(this);
    }
}
