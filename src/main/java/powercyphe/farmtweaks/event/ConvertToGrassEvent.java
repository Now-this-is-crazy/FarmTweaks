package powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.Nullable;
import powercyphe.farmtweaks.FarmTweaks;
import powercyphe.farmtweaks.FarmTweaksUtil;
import powercyphe.farmtweaks.init.FTTags;

public class ConvertToGrassEvent implements ItemEvents.UseOnCallback {

    @Override
    public @Nullable InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        BlockPos blockPos = context.getClickedPos();
        BlockState state = level.getBlockState(blockPos);

        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        ItemStack stack = context.getItemInHand();

        if (level instanceof ServerLevel serverLevel) {
            if (stack.is(FTTags.Items.GRASS_SEEDS_TAG) && FarmTweaksUtil.allowGrassReplenishment()) {
                for (Tuple<Block, Block> pair : FarmTweaks.GRASS_CONVERTABLE) {
                    Block prevBlock = pair.getA();
                    Block convBlock = pair.getB();
                    if (state.getBlock() == prevBlock) {
                        if (player != null) {
                            stack.consume(1, player);
                            player.swing(hand, true);
                        }

                        serverLevel.playSound(null, blockPos, SoundEvents.MOSS_PLACE, SoundSource.BLOCKS, 1f, 1f);
                        serverLevel.sendParticles(ParticleTypes.COMPOSTER, blockPos.getX() + 0.5, blockPos.getY() + 1.025, blockPos.getZ() + 0.5, 7, 0.25, 0.02, 0.25, 1);

                        serverLevel.setBlockAndUpdate(blockPos, convBlock.defaultBlockState());
                        serverLevel.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);;
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return null;
    }
}
