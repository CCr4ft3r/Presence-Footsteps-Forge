package eu.ha3.presencefootsteps.world;

import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public final class Association {
    public static final Association NOT_EMITTER = new Association();

    private final BlockState blockState;

    private final BlockPos pos;

    private String data = Emitter.NOT_EMITTER;
    private String wetData = Emitter.NOT_EMITTER;

    private LivingEntity source;

    public Association() {
        this(Blocks.AIR.defaultBlockState(), BlockPos.ZERO);
    }

    public Association(BlockState state, BlockPos pos) {
        blockState = state;
        this.pos = pos;
    }

    public Association at(LivingEntity source) {

        if (!isNull()) {
            this.source = source;
        }

        return this;
    }

    public Association withDry(@Nullable String data) {

        if (!isNull() && data != null) {
            this.data = data;
        }

        return this;
    }

    public Association withWet(@Nullable String data) {

        if (!isNull() && data != null) {
            this.wetData = data;
        }

        return this;
    }

    public boolean isNull() {
        return this == NOT_EMITTER;
    }

    public boolean isNotEmitter() {
        return isNull() || (Emitter.isNonEmitter(data) && Emitter.isNonEmitter(wetData));
    }

    public boolean hasAssociation() {
        return !isNotEmitter() && isResult();
    }


    public boolean isResult() {
        return Emitter.isResult(data) || Emitter.isResult(wetData);
    }

    public String getAcousticName() {
        return data;
    }

    public String getWetAcousticName() {
        return wetData;
    }

    public LivingEntity getSource() {
        return source;
    }

    public BlockState getState() {
        return blockState;
    }

    public BlockPos getPos() {
        return pos;
    }

    public SoundType getSoundGroup() {
        return blockState.getSoundType();
    }

    public boolean dataEquals(Association other) {
        return hasAssociation() && Objects.equals(data, other.data);
    }
}
