package net.blay09.mods.waystones.api;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

/** Compile-only stub of the Waystones API; the real interface comes from the Waystones mod. */
public interface IWaystone {
    String getName();

    ResourceKey<Level> getDimension();

    BlockPos getPos();

    boolean hasName();
}
