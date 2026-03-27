package com.izzulnajmi;

import com.izzulnajmi.client.model.MossKeeperEntityModel;
import com.izzulnajmi.client.render.MossKeeperEntityRenderer;
import com.izzulnajmi.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MossKeeperClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityModelLayerRegistry.registerModelLayer(
                MossKeeperEntityModel.LAYER_LOCATION,
                MossKeeperEntityModel::getTexturedModelData
        );
        EntityRendererRegistry.register(ModEntities.MOSS_KEEPER, MossKeeperEntityRenderer::new);
    }
}
