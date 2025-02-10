package powercyphe.farmtweaks.mixin.dispenser;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.BlockPlacementDispenserBehavior;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.farmtweaks.FarmTweaksUtil;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "loadWorld", at = @At("RETURN"))
    private void farmtweaks$dispenserBehavior(CallbackInfo ci) {
        farmtweaks$loadDispenserBehavior();
    }

    @Inject(method = "reloadResources", at = @At("RETURN"))
    private void farmtweaks$dispenserBehavior(Collection<String> dataPacks, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        farmtweaks$loadDispenserBehavior();
    }

    @Unique
    private static void farmtweaks$loadDispenserBehavior() {
        for (Item dispensableItem : FarmTweaksUtil.getDispensableItems()) {
            if (dispensableItem instanceof BlockItem) {
                DispenserBlock.registerBehavior(dispensableItem, new BlockPlacementDispenserBehavior());
            }
        }
    }
}
