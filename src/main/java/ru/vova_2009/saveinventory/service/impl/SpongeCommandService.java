package ru.vova_2009.saveinventory.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.spongepowered.api.Sponge;
import ru.vova_2009.saveinventory.command.LoadInventoryCommand;
import ru.vova_2009.saveinventory.command.SaveInventoryCommand;
import ru.vova_2009.saveinventory.service.CommandService;

/**
 * @author Vova 2009
 */
@Singleton
public class SpongeCommandService implements CommandService {

    private final SaveInventoryCommand saveInventory;

    private final LoadInventoryCommand loadInventory;

    @Inject
    public SpongeCommandService(SaveInventoryCommand saveInventory,
                                LoadInventoryCommand loadInventory) {
        this.saveInventory = saveInventory;
        this.loadInventory = loadInventory;
    }

    @Override
    public void initialize(Object plugin) {
        Sponge.getCommandManager().register(plugin, saveInventory.build(), "save");
        Sponge.getCommandManager().register(plugin, loadInventory.build(), "load");
    }
}
