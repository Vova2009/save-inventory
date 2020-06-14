package ru.vova_2009.saveinventory.service;

import com.google.inject.ImplementedBy;
import org.spongepowered.api.entity.living.player.Player;
import ru.vova_2009.saveinventory.service.impl.JsonInventoryDataService;

/**
 * @author Vova 2009
 */
@ImplementedBy(JsonInventoryDataService.class)
public interface InventoryService {

    void saveInventory(Player player);

    void loadInventory(Player player);
}
