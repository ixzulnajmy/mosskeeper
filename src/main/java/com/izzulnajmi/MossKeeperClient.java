package com.izzulnajmi;

import com.izzulnajmi.client.render.MossKeeperEntityRenderer;
import com.izzulnajmi.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MossKeeperClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.MOSS_KEEPER, MossKeeperEntityRenderer::new);
    }
}
