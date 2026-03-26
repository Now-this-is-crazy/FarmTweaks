package powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import powercyphe.farmtweaks.mixin.accessor.LeavesBlockAccessor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LeafDecayEvent implements ServerTickEvents.EndLevelTick {
    private static final LeafDecayEvent INSTANCE = new LeafDecayEvent();

    private final List<DecayingBlock> queue = new ArrayList<>();
    private final List<DecayingBlock> active = new ArrayList<>();

    public static LeafDecayEvent get() {
        return INSTANCE;
    }

    @Override
    public void onEndTick(ServerLevel level) {
        this.active.removeIf((block) -> block.ticks < 0);
        while (!this.queue.isEmpty()) {
            this.active.add(this.queue.removeFirst());
        }

        for (DecayingBlock dBlock : this.active) {
            dBlock.ticks--;

            LeavesBlock block = dBlock.block;
            BlockPos blockPos = dBlock.blockPos;
            BlockState state = level.getBlockState(blockPos);

            this.queueNearby(level, block, blockPos);
            if (state.is(block)) {
                if (dBlock.ticks < 0) {
                    level.removeBlock(blockPos, true);
                }
            }
        }
    }

    public void queue(BlockState state, BlockPos blockPos) {
        List<DecayingBlock> all = new ArrayList<>(this.queue);
        all.addAll(this.active);

        Stream<DecayingBlock> stream = all.stream();
        if (stream.noneMatch((d) -> d.blockPos.equals(blockPos)) && state.getBlock() instanceof LeavesBlock leavesBlock) {
            this.queue.add(new DecayingBlock(leavesBlock, blockPos));
        }
    }

    public void queueNearby(ServerLevel level, LeavesBlock block, BlockPos rootPos) {
        for (Direction dir1 : Direction.values()) {
            for (Direction dir2 : Direction.values()) {
                BlockPos adjPos = rootPos.relative(dir1).relative(dir2, dir1 == dir2 ? 0 : 1);
                BlockState adjState = level.getBlockState(adjPos);

                if (adjState.is(block) && ((LeavesBlockAccessor) block).farmtweaks$decaying(adjState)) {
                    this.queue(adjState, adjPos);
                }
            }
        }
    }

    public static class DecayingBlock {
        LeavesBlock block;
        BlockPos blockPos;
        int ticks;

        public DecayingBlock(LeavesBlock block, BlockPos blockPos) {
            this.block = block;
            this.blockPos = blockPos;

            this.ticks = RandomSource.create().nextInt(7) + 3;
        }

        @Override
        public String toString() {
            return this.blockPos.toString() + " " + this.ticks;
        }
    }
}
