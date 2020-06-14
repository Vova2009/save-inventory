package ru.vova_2009.saveinventory.command;

import com.google.inject.Inject;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import ru.vova_2009.saveinventory.service.InventoryService;

/**
 * @author Vova 2009
 */
public class SaveInventoryCommand extends AbstractCommand {

    private final InventoryService inventoryService;


    @Inject
    public SaveInventoryCommand(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Player player = args.<Player>getOne("target")
                .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "Target not found!")));
        inventoryService.saveInventory(player);
        return CommandResult.success();
    }

    @Override
    public CommandSpec build() {
        return CommandSpec.builder()
                .executor(this)
                .permission("save-inventory.command.save")
                .arguments(
                        GenericArguments.playerOrSource(Text.of("target"))
                )
                .build();
    }
}
