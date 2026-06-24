package powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.minecraft.core.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.ScalableParticleOptionsBase;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;
import powercyphe.farmtweaks.util.AltHarvest;
import powercyphe.farmtweaks.util.FarmTweaksUtil;
import powercyphe.farmtweaks.util.Harvestable;

import java.util.Optional;

public class AlternateHoeUseEvent implements ItemEvents.UseOnCallback {

    @Override
    public @Nullable InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        BlockPos blockPos = context.getClickedPos();
        BlockState state = level.getBlockState(blockPos);

        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();

        ItemStack stack = context.getItemInHand();
        Item item = stack.getItem();

        if (AltHarvest.isAllowed(item)) {
            if (level instanceof ServerLevel serverLevel) {

                if (tryHarvest(serverLevel, player, state, blockPos, stack)) {
                    if (FarmTweaksUtil.rangedAltHarvest()) {
                        double range = AltHarvest.getRange(item);
                        int rangeI = (int) (range * 2);

                        if (range > 0) {
                            BlockPos r1 = blockPos.offset(-rangeI, 0, -rangeI);
                            BlockPos r2 = blockPos.offset(rangeI, 0, rangeI);

                            for (BlockPos hPos : BlockBox.of(r1, r2)) {
                                if (Math.sqrt(hPos.distSqr(blockPos)) < range * 1.33) {
                                    BlockState hState = level.getBlockState(hPos);

                                    if (hState.is(state.getBlock())) {
                                        tryHarvest(serverLevel, player, hState, hPos, stack);
                                    }
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
                        stack.hurtAndBreak(AltHarvest.getDamage(item), player, hand.asEquipmentSlot());
                    }
                    return InteractionResult.SUCCESS_SERVER;
                }
            }
        }
        return null;
    }

    public static boolean tryHarvest(ServerLevel level, Player player, BlockState state, BlockPos blockPos, ItemStack heldStack) {
        Block block = state.getBlock();
        Optional<BlockState> harvested = Harvestable.getHarvested(state);

        if (harvested.isPresent()) {
            block.playerDestroy(level, player, blockPos, state, level.getBlockEntity(blockPos), heldStack);
            level.gameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);

            level.setBlockAndUpdate(blockPos, harvested.get());

            BlockParticleOption particleEffect = new BlockParticleOption(ParticleTypes.BLOCK, state);
            level.sendParticles(particleEffect, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 14, 0, 0, 0, 1);
            return true;
        }
        return false;
    }
}
