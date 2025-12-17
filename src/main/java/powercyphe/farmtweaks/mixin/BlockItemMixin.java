package powercyphe.farmtweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.farmtweaks.FarmTweaks;
import powercyphe.farmtweaks.FarmTweaksUtil;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "useOn", at = @At("HEAD"))
    private void useOnBlockMixin(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        Level world = context.getLevel();
        if (!world.isClientSide()) {
            Player player = context.getPlayer();
            ItemStack stack = context.getItemInHand();
            BlockPos blockPos = context.getClickedPos();
            if (stack.is(FarmTweaks.GRASS_SEEDS_TAG) && FarmTweaksUtil.allowGrassReplenishment()) {
                for (Tuple<Block, Block> pair : FarmTweaks.GRASS_CONVERTABLE) {
                    Block prevBlock = pair.getA();
                    Block convBlock = pair.getB();
                    if (world.getBlockState(blockPos).getBlock() == prevBlock) {
                        if (player != null) {
                            if (!player.isCreative()) {
                                stack.shrink(1);
                            }
                            player.swing(context.getHand(), true);
                        }

                        world.playSound(null, blockPos, SoundEvents.MOSS_PLACE, SoundSource.BLOCKS, 1f, 1f);
                        ((ServerLevel) world).sendParticles(ParticleTypes.COMPOSTER, blockPos.getX() + 0.5, blockPos.getY() + 1.025, blockPos.getZ() + 0.5, 7, 0.25, 0.02, 0.25, 1);


                        world.setBlockAndUpdate(blockPos, convBlock.defaultBlockState());
                        world.gameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);;
                        break;
                    }
                }
            }
        }
    }
}
