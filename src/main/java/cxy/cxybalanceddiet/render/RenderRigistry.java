package cxy.cxybalanceddiet.render;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class RenderRigistry {
    public static void register() {

        HudRenderCallback.EVENT.register(ThirstRender::renderThird);

    }
}
