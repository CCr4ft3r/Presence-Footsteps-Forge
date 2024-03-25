package eu.ha3.presencefootsteps.world;

import eu.ha3.presencefootsteps.util.BlockReport.Reportable;
import net.minecraft.resources.ResourceLocation;

public interface Index<K, V> extends Loadable, Reportable {
    V lookup(K key);

    boolean contains(ResourceLocation key);
}
