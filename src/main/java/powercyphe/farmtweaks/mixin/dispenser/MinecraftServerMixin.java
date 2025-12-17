package powercyphe.farmtweaks.mixin.dispenser;

import net.minecraft.core.dispenser.ShulkerBoxDispenseBehavior;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DispenserBlock;
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

    @Inject(method = "loadLevel", at = @At("RETURN"))
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
                DispenserBlock.registerBehavior(dispensableItem, new ShulkerBoxDispenseBehavior());
            }
        }
    }
}
