package ru.vova_2009.saveinventory.command;

import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;

/**
 * @author Vova 2009
 */
public abstract class AbstractCommand implements CommandExecutor {

    public abstract CommandSpec build();
}
