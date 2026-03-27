package com.izzulnajmi;

import com.izzulnajmi.entity.MossKeeperEntity;
import com.izzulnajmi.registry.ModEntities;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MossKeeper implements ModInitializer {
    public static final String MOD_ID = "moss-keeper";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModEntities.register();
        FabricDefaultAttributeRegistry.register(ModEntities.MOSS_KEEPER, MossKeeperEntity.createAttributes());
        LOGGER.info("Moss Keeper initialized!");
    }
}
