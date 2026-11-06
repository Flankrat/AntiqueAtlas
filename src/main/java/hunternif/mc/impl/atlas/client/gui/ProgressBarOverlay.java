package hunternif.mc.impl.atlas.client.gui;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

class ProgressBarOverlay {
    /**
     * Total width of the progress bar.
     */
    private final int barWidth;

    /**
     * Total height of the progress bar.
     */
    private final int barHeight;

    private final Font textRenderer;

    @OnlyIn(Dist.CLIENT)
    public ProgressBarOverlay(int barWidth, int barHeight) {
        this.barWidth = barWidth;
        this.barHeight = barHeight;
        textRenderer = Minecraft.getInstance().font;
    }

    /**
     * Render progress bar on the screen.
     */
    @OnlyIn(Dist.CLIENT)
    public void draw(GuiGraphics drawContext, int x, int y) {
        ExportUpdateListener l = ExportUpdateListener.INSTANCE;

        int headerWidth = this.textRenderer.width(l.header);
        drawContext.drawString(this.textRenderer, l.header, x + (barWidth - headerWidth) / 2, y - 14, 0xffffff, false);
        int statusWidth = this.textRenderer.width(l.status);
        drawContext.drawString(this.textRenderer, l.header, x + (barWidth - statusWidth) / 2, y, 0xffffff, false);
        y += 14;

        double p = l.currentProgress / l.maxProgress;
        if (l.maxProgress < 0)
            p = 0;

        //RenderSystem.disableTexture();
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder vb = tessellator.getBuilder();

        vb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        vb.vertex(x, y, 0).color(0.5f, 0.5f, 0.5f, 1).endVertex();
        vb.vertex(x, y + barHeight, 0).color(0.5f, 0.5f, 0.5f, 1).endVertex();
        vb.vertex(x + barWidth, y + barHeight, 0).color(0.5f, 0.5f, 0.5f, 1).endVertex();
        vb.vertex(x + barWidth, y, 0).color(0.5f, 0.5f, 0.5f, 1).endVertex();

        vb.vertex(x, y, 0).color(0.5f, 1, 0.5f, 1).endVertex();
        vb.vertex(x, y + barHeight, 0).color(0.5f, 1, 0.5f, 1).endVertex();
        vb.vertex(x + barWidth * p, y + barHeight, 0).color(0.5f, 1, 0.5f, 1).endVertex();
        vb.vertex(x + barWidth * p, y, 0).color(0.5f, 1, 0.5f, 1).endVertex();

        tessellator.end();

        //RenderSystem.enableTexture();
    }

}
