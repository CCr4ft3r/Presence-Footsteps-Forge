package eu.ha3.presencefootsteps.sound.player;

import eu.ha3.presencefootsteps.sound.State;
import eu.ha3.presencefootsteps.world.Association;
import net.minecraft.entity.LivingEntity;

/**
 * Can generate footsteps using the default Minecraft function.
 */
public interface StepSoundPlayer {
    /**
     * Play a step sound from a block
     */
    boolean playStep(LivingEntity ply, Association assos, State eventType);
}
