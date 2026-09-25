package dev.powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import dev.powercyphe.farmtweaks.mixin.accessor.LeavesBlockAccessor;
import dev.powercyphe.farmtweaks.util.FarmTweaksUtil;

import java.util.*;

public class LeafDecayEvent implements ServerTickEvents.EndLevelTick {
    private static final LeafDecayEvent INSTANCE = new LeafDecayEvent();
    private final List<BlockPos> queue = new ArrayList<>();

    public static LeafDecayEvent get() {
        return INSTANCE;
    }

    @Override
    public void onEndTick(ServerLevel level) {
        if (!this.queue.isEmpty()) {
            int i = 0;
            while (!this.queue.isEmpty() && i < FarmTweaksUtil.leafDecaySpeed()) {
                BlockPos blockPos = this.queue.removeFirst();
                BlockState state = level.getBlockState(blockPos);

                if (state.getBlock() instanceof LeavesBlock leaves
                        && ((LeavesBlockAccessor) leaves).farmtweaks$decaying(state)) {
                    state.randomTick(level, blockPos, level.getRandom());
                }
                i++;
            }

        }
    }

    public void queue(ServerLevel level, BlockPos blockPos) {
        if (!this.queue.contains(blockPos)) {
            this.queue.add(blockPos);
            Util.shuffle(this.queue, level.getRandom());
        }
    }

    public void queueNearby(ServerLevel level, BlockPos rootPos) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (x == 0 && y == 0 && z == 0) {
                        continue;
                    }
                    BlockPos adjPos = rootPos.offset(x, y, z);
                    BlockState adjState = level.getBlockState(adjPos);

                    if (adjState.getBlock() instanceof LeavesBlock leavesBlock
                            && ((LeavesBlockAccessor) leavesBlock).farmtweaks$decaying(adjState)) {
                        this.queue(level, new BlockPos(adjPos));

                    }
                }
            }
        }
    }
}
