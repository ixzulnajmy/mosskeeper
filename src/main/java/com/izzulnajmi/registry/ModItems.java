package com.izzulnajmi.registry;

import com.izzulnajmi.MossKeeper;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TypedEntityData;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModItems {

    private static final Identifier MOSS_KEEPER_SPAWN_EGG_ID =
            Identifier.of(MossKeeper.MOD_ID, "moss_keeper_spawn_egg");

    public static final SpawnEggItem MOSS_KEEPER_SPAWN_EGG = Registry.register(
            Registries.ITEM,
            MOSS_KEEPER_SPAWN_EGG_ID,
            new SpawnEggItem(
                    new Item.Settings()
                            .registryKey(RegistryKey.of(RegistryKeys.ITEM, MOSS_KEEPER_SPAWN_EGG_ID))
                            .component(DataComponentTypes.ENTITY_DATA,
                                    TypedEntityData.create((EntityType<?>) ModEntities.MOSS_KEEPER, new NbtCompound()))
            )
    );

    public static void register() {
        MossKeeper.LOGGER.info("Registering Moss Keeper items");
    }
}
