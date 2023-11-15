package eu.ha3.presencefootsteps.mixins;

import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

import eu.ha3.presencefootsteps.sound.SoundEngine;
import eu.ha3.presencefootsteps.sound.StepSoundSource;
import eu.ha3.presencefootsteps.sound.generator.StepSoundGenerator;

@Mixin(LivingEntity.class)
abstract class MLivingEntity extends Entity implements StepSoundSource {
    MLivingEntity() {super(null, null);}
    private final StepSoundSource stepSoundSource = new StepSoundSource.Container((LivingEntity)(Object)this);
    @Override
    public Optional<StepSoundGenerator> getStepGenerator(SoundEngine engine) {
        return stepSoundSource.getStepGenerator(engine);
    }
}
