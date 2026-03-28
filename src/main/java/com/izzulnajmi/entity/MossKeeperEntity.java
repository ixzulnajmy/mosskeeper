package com.izzulnajmi.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Property;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MossKeeperEntity extends PassiveEntity {

    private static final int SCAN_RADIUS = 8;

    private int mossTick = 0;
    private int glowTick = 0;
    private final List<BlockPos> convertedBlocks = new ArrayList<>();

    public int getGrowthStage() {
        int count = convertedBlocks.size();
        if (count == 0) return 0;
        if (count <= 10) return 1;
        if (count <= 30) return 2;
        return 3;
    }

    public String getStageName() {
        return switch (getGrowthStage()) {
            case 0 -> "Settling";
            case 1 -> "Awakening";
            case 2 -> "Claiming";
            default -> "Ancient";
        };
    }

    private int getMossTickInterval() {
        return switch (getGrowthStage()) {
            case 2 -> 200;
            case 3 -> 100;
            default -> 300;
        };
    }

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
            if (mossTick >= getMossTickInterval()) {
                mossTick = 0;
                tickMossSpreading();
            }
            glowTick++;
            if (glowTick >= 100) {
                glowTick = 0;
                if (getGrowthStage() >= 2) {
                    this.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0, false, false));
                }
            }
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(Items.BONE_MEAL)) {
            if (!this.getEntityWorld().isClient()) {
                for (int i = 0; i < 5; i++) {
                    tickMossSpreading();
                }
                stack.decrement(1);
                ServerWorld serverWorld = (ServerWorld) this.getEntityWorld();
                serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER,
                        this.getX(), this.getY() + this.getHeight() / 2.0, this.getZ(),
                        10, 0.5, 0.5, 0.5, 0.1);
            }
            return ActionResult.SUCCESS;
        }
        if (stack.isOf(Items.SHEARS)) {
            if (!this.getEntityWorld().isClient()) {
                mossTick = 0;
                this.getEntityWorld().playSound(null, this.getBlockPos(),
                        SoundEvents.BLOCK_GRASS_BREAK, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                EquipmentSlot slot = hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                stack.damage(1, player, slot);
            }
            return ActionResult.SUCCESS;
        }
        return super.interactMob(player, hand);
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        World world = this.getEntityWorld();
        if (!world.isClient()) {
            for (BlockPos pos : convertedBlocks) {
                BlockState current = world.getBlockState(pos);
                Block revertTo = getRevertBlock(current.getBlock());
                if (revertTo != null) {
                    world.setBlockState(pos, copyProperties(current, revertTo), Block.NOTIFY_ALL);
                }
            }
            convertedBlocks.clear();
        }
        super.onDeath(damageSource);
    }

    private void tickMossSpreading() {
        World world = this.getEntityWorld();
        BlockPos center = this.getBlockPos();

        List<BlockPos> eligible = new ArrayList<>();
        for (BlockPos pos : BlockPos.iterate(
                center.add(-SCAN_RADIUS, -SCAN_RADIUS, -SCAN_RADIUS),
                center.add(SCAN_RADIUS, SCAN_RADIUS, SCAN_RADIUS))) {
            Block block = world.getBlockState(pos).getBlock();
            if (getMossTarget(block) != null) {
                eligible.add(pos.toImmutable());
            }
        }

        if (eligible.isEmpty()) return;

        BlockPos target = eligible.get(this.random.nextInt(eligible.size()));
        BlockState targetState = world.getBlockState(target);
        Block mossTarget = getMossTarget(targetState.getBlock());
        if (mossTarget == null) return;

        world.setBlockState(target, copyProperties(targetState, mossTarget), Block.NOTIFY_ALL);
        convertedBlocks.add(target);
    }

    @Nullable
    private Block getMossTarget(Block block) {
        if (block == Blocks.STONE || block == Blocks.STONE_BRICKS) return Blocks.MOSSY_STONE_BRICKS;
        if (block == Blocks.COBBLESTONE) return Blocks.MOSSY_COBBLESTONE;
        if (block == Blocks.STONE_BRICK_STAIRS) return Blocks.MOSSY_STONE_BRICK_STAIRS;
        if (block == Blocks.STONE_BRICK_SLAB) return Blocks.MOSSY_STONE_BRICK_SLAB;
        if (block == Blocks.STONE_BRICK_WALL) return Blocks.MOSSY_STONE_BRICK_WALL;
        if (block == Blocks.COBBLESTONE_STAIRS) return Blocks.MOSSY_COBBLESTONE_STAIRS;
        if (block == Blocks.COBBLESTONE_SLAB) return Blocks.MOSSY_COBBLESTONE_SLAB;
        if (block == Blocks.COBBLESTONE_WALL) return Blocks.MOSSY_COBBLESTONE_WALL;
        return null;
    }

    @Nullable
    private Block getRevertBlock(Block block) {
        if (block == Blocks.MOSSY_STONE_BRICKS) return Blocks.STONE_BRICKS;
        if (block == Blocks.MOSSY_COBBLESTONE) return Blocks.COBBLESTONE;
        if (block == Blocks.MOSSY_STONE_BRICK_STAIRS) return Blocks.STONE_BRICK_STAIRS;
        if (block == Blocks.MOSSY_STONE_BRICK_SLAB) return Blocks.STONE_BRICK_SLAB;
        if (block == Blocks.MOSSY_STONE_BRICK_WALL) return Blocks.STONE_BRICK_WALL;
        if (block == Blocks.MOSSY_COBBLESTONE_STAIRS) return Blocks.COBBLESTONE_STAIRS;
        if (block == Blocks.MOSSY_COBBLESTONE_SLAB) return Blocks.COBBLESTONE_SLAB;
        if (block == Blocks.MOSSY_COBBLESTONE_WALL) return Blocks.COBBLESTONE_WALL;
        return null;
    }

    private BlockState copyProperties(BlockState source, Block targetBlock) {
        BlockState target = targetBlock.getDefaultState();
        for (Property<?> property : source.getProperties()) {
            if (target.contains(property)) {
                target = copyProperty(target, source, property);
            }
        }
        return target;
    }

    @SuppressWarnings("unchecked")
    private <T extends Comparable<T>> BlockState copyProperty(BlockState target, BlockState source, Property<T> property) {
        return target.with(property, source.get(property));
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
