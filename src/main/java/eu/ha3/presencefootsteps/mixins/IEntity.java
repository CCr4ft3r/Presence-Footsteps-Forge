package eu.ha3.presencefootsteps.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public interface IEntity {
    @Accessor("nextStepSoundDistance")
    void setNextStepDistance(float value);

    @Accessor("nextStepSoundDistance")
    float getNextStepDistance();
}
