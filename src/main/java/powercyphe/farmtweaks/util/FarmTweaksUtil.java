package powercyphe.farmtweaks.util;

import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FarmTweaksUtil {

    public static void farmtweaks$dropExp(World world, BlockPos pos, Integer chance) {
        if (new Random().nextInt(100) < chance) {
            ExperienceOrbEntity exp = new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), 1);
            world.spawnEntity(exp);
        }
    }
}
