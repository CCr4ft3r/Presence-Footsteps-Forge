package eu.ha3.presencefootsteps.sound.generator;

/*import com.minelittlepony.api.pony.meta.Race;
import com.minelittlepony.client.MineLittlePony;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;*/

public class MineLP {
    private static boolean checkCompleted = false;
    private static boolean hasMineLP;

    public static boolean hasPonies() {
        if (!checkCompleted) {
            checkCompleted = true;
            //hasMineLP = FabricLoader.getInstance().isModLoaded("minelp");
            hasMineLP = false; // doesn't officially exist for Forge yet afaik
        }

        return hasMineLP;
    }

    /*public static Locomotion getLocomotion(Entity entity, Locomotion fallback) {

        ResourceLocation texture = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity).getTexture(entity);

        Race race = MineLittlePony.getInstance().getManager().getPony(texture).getRace(false);

        if (race.isHuman()) {
            return fallback;
        }

        return race.hasWings() ? Locomotion.FLYING : Locomotion.QUADRUPED;
    }

    public static Locomotion getLocomotion(PlayerEntity ply) {
        return MineLittlePony.getInstance().getManager().getPony(ply).canFly() ? Locomotion.FLYING : Locomotion.QUADRUPED;
    }*/
}
