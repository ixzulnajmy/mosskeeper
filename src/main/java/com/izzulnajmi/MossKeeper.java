package com.izzulnajmi;

import com.izzulnajmi.entity.MossKeeperEntity;
import com.izzulnajmi.registry.ModEntities;
import com.izzulnajmi.registry.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.world.biome.BiomeKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MossKeeper implements ModInitializer {
    public static final String MOD_ID = "moss-keeper";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        ModEntities.register();
        ModItems.register();
        FabricDefaultAttributeRegistry.register(ModEntities.MOSS_KEEPER, MossKeeperEntity.createAttributes());

        BiomeModifications.addSpawn(
                BiomeSelectors.includeByKey(
                        BiomeKeys.LUSH_CAVES,
                        BiomeKeys.JUNGLE,
                        BiomeKeys.SPARSE_JUNGLE
                ),
                SpawnGroup.CREATURE,
                ModEntities.MOSS_KEEPER,
                8, 1, 2
        );

        LOGGER.info("Moss Keeper initialized!");
    }
}
