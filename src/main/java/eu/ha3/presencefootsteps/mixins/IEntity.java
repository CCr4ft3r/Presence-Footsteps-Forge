package eu.ha3.presencefootsteps.mixins;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface IEntity {
    @Accessor("nextStepSoundDistance")
    void setNextStepDistance(float value);

    @Accessor("nextStepSoundDistance")
    float getNextStepDistance();
}
