package com.izzulnajmi.client.render;

import com.izzulnajmi.MossKeeper;
import com.izzulnajmi.client.model.MossKeeperEntityModel;
import com.izzulnajmi.entity.MossKeeperEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

public class MossKeeperEntityRenderer extends MobEntityRenderer<MossKeeperEntity, LivingEntityRenderState, MossKeeperEntityModel> {

    private static final Identifier TEXTURE =
            Identifier.of(MossKeeper.MOD_ID, "textures/entity/moss_keeper.png");

    public MossKeeperEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new MossKeeperEntityModel(context.getPart(MossKeeperEntityModel.LAYER_LOCATION)), 0.4f);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTexture(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
