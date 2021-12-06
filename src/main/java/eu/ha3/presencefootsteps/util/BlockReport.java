package eu.ha3.presencefootsteps.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.google.gson.stream.JsonWriter;
import com.minelittlepony.common.util.GamePaths;

import eu.ha3.presencefootsteps.PresenceFootsteps;
import eu.ha3.presencefootsteps.world.Lookup;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.InfestedBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowyBlock;
import net.minecraft.block.SpreadableBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.block.TransparentBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BlockReport {
    private final Path loc;

    public BlockReport(String baseName) {
        loc = getUniqueFileName(GamePaths.getGameDirectory().resolve("presencefootsteps"), baseName, ".json");
    }

    public CompletableFuture<?> execute(@Nullable Predicate<BlockState> filter) {
        return CompletableFuture.runAsync(() -> {
            try {
                writeReport(filter);
                printResults();
            } catch (Exception e) {
                addMessage(new TranslatableText("pf.report.error", e.getMessage()).styled(s -> s.withColor(Formatting.RED)));
            }
        });
    }

    private void writeReport(@Nullable Predicate<BlockState> filter) throws IOException {
        Files.createDirectories(loc.getParent());

        try (JsonWriter writer = new JsonWriter(Files.newBufferedWriter(loc))) {
            writer.setIndent("    ");
            writer.beginObject();
            writer.name("blocks");
            writer.beginObject();
            Registry.BLOCK.forEach(block -> {
                BlockState state = block.getDefaultState();

                try {
                    if (filter == null || filter.test(state)) {
                        writer.name(Registry.BLOCK.getId(block).toString());
                        writer.beginObject();
                        writer.name("class");
                        writer.value(getClassData(state));
                        writer.name("sound");
                        writer.value(getSoundData(state));
                        writer.name("association");
                        writer.value(PresenceFootsteps.getInstance().getEngine().getIsolator().getBlockMap().getAssociation(state, Lookup.EMPTY_SUBSTRATE));
                        writer.endObject();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            writer.endObject();
            writer.name("unmapped_entities");
            writer.beginArray();
            Registry.ENTITY_TYPE.forEach(type -> {
                if (type.create(MinecraftClient.getInstance().world) instanceof LivingEntity) {
                    Identifier id = Registry.ENTITY_TYPE.getId(type);
                    if (!PresenceFootsteps.getInstance().getEngine().getIsolator().getLocomotionMap().contains(id)) {
                        try {
                            writer.value(id.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            writer.endArray();
            writer.endObject();
        }
    }

    private String getSoundData(BlockState state) {
        if (state.getSoundGroup() == null) {
            return "NULL";
        }
        if (state.getSoundGroup().getStepSound() == null) {
            return "NO_SOUND";
        }
        return state.getSoundGroup().getStepSound().getId().getPath();
    }

    private String getClassData(BlockState state) {
        Block block = state.getBlock();

        String soundName = "";

        if (block instanceof AbstractPressurePlateBlock) soundName += ",EXTENDS_PRESSURE_PLATE";
        if (block instanceof AbstractRailBlock) soundName += ",EXTENDS_RAIL";
        if (block instanceof BlockWithEntity) soundName += ",EXTENDS_CONTAINER";
        if (block instanceof FluidBlock) soundName += ",EXTENDS_LIQUID";
        if (block instanceof PlantBlock) soundName += ",EXTENDS_PLANT";
        if (block instanceof TallPlantBlock) soundName += ",EXTENDS_DOUBLE_PLANT";
        if (block instanceof ConnectingBlock) soundName += ",EXTENDS_CONNECTED_PLANT";
        if (block instanceof LeavesBlock) soundName += ",EXTENDS_LEAVES";
        if (block instanceof SlabBlock) soundName += ",EXTENDS_SLAB";
        if (block instanceof StairsBlock) soundName += ",EXTENDS_STAIRS";
        if (block instanceof SnowyBlock) soundName += ",EXTENDS_SNOWY";
        if (block instanceof SpreadableBlock) soundName += ",EXTENDS_SPREADABLE";
        if (block instanceof FallingBlock) soundName += ",EXTENDS_PHYSICALLY_FALLING";
        if (block instanceof PaneBlock) soundName += ",EXTENDS_PANE";
        if (block instanceof HorizontalFacingBlock) soundName += ",EXTENDS_PILLAR";
        if (block instanceof TorchBlock) soundName += ",EXTENDS_TORCH";
        if (block instanceof CarpetBlock) soundName += ",EXTENDS_CARPET";
        if (block instanceof InfestedBlock) soundName += ",EXTENDS_INFESTED";
        if (block instanceof TransparentBlock) soundName += ",EXTENDS_TRANSPARENT";

        return soundName;
    }

    private void printResults() {
        addMessage(new TranslatableText("pf.report.save")
                .append(new LiteralText(loc.getFileName().toString()).styled(s -> s
                    .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, loc.toString()))
                    .withFormatting(Formatting.UNDERLINE)))
                .styled(s -> s
                    .withColor(Formatting.GREEN)));
    }

    public static void addMessage(Text text) {
        MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(text);
    }

    static Path getUniqueFileName(Path directory, String baseName, String ext) {
        Path loc = null;

        int counter = 0;
        while (loc == null || Files.exists(loc)) {
            loc = directory.resolve(baseName + (counter == 0 ? "" : "_" + counter) + ext);
            counter++;
        }

        return loc;
    }
}
