package powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.minecraft.core.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;
import powercyphe.farmtweaks.init.FTTags;
import powercyphe.farmtweaks.mixin.accessor.IntegerPropertyAccessor;
import powercyphe.farmtweaks.FarmTweaksUtil;

import java.util.HashMap;
import java.util.Map;

public class AlternateHoeUseEvent implements ItemEvents.UseOnCallback {
    public static final Map<Item, Integer> RANGE_MAP = new HashMap<>();

    @Override
    public @Nullable InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        BlockPos blockPos = context.getClickedPos();
        BlockState state = level.getBlockState(blockPos);

        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        ItemStack stack = context.getItemInHand();

        if (stack.is(FTTags.Items.ALLOWS_ALTERNATE_HOE_USE)) {
            if (level instanceof ServerLevel serverLevel && FarmTweaksUtil.allowAltHoeUse(state.getBlock())) {
                if (tryHarvest(serverLevel, player, state, blockPos, stack)) {
                    if (FarmTweaksUtil.useTieredAltHoeUse()) {
                        int range = RANGE_MAP.getOrDefault(stack.getItem(), 1);
                        if (range > 0) {
                            BlockPos r1 = blockPos.offset(-range, 0, -range);
                            BlockPos r2 = blockPos.offset(range, 0, range);

                            for (BlockPos hPos : BlockBox.of(r1, r2)) {
                                if (Math.sqrt(hPos.distSqr(blockPos)) < range * 1.33) {
                                    tryHarvest(serverLevel, player, level.getBlockState(hPos), hPos, stack);
                                }
                            }
                        }
                    }

                    serverLevel.playSound(null, blockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS);
                    serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                            blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5,
                            1, 0, 0, 0, 1
                    );
                    if (player != null) {
                        player.swing(hand, true);
                        stack.hurtAndBreak(1, player, hand.asEquipmentSlot());
                    }
                    return InteractionResult.SUCCESS_SERVER;
                }
            }
        }
        return null;
    }

    public static boolean tryHarvest(ServerLevel level, Player player, BlockState state, BlockPos blockPos, ItemStack heldStack) {
        Block block = state.getBlock();
        if (FarmTweaksUtil.canHarvest(block)) {

            // Get Age Property
            IntegerProperty ageProperty = null;
            for (Property<?> property : state.getProperties()) {
                if (property.getName().equalsIgnoreCase("age") && property instanceof IntegerProperty intProperty) {
                    ageProperty = intProperty;
                    break;
                }
            }


            if (ageProperty != null) {
                IntegerPropertyAccessor accessor = (IntegerPropertyAccessor) (Object) ageProperty;

                if (state.getValue(ageProperty) >= accessor.farmtweaks$getMax() &&
                        !(state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) &&
                                state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) != DoubleBlockHalf.LOWER)) {
                    block.playerDestroy(level, player, blockPos, state, level.getBlockEntity(blockPos), heldStack);
                    level.setBlockAndUpdate(blockPos, state.setValue(ageProperty, accessor.farmtweaks$getMin()));
                    level.gameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);

                    BlockParticleOption particleEffect = new BlockParticleOption(ParticleTypes.BLOCK, state);
                    level.sendParticles(particleEffect, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 14, 0, 0, 0, 1);

                    return true;
                }
            }
        }
        return false;
    }

    static {
        RANGE_MAP.put(Items.WOODEN_HOE, 0);
        RANGE_MAP.put(Items.STONE_HOE, 0);
        RANGE_MAP.put(Items.COPPER_HOE, 1);
        RANGE_MAP.put(Items.GOLDEN_HOE, 1);
        RANGE_MAP.put(Items.IRON_HOE, 2);
        RANGE_MAP.put(Items.DIAMOND_HOE, 3);
        RANGE_MAP.put(Items.NETHERITE_HOE, 3);
    }
}
