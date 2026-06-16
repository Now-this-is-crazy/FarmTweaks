package powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import powercyphe.farmtweaks.FarmTweaksUtil;

import java.util.Arrays;
import java.util.List;

public class BonemealGrowEvent implements ItemEvents.UseOnCallback {
    public static final List<Block> GROWABLES = Arrays.asList(Blocks.CACTUS, Blocks.SUGAR_CANE);

    @Override
    public @Nullable InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        BlockPos blockPos = context.getClickedPos();
        BlockState state = level.getBlockState(blockPos);

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (stack.is(Items.BONE_MEAL) && FarmTweaksUtil.canBonemeal(state.getBlock())) {
            if (GROWABLES.contains(state.getBlock())) {
                BlockPos topPos = blockPos;
                while (GROWABLES.contains(level.getBlockState(topPos.above()).getBlock())) {
                    topPos = topPos.above();
                }
                state = level.getBlockState(topPos);

                if (level instanceof ServerLevel serverLevel) {
                    RandomSource ran = serverLevel.getRandom();

                    for (int i = 0; i < 3 + ran.nextInt(4); i++) {
                        state.randomTick(serverLevel, topPos, ran);
                        BlockState newState = level.getBlockState(topPos);

                        if (!newState.is(state.getBlock())) {
                            break;
                        }
                        state = newState;
                    }
                }

                ParticleUtils.spawnParticleInBlock(level, blockPos, 14, ParticleTypes.HAPPY_VILLAGER);
                level.levelEvent(1505, blockPos.relative(context.getClickedFace()), 15);

                stack.consume(1, player);
                return InteractionResult.SUCCESS;
            }
        }
        return null;
    }
}
