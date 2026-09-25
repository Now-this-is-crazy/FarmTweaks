package dev.powercyphe.farmtweaks.event;

import dev.powercyphe.farmtweaks.reloadlistener.data.Harvestable;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CropExperienceEvent implements PlayerBlockBreakEvents.Before, PlayerBlockBreakEvents.After, PlayerBlockBreakEvents.Canceled {
    public static final CropExperienceEvent INSTANCE = new CropExperienceEvent();
    private static final List<BlockPos> QUEUE = new ArrayList<>();

    @Override
    public boolean beforeBlockBreak(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (!player.isCreative() && Harvestable.shouldDropExperience(state, pos, level)) {
            QUEUE.add(pos);
        }
        return true;
    }

    @Override
    public void afterBlockBreak(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (QUEUE.contains(pos)) {
            Harvestable.tryExperienceDrop(state, pos, level);
            QUEUE.remove(pos);
        }
    }

    @Override
    public void onBlockBreakCanceled(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        QUEUE.remove(pos);
    }
}
