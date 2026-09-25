package dev.powercyphe.farmtweaks.reloadlistener.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.powercyphe.farmtweaks.util.FarmTweaksUtil;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public record AltHarvest(boolean allowed, double range, int damage) {
    public static final AltHarvest DEFAULT = new AltHarvest(true,0, 1);
    public static final Map<Item, AltHarvest> VALUES = new HashMap<>();

    public static final Codec<AltHarvest> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("altHarvest", DEFAULT.allowed).forGetter(AltHarvest::allowed),
                    Codec.DOUBLE.fieldOf("range").forGetter(AltHarvest::range),
                    Codec.INT.optionalFieldOf("damage", DEFAULT.damage).forGetter(AltHarvest::damage)
            ).apply(instance, AltHarvest::new)
    );

    public AltHarvest(double range, int damage) {
        this(DEFAULT.allowed, range, damage);
    }

    public AltHarvest(double range) {
        this(range, DEFAULT.damage);
    }

    public static boolean isAllowed(Item item) {
        return FarmTweaksUtil.allowAltHarvest() && VALUES.containsKey(item);
    }

    public static double getRange(Item item) {
        return VALUES.getOrDefault(item, DEFAULT).range();
    }

    public static int getDamage(Item item) {
        return VALUES.getOrDefault(item, DEFAULT).damage();
    }
}
