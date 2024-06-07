package powercyphe.farmtweaks.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.farmtweaks.util.ConfigUtil;

@Mixin(DispenserBehavior.class)
public interface DispenserBehaviorMixin {
    @Inject(method = "registerDefaults", at = @At("HEAD"))
    private static void addBlockItemBehavior(CallbackInfo ci) {
        DispenserBehavior blockItemDispenserBehavior = new FallibleItemDispenserBehavior() {
            protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
                World world = pointer.world();
                BlockPos blockPos = pointer.pos().offset((Direction)pointer.state().get(DispenserBlock.FACING));
                if (world.isAir(blockPos) && world.getBlockState(blockPos.down()).isOf(Blocks.FARMLAND)) {
                    if (!world.isClient) {
                        world.setBlockState(blockPos, ((BlockItem)(Object)stack.getItem()).getBlock().getDefaultState(), 3);
                        world.emitGameEvent((Entity)null, GameEvent.BLOCK_PLACE, blockPos);
                    }

                    stack.decrement(1);
                    this.setSuccess(true);
                }

                return stack;
            }
        };
        for (Item dispensableItem : ConfigUtil.getDispensableItems()) {
            if (dispensableItem instanceof BlockItem) {
                DispenserBlock.registerBehavior(dispensableItem, blockItemDispenserBehavior);
            }
        }
    }
}
