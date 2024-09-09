package org.confluence.phase_journey.common.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(value = LevelRenderer.class, priority = 900)
public abstract class LevelRendererMixin implements org.confluence.phase_journey.common.accessor.LevelRendererAccessor {
	@Shadow
	@Nullable
	private ViewArea viewArea;

	@Shadow
	public abstract void needsUpdate();

	@Override
	public void rebuildAllChunks() {
		if (Minecraft.getInstance().level != null) {
			if (Minecraft.getInstance().levelRenderer != null && Minecraft.getInstance().player != null) {
				for (SectionRenderDispatcher.RenderSection chunk : this.viewArea.sections) {
					chunk.setDirty(true);
				}
				needsUpdate();
			}
		}
	}
}