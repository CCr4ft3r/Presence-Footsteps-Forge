package eu.ha3.presencefootsteps;

import java.util.Optional;
import java.util.function.Function;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import com.minelittlepony.common.client.gui.GameGui;
import com.minelittlepony.common.client.gui.ScrollContainer;
import com.minelittlepony.common.client.gui.Tooltip;
import com.minelittlepony.common.client.gui.dimension.Bounds;
import com.minelittlepony.common.client.gui.element.AbstractSlider;
import com.minelittlepony.common.client.gui.element.Button;
import com.minelittlepony.common.client.gui.element.EnumSlider;
import com.minelittlepony.common.client.gui.element.Label;
import com.minelittlepony.common.client.gui.element.Slider;

import eu.ha3.presencefootsteps.util.BlockReport;

class PFOptionsScreen extends GameGui {
    public static final Component TITLE = Component.translatable("menu.pf.title");
    public static final Component VOLUME_MIN = Component.translatable("menu.pf.volume.min");

    private final ScrollContainer content = new ScrollContainer();

    public PFOptionsScreen(@Nullable Screen parent) {
        super(Component.translatable("%s (%s)", TITLE, PresenceFootsteps.getInstance().getKeyBinding().getTranslatedKeyMessage()), parent);
        content.margin.top = 30;
        content.margin.bottom = 30;
        content.getContentPadding().top = 10;
        content.getContentPadding().right = 10;
        content.getContentPadding().bottom = 20;
        content.getContentPadding().left = 10;

    }

    @Override
    protected void init() {
        content.init(this::rebuildContent);
    }

