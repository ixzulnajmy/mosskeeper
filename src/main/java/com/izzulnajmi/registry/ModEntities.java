package com.izzulnajmi.registry;

import com.izzulnajmi.MossKeeper;
import com.izzulnajmi.entity.MossKeeperEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {

    private static final Identifier MOSS_KEEPER_ID = Identifier.of(MossKeeper.MOD_ID, "moss_keeper");

    public static final EntityType<MossKeeperEntity> MOSS_KEEPER = Registry.register(
            Registries.ENTITY_TYPE,
            MOSS_KEEPER_ID,
            EntityType.Builder.create(MossKeeperEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.7f, 0.6f)
                    .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, MOSS_KEEPER_ID))
    );

    public static void register() {
        MossKeeper.LOGGER.info("Registering Moss Keeper entities");
    }
}
