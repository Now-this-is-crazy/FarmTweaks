package powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockBox;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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

            if (state.is(block)) {
                if (dBlock.ticks < 0) {
                    state.randomTick(level, blockPos, level.getRandom());
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
        BlockBox box = BlockBox.of(rootPos.offset(-1, -1, -1), rootPos.offset(1, 1, 1));
        for (BlockPos adjPos : box) {
            if (!adjPos.equals(rootPos)) {
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
