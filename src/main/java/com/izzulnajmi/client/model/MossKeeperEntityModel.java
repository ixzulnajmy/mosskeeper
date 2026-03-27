package com.izzulnajmi.client.model;

import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;

public class MossKeeperEntityModel extends QuadrupedEntityModel<LivingEntityRenderState> {

    public static final EntityModelLayer LAYER_LOCATION =
            new EntityModelLayer(Identifier.of("moss-keeper", "moss_keeper"), "main");

    public MossKeeperEntityModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();

        // Body/shell: wide and flat like a tortoise carapace
        // pivot y=12, cuboid goes from y=12 down to y=20 (bottom), 8px tall
        root.addChild("body",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(-6.0f, -8.0f, -8.0f, 12, 8, 16, Dilation.NONE),
                ModelTransform.origin(0.0f, 20.0f, 0.0f));

        // Head: small, sticks forward from shell front
        root.addChild("head",
                ModelPartBuilder.create()
                        .uv(0, 24)
                        .cuboid(-3.0f, -3.0f, -5.0f, 6, 6, 5, Dilation.NONE),
                ModelTransform.origin(0.0f, 14.0f, -8.0f));

        // Front legs: stubby, 4px tall, pivot at body-bottom level
        root.addChild("right_front_leg",
                ModelPartBuilder.create()
                        .uv(0, 35)
                        .cuboid(-1.0f, 0.0f, -1.0f, 3, 4, 3, Dilation.NONE),
                ModelTransform.origin(4.0f, 20.0f, -5.0f));

        root.addChild("left_front_leg",
                ModelPartBuilder.create()
                        .uv(12, 35)
                        .cuboid(-2.0f, 0.0f, -1.0f, 3, 4, 3, Dilation.NONE),
                ModelTransform.origin(-4.0f, 20.0f, -5.0f));

        // Hind legs
        root.addChild("right_hind_leg",
                ModelPartBuilder.create()
                        .uv(24, 35)
                        .cuboid(-1.0f, 0.0f, -2.0f, 3, 4, 3, Dilation.NONE),
                ModelTransform.origin(4.0f, 20.0f, 5.0f));

        root.addChild("left_hind_leg",
                ModelPartBuilder.create()
                        .uv(36, 35)
                        .cuboid(-2.0f, 0.0f, -2.0f, 3, 4, 3, Dilation.NONE),
                ModelTransform.origin(-4.0f, 20.0f, 5.0f));

        return TexturedModelData.of(modelData, 64, 64);
    }
}
