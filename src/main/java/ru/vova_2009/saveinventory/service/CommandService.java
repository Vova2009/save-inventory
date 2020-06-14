package ru.vova_2009.saveinventory.service;

import com.google.inject.ImplementedBy;
import ru.vova_2009.saveinventory.service.impl.SpongeCommandService;

/**
 * @author Vova 2009
 */
@ImplementedBy(SpongeCommandService.class)
public interface CommandService {
    void initialize(Object plugin);
}
