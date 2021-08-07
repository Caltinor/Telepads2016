package subaraki.telepads.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;
import subaraki.telepads.capability.player.TelepadData;
import subaraki.telepads.mod.Telepads;
import subaraki.telepads.network.NetworkHandler;
import subaraki.telepads.network.server.SPacketAddWhiteListEntry;

public class WhiteListScreen extends Screen {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(Telepads.MODID, "textures/gui/whitelist.png");

    private final int tex_x = 142;
    private final int tex_y = 166;
    private final String suggestion;
    EditBox textfield;
    private int center_x;
    private int center_y;
    private boolean is_open = false;

    public WhiteListScreen() {

        super(new TranslatableComponent("screen.whitelist"));
        suggestion = new TranslatableComponent("suggest.name").getContents();
    }

    @Override
    public boolean isPauseScreen() {

        return false;
    }

    @Override
    public void render(PoseStack stack, int x, int y, float partialTicks) {

        this.renderBackground(stack);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);

        blit(stack, center_x - tex_x / 2, center_y - tex_y / 2, 0, 0, tex_x, tex_y);

        // tiny hack to better control the suggestion text.
        if (!textfield.getValue().isEmpty())
            textfield.setSuggestion("");
        else
            textfield.setSuggestion(suggestion);

        super.render(stack, x, y, partialTicks);

        for (int i = 0; i < 9; i++) {
            font.drawShadow(stack, "-", center_x - 62, center_y - 47 + (i * 13), 0x888888);

        }
        TelepadData.get(minecraft.player).ifPresent(data -> {

            if (!data.getWhitelist().isEmpty()) {
                int index = 0;
                for (String name : data.getWhitelist().keySet()) {
                    font.drawShadow(stack, name, center_x - 54, center_y - 48 + (index++ * 10), 0xeeeeee);
                }

            }

            font.drawShadow(stack, data.getWhitelist().size() + "/9", center_x + 45, center_y - 50, 0x002222);

            font.drawShadow(stack, "<add,remove>[playername]", center_x - 65, center_y + 69, 0x555555);

        });
    }

    @Override
    public boolean charTyped(char keyCode, int scanCode) {
        if (!is_open)
            is_open = true;
        else {
            textfield.charTyped(keyCode, scanCode);
            return true;
        }
        return super.charTyped(keyCode, scanCode);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int p_keyPressed_3_) {


        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            String command = textfield.getValue();
            textfield.setValue("");

            NetworkHandler.NETWORK.sendToServer(new SPacketAddWhiteListEntry(command));
            return super.keyPressed(keyCode, scanCode, p_keyPressed_3_);
        }

        if (is_open && !super.keyPressed(keyCode, scanCode, p_keyPressed_3_)) {
            textfield.keyPressed(keyCode, scanCode, p_keyPressed_3_);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, p_keyPressed_3_);
    }

    @Override
    protected void init() {

        super.init();
        center_x = minecraft.getWindow().getGuiScaledWidth() / 2;
        center_y = minecraft.getWindow().getGuiScaledHeight() / 2;
        textfield = new EditBox(font, center_x - tex_x / 2 + 5, center_y - tex_y / 2 + 13, 132, 11, new TextComponent("field_name"));
        textfield.setSuggestion(suggestion);
        // player names must be between 3 and 16 characters, our command 'remove' is the
        // longest word, space included gives 23 characters max
        textfield.setMaxLength(23);
        textfield.setFocus(true);
        textfield.setEditable(true);
        addRenderableWidget(textfield);
    }
}
