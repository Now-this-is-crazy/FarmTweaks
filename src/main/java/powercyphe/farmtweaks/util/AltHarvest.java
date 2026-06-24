package powercyphe.farmtweaks.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Item;
import powercyphe.farmtweaks.FarmTweaksConfig;

import java.util.HashMap;
import java.util.Map;

public record AltHarvest(boolean allowed, double range, int damage) {
    public static final AltHarvest DEFAULT = new AltHarvest(0, 1);
    public static final Map<Item, AltHarvest> VALUES = new HashMap<>();

    public AltHarvest(double range, int damage) {
        this(true, range, damage);
    }

    public static final Codec<AltHarvest> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("allowed", true).forGetter(AltHarvest::allowed),
                    Codec.DOUBLE.optionalFieldOf("range", 0.0).forGetter(AltHarvest::range),
                    Codec.INT.optionalFieldOf("damage", 1).forGetter(AltHarvest::damage)
            ).apply(instance, AltHarvest::new)
    );

    public static boolean isAllowed(Item item) {
        return FarmTweaksConfig.allowAlternateHarvest && VALUES.containsKey(item);
    }

    public static double getRange(Item item) {
        return VALUES.getOrDefault(item, DEFAULT).range();
    }

    public static int getDamage(Item item) {
        return VALUES.getOrDefault(item, DEFAULT).damage();
    }
}
