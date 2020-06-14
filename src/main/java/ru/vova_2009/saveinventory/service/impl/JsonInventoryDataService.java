package ru.vova_2009.saveinventory.service.impl;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.living.player.Player;
import ru.vova_2009.saveinventory.data.InventoryData;
import ru.vova_2009.saveinventory.service.InventoryService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * @author Vova 2009
 */
@Singleton
@SuppressWarnings("UnstableApiUsage")
public class JsonInventoryDataService implements InventoryService {

    private final Path inventories;
    private final Logger logger;

    @Inject
    public JsonInventoryDataService(@ConfigDir(sharedRoot = false) Path config, Logger logger) {
        this.inventories = config.resolve("inventories");
        this.logger = logger;
        if (!Files.exists(inventories)) {
            try {
                Files.createDirectories(inventories);
            } catch (IOException e) {
                logger.error("Unable create inventories directory", e);
            }
        }
    }

    @Override
    public void saveInventory(Player player) {
        Path inventory = inventories.resolve(String.format("%s.json", player.getUniqueId()));
        try {
            if (!Files.exists(inventory)) {
                Files.createFile(inventory);
            }
            ConfigurationLoader<ConfigurationNode> loader =
                    GsonConfigurationLoader.builder().setPath(inventory).build();
            ConfigurationNode rootNode = loader.createEmptyNode(ConfigurationOptions.defaults());
            rootNode.setValue(TypeToken.of(InventoryData.class), new InventoryData(player));
            loader.save(rootNode);
        } catch (IOException | ObjectMappingException e) {
            logger.error("Unable to save inventory", e);
        }


    }

    @Override
    public void loadInventory(Player player) {
        Path inventory = inventories.resolve(String.format("%s.json", player.getUniqueId()));
        if (Files.exists(inventory)) {
            try {
                ConfigurationLoader<ConfigurationNode> loader =
                        GsonConfigurationLoader.builder().setPath(inventory).build();
                ConfigurationNode rootNode = loader.load();
                Optional<InventoryData> data = Optional.ofNullable(rootNode.getValue(TypeToken.of(InventoryData.class)));
                data.ifPresent(inventoryData -> inventoryData.fillInventory(player));
            } catch (IOException | ObjectMappingException e) {
                logger.error("Unable fill player inventory", e);
            }
        }
    }
}
