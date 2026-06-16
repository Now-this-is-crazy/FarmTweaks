package powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;
import powercyphe.farmtweaks.FarmTweaksUtil;
import powercyphe.farmtweaks.init.FTTags;

public class BonemealDuplicationEvent implements ItemEvents.UseOnCallback {

    @Override
    public @Nullable InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        BlockPos blockPos = context.getClickedPos();
        BlockState state = level.getBlockState(blockPos);

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();

        if (FarmTweaksUtil.canBonemeal(state.getBlock()) && stack.is(Items.BONE_MEAL)) {
            if (state.is(FTTags.Blocks.BONEMEAL_DUPLICATABLE)) {

                ItemStack item = state.getBlock().asItem().getDefaultInstance();
                ItemEntity itemEntity = new ItemEntity(level, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, item);
                level.addFreshEntity(itemEntity);

                ParticleUtils.spawnParticleInBlock(level, blockPos, 14, ParticleTypes.HAPPY_VILLAGER);
                level.levelEvent(1505, blockPos.relative(context.getClickedFace()), 15);

                stack.consume(1, player);
                return InteractionResult.SUCCESS;
            }
        }

        return null;
    }
}
