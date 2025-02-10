package powercyphe.farmtweaks.mixin;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.farmtweaks.FarmTweaks;
import powercyphe.farmtweaks.FarmTweaksUtil;

@Mixin(BlockItem.class)
public class BlockItemMixin {
    @Inject(method = "useOnBlock", at = @At("HEAD"))
    private void useOnBlockMixin(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        if (!world.isClient()) {
            PlayerEntity player = context.getPlayer();
            ItemStack stack = context.getStack();
            BlockPos blockPos = context.getBlockPos();
            if (stack.isIn(FarmTweaks.GRASS_SEEDS_TAG) && FarmTweaksUtil.allowGrassReplenishment()) {
                for (Pair<Block, Block> pair : FarmTweaks.GRASS_CONVERTABLE) {
                    Block prevBlock = pair.getLeft();
                    Block convBlock = pair.getRight();
                    if (world.getBlockState(blockPos).getBlock() == prevBlock) {
                        if (player != null) {
                            if (!player.isCreative()) {
                                stack.decrement(1);
                            }
                            player.swingHand(context.getHand(), true);
                        }

                        world.playSound(null, blockPos, SoundEvents.BLOCK_MOSS_PLACE, SoundCategory.BLOCKS, 1f, 1f);
                        ((ServerWorld) world).spawnParticles(ParticleTypes.COMPOSTER, blockPos.getX() + 0.5, blockPos.getY() + 1.025, blockPos.getZ() + 0.5, 7, 0.25, 0.02, 0.25, 1);


                        world.setBlockState(blockPos, convBlock.getDefaultState());
                        world.emitGameEvent(player, GameEvent.BLOCK_CHANGE, blockPos);;
                        break;
                    }
                }
            }
        }
    }
}
