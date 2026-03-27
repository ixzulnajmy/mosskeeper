package com.izzulnajmi.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MossKeeperEntity extends PassiveEntity {

    private static final int MOSS_TICK_INTERVAL = 300;
    private static final int SCAN_RADIUS = 8;

    private int mossTick = 0;
    private final List<BlockPos> convertedBlocks = new ArrayList<>();

    public MossKeeperEntity(EntityType<? extends MossKeeperEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return PassiveEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 20.0)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.15);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new WanderAroundFarGoal(this, 0.6));
        this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(3, new LookAroundGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.getEntityWorld().isClient()) {
            mossTick++;
            if (mossTick >= MOSS_TICK_INTERVAL) {
                mossTick = 0;
                tickMossSpreading();
            }
        }
    }

    private void tickMossSpreading() {
        World world = this.getEntityWorld();
        BlockPos center = this.getBlockPos();

        List<BlockPos> eligible = new ArrayList<>();
        for (BlockPos pos : BlockPos.iterate(
                center.add(-SCAN_RADIUS, -SCAN_RADIUS, -SCAN_RADIUS),
                center.add(SCAN_RADIUS, SCAN_RADIUS, SCAN_RADIUS))) {
            Block block = world.getBlockState(pos).getBlock();
            if (block == Blocks.STONE || block == Blocks.STONE_BRICKS || block == Blocks.COBBLESTONE) {
                eligible.add(pos.toImmutable());
            }
        }

        if (eligible.isEmpty()) return;

        BlockPos target = eligible.get(this.random.nextInt(eligible.size()));
        Block block = world.getBlockState(target).getBlock();

        BlockState newState;
        if (block == Blocks.COBBLESTONE) {
            newState = Blocks.MOSSY_COBBLESTONE.getDefaultState();
        } else {
            newState = Blocks.MOSSY_STONE_BRICKS.getDefaultState();
        }

        world.setBlockState(target, newState, Block.NOTIFY_ALL);
        convertedBlocks.add(target);
    }

    @Override
    protected void writeCustomData(WriteView writeView) {
        super.writeCustomData(writeView);
        writeView.put("ConvertedBlocks", BlockPos.CODEC.listOf(), new ArrayList<>(convertedBlocks));
        writeView.putInt("MossTick", mossTick);
    }

    @Override
    protected void readCustomData(ReadView readView) {
        super.readCustomData(readView);
        readView.read("ConvertedBlocks", BlockPos.CODEC.listOf()).ifPresent(list -> {
            convertedBlocks.clear();
            convertedBlocks.addAll(list);
        });
        mossTick = readView.getInt("MossTick", 0);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
