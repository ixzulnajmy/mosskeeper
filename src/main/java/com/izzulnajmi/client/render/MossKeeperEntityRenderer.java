package com.izzulnajmi.client.render;

import com.izzulnajmi.entity.MossKeeperEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;

public class MossKeeperEntityRenderer extends EntityRenderer<MossKeeperEntity, EntityRenderState> {

    public MossKeeperEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}
