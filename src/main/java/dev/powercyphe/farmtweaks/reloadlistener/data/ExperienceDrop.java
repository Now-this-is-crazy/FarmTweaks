package dev.powercyphe.farmtweaks.reloadlistener.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.powercyphe.farmtweaks.FarmTweaksConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public record ExperienceDrop(IntProvider experience, float chance, Optional<CropCondition> cropCondition) {
    public static final Codec<ExperienceDrop> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    IntProviders.CODEC.fieldOf("experience").forGetter(ExperienceDrop::experience),
                    Codec.FLOAT.fieldOf("chance").forGetter(ExperienceDrop::chance),
                    CropCondition.CODEC.optionalFieldOf("cropCondition").forGetter(ExperienceDrop::cropCondition)
            ).apply(instance, ExperienceDrop::new)
    );

    public ExperienceDrop(int min, int max, float chance, CropCondition cropCondition) {
        this(UniformInt.of(min, max), chance, Optional.ofNullable(cropCondition));
    }

    public ExperienceDrop(int min, int max, float chance) {
        this(UniformInt.of(min, max), chance, Optional.empty());
    }

    public void dropExperience(BlockState state, BlockPos blockPos, Level level) {
        if (level instanceof ServerLevel serverLevel) {
            Vec3 pos = new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
            ExperienceOrb.award(serverLevel, pos, this.getExperience(level.getRandom()));
        }
    }

    public boolean shouldDrop(BlockState state, BlockPos blockPos, Level level) {
        return FarmTweaksConfig.cropExperience && this.isInCondition(state, blockPos, level)
                && level.getRandom().nextFloat() >= 1F - this.chance;
    }

    public boolean isInCondition(BlockState state, BlockPos blockPos, Level level) {
        return this.cropCondition.isEmpty() || this.cropCondition.get().isInCondition(state, blockPos, level);
    }

    public int getExperience(RandomSource ran) {
        return this.experience.sample(ran);
    }

    public enum CropCondition implements StringRepresentable {
        HAS_STEM((state, blockPos, level) -> {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                BlockPos stemPos = blockPos.relative(direction);
                BlockState stem = level.getBlockState(stemPos);

                if (stem.getBlock() instanceof AttachedStemBlock) {
                    if (stem.getValue(AttachedStemBlock.FACING) == direction.getOpposite()) {
                        return true;
                    }
                }
            }
            return false;
        });

        public static final Codec<CropCondition> CODEC = StringRepresentable.fromEnum(CropCondition::values);

        private final Check check;

        CropCondition(Check check) {
            this.check = check;
        }

        public boolean isInCondition(BlockState state, BlockPos blockPos, Level level) {
            return this.check.isInCondition(state, blockPos, level);
        }

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase();
        }

        interface Check {
            boolean isInCondition(BlockState state, BlockPos blockPos, Level level);
        }
    }
}
