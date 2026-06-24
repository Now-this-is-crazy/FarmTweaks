package powercyphe.farmtweaks.mixin.tree;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(SaplingBlock.class)
public class SaplingBlockMixin {

    @Unique
    private static final TreeGrower SWAMP_OAK = new TreeGrower("swamp_oak", Optional.empty(), Optional.of(TreeFeatures.SWAMP_OAK), Optional.empty());

    @Definition(id = "growTree", method = "Lnet/minecraft/world/level/block/grower/TreeGrower;growTree(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/util/RandomSource;)Z")
    @Expression("@(?).growTree(?, ?, ?, ?, ?)")
    @ModifyExpressionValue(method = "advanceTree", at = @At("MIXINEXTRAS:EXPRESSION"))
    private TreeGrower farmtweaks$swampTree(TreeGrower original, ServerLevel level, BlockPos pos) {
        if (original == TreeGrower.OAK && level.getBiome(pos).is(Biomes.SWAMP)) {
            return SWAMP_OAK;
        }
        return original;
    }
}