    private void rebuildContent() {
        int left = content.width / 2 - 100;

        int wideLeft = content.width / 2 - 155;
        int wideRight = wideLeft + 160;

        int row = 0;

        PFConfig config = PresenceFootsteps.getInstance().getConfig();

        getChildElements().add(content);

        addButton(new Label(width / 2, 10)).setCentered().getStyle()
                .setText(getTitle());

        var slider = content.addButton(new Slider(wideLeft, row, 0, 100, config.getGlobalVolume()))
            .onChange(config::setGlobalVolume)
            .setTextFormat(this::formatVolume);
        slider.setBounds(new Bounds(row, wideLeft, 310, 20));
        slider.getStyle().setTooltip(Tooltip.of("menu.pf.volume.tooltip", 210)).setTooltipOffset(0, 25);

        row += 10;

        slider = content.addButton(new Slider(wideLeft, row += 24, 0, 100, config.getClientPlayerVolume()))
            .onChange(config::setClientPlayerVolume)
            .setTextFormat(formatVolume("menu.pf.volume.player"));
        slider.setBounds(new Bounds(row, wideLeft, 150, 20));
        slider.getStyle().setTooltip(Tooltip.of("menu.pf.volume.player.tooltip", 210)).setTooltipOffset(0, 25);

        slider = content.addButton(new Slider(wideRight, row, 0, 100, config.getOtherPlayerVolume()))
            .onChange(config::setOtherPlayerVolume)
            .setTextFormat(formatVolume("menu.pf.volume.other_players"));
        slider.setBounds(new Bounds(row, wideRight, 150, 20));
        slider.getStyle().setTooltip(Tooltip.of("menu.pf.volume.other_players.tooltip", 210)).setTooltipOffset(0, 25);

        slider = content.addButton(new Slider(wideLeft, row += 24, 0, 100, config.getHostileEntitiesVolume()))
                .onChange(config::setHostileEntitiesVolume)
                .setTextFormat(formatVolume("menu.pf.volume.hostile_entities"));
        slider.setBounds(new Bounds(row, wideLeft, 150, 20));
        slider.getStyle().setTooltip(Tooltip.of("menu.pf.volume.hostile_entities.tooltip", 210)).setTooltipOffset(0, 25);

        slider = content.addButton(new Slider(wideRight, row, 0, 100, config.getPassiveEntitiesVolume()))
            .onChange(config::setPassiveEntitiesVolume)
            .setTextFormat(formatVolume("menu.pf.volume.passive_entities"));
        slider.setBounds(new Bounds(row, wideRight, 150, 20));
        slider.getStyle().setTooltip(Tooltip.of("menu.pf.volume.passive_entities.tooltip", 210)).setTooltipOffset(0, 25);

        slider = content.addButton(new Slider(wideLeft, row += 24, -100, 100, config.getRunningVolumeIncrease()))
            .onChange(config::setRunningVolumeIncrease)
            .setTextFormat(formatVolume("menu.pf.volume.running"));
        slider.setBounds(new Bounds(row, wideLeft, 310, 20));
        slider.getStyle().setTooltip(Tooltip.of("menu.pf.volume.running.tooltip", 210)).setTooltipOffset(0, 25);

        slider = content.addButton(new Slider(wideLeft, row += 24, 0, 100, config.getWetSoundsVolume()))
                .onChange(config::setWetSoundsVolume)
                .setTextFormat(formatVolume("menu.pf.volume.wet"));
        slider.setBounds(new Bounds(row, wideLeft, 310, 20));
        slider.styled(s -> s.setTooltip(Tooltip.of("menu.pf.volume.wet.tooltip", 210)).setTooltipOffset(0, 25));

        row += 10;

        content.addButton(new EnumSlider<>(left, row += 24, config.getLocomotion())
                .onChange(config::setLocomotion)
                .setTextFormat(v -> v.getValue().getOptionName()))
                .setTooltipFormat(v -> Tooltip.of(v.getValue().getOptionTooltip(), 250))
                .setBounds(new Bounds(row, wideLeft, 310, 20));

        row += 10;

        content.addButton(new Button(wideLeft, row += 24, 150, 20).onClick(sender -> {
            sender.getStyle().setText("menu.pf.global." + config.cycleTargetSelector().name().toLowerCase());
        })).getStyle()
            .setText("menu.pf.global." + config.getEntitySelector().name().toLowerCase());

        content.addButton(new Button(wideRight, row, 150, 20).onClick(sender -> {
            sender.getStyle().setText("menu.pf.multiplayer." + config.toggleMultiplayer());
        })).getStyle()
            .setText("menu.pf.multiplayer." + config.getEnabledMP());

        content.addButton(new Button(wideLeft, row += 24, 150, 20).onClick(sender -> {
            sender.setEnabled(false);
            BlockReport.execute(PresenceFootsteps.getInstance().getEngine().getIsolator(), "report_concise", false).thenRun(() -> sender.setEnabled(true));
        })).setEnabled(minecraft.level != null)
            .getStyle()
            .setText("menu.pf.report.concise");

        content.addButton(new Button(wideRight, row, 150, 20)
            .onClick(sender -> {
                sender.setEnabled(false);
                BlockReport.execute(PresenceFootsteps.getInstance().getEngine().getIsolator(), "report_full", true).thenRun(() -> sender.setEnabled(true));
            }))
            .setEnabled(minecraft.level != null)
            .getStyle()
                .setText("menu.pf.report.full");

        addButton(new Button(left, height - 25)
            .onClick(sender -> finish())).getStyle()
            .setText("gui.done");
    }

    private Component formatVolume(AbstractSlider<Float> slider) {
        if (slider.getValue() <= 0) {
            return VOLUME_MIN;
        }

        return Component.translatable("menu.pf.volume", (int)Math.floor(slider.getValue()));
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float tickDelta) {
        super.render(context, mouseX, mouseY, tickDelta);
        content.render(context, mouseX, mouseY, tickDelta);
    }


    static Function<AbstractSlider<Float>, Component> formatVolume(String key) {
        return slider -> Component.translatable(key, (int)Math.floor(slider.getValue()));
    }
}
