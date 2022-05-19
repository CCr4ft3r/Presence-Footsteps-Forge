package eu.ha3.mc.quick.update;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import net.fabricmc.loader.api.VersionParsingException;
import net.minecraft.util.GsonHelper;

public record Versions (
        TargettedVersion latest,
        List<TargettedVersion> previous) {

    public Versions(JsonObject json) throws VersionParsingException {
        this(new TargettedVersion(GsonHelper.getAsJsonObject(json, "latest")), new ArrayList<>());
        for (var el : GsonHelper.getAsJsonArray(json, "previous")) {
            previous.add(new TargettedVersion(el.getAsJsonObject()));
        }
    }
}
