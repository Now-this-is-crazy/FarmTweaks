package powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import powercyphe.farmtweaks.mixin.accessor.LeavesBlockAccessor;

import java.util.*;

public class LeafDecayEvent implements ServerTickEvents.EndLevelTick {
    private static final LeafDecayEvent INSTANCE = new LeafDecayEvent();
    private final Map<BlockPos, Integer> queue = new HashMap<>();

    public static LeafDecayEvent get() {
        return INSTANCE;
    }

    @Override
    public void onEndTick(ServerLevel level) {
        for (BlockPos blockPos : List.copyOf(this.queue.keySet())) {
            if (this.queue.containsKey(blockPos)) {
                int ticks = this.queue.get(blockPos);

                if (ticks < 0) {
                    BlockState state = level.getBlockState(blockPos);

                    if (state.getBlock() instanceof LeavesBlock leaves
                            && ((LeavesBlockAccessor) leaves).farmtweaks$decaying(state)) {
                        state.randomTick(level, blockPos, level.getRandom());
                        this.queue.remove(blockPos);
                    }
                } else {
                    this.queue.put(blockPos, ticks - 1);
                }
            }
        }
    }

    public void queue(ServerLevel level, BlockPos blockPos) {
        this.queue.putIfAbsent(blockPos, level.getRandom().nextInt(30) + 21);
    }

    public void queueNearby(ServerLevel level, BlockPos rootPos) {
        BlockBox box = BlockBox.of(rootPos.offset(-1, -1, -1), rootPos.offset(1, 1, 1));
        for (BlockPos adjPos : box) {
            if (!adjPos.equals(rootPos)) {
                BlockState adjState = level.getBlockState(adjPos);

                if (adjState.getBlock() instanceof LeavesBlock leavesBlock
                        && ((LeavesBlockAccessor) leavesBlock).farmtweaks$decaying(adjState)) {
                    this.queue(level, new BlockPos(adjPos));

                }
            }
        }
    }
}
