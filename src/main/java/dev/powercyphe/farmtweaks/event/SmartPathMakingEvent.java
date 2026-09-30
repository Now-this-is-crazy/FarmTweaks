package dev.powercyphe.farmtweaks.event;

import dev.powercyphe.farmtweaks.FarmTweaks;
import dev.powercyphe.farmtweaks.FarmTweaksConfig;
import dev.powercyphe.farmtweaks.util.FarmTweaksUtil;
import net.fabricmc.fabric.api.item.v1.BlockTransformerEvents;
import net.fabricmc.fabric.api.item.v1.ResourceSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.ReplaceablePredicate;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedStateProvider;

import java.util.List;
import java.util.Optional;

public class SmartPathMakingEvent implements BlockTransformerEvents.Modify{
    @Override
    public void modify(ResourceKey<BlockTransformer> key, List<BlockTransformer.BlockTransformData> transforms, ResourceSource source, RegistryOps.RegistryInfoLookup registryInfoLookup) {
        if (FarmTweaksUtil.useSmartPathMaking() && key == BlockTransformers.SHOVEL && source.isBuiltIn()) {
            transforms.add(new BlockTransformer.BlockTransformData(
                    Holder.direct(
                        RuleBasedStateProvider.builder()
                                .ifTrueThenProvide(BlockPredicate.allOf(
                                        BlockPredicate.matchesTag(BlockTags.TURNS_INTO_DIRT_PATH),
                                        new ReplaceablePredicate(new Vec3i(0, 1, 0))
                                        ),
                                        Blocks.DIRT_PATH
                                )
                                .build()
                    ), SoundEvents.SHOVEL_FLATTEN, BlockTransformer.TransformParticle.NONE,
                    List.of(Direction.DOWN), Optional.empty(), BlockTransformer.DropStrategy.FROM_MIDDLE,
                    true, BlockTransformer.TransformType.SINGLE_BLOCK, true, 1
            ));
        }
    }
}
