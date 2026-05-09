package com.github.alexthe666.iceandfire.client.gui;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforge;
import com.github.alexthe666.iceandfire.inventory.ContainerDragonforge;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDragonforge extends GuiContainer {
    private static final ResourceLocation TEXTURE_FIRE = new ResourceLocation("iceandfire:textures/gui/dragonforge_fire.png");
    private static final ResourceLocation TEXTURE_ICE = new ResourceLocation("iceandfire:textures/gui/dragonforge_ice.png");
    private static final ResourceLocation TEXTURE_LIGHTNING = new ResourceLocation("iceandfire:textures/gui/dragonforge_lightning.png");
    private static final ResourceLocation TEXTURE_EMPTY = new ResourceLocation("iceandfire:textures/gui/dragonforge_empty.png");
    private final InventoryPlayer playerInventory;
    private final IInventory tileFurnace;

    public GuiDragonforge(InventoryPlayer playerInv, IInventory furnaceInv) {
        super(new ContainerDragonforge(playerInv, furnaceInv));
        this.playerInventory = playerInv;
        this.tileFurnace = furnaceInv;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (tileFurnace != null) {
            String s = I18n.format("tile.iceandfire.dragonforge_core.name");
            this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        }
        this.fontRenderer.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ItemStack blood = this.inventorySlots.getSlot(1).getStack();
        if (blood.getItem() == IafItemRegistry.fire_dragon_blood) {
            this.mc.getTextureManager().bindTexture(TEXTURE_FIRE);
        } else if (blood.getItem() == IafItemRegistry.ice_dragon_blood) {
            this.mc.getTextureManager().bindTexture(TEXTURE_ICE);
        } else if (blood.getItem() == IafItemRegistry.lightning_dragon_blood) {
            this.mc.getTextureManager().bindTexture(TEXTURE_LIGHTNING);
        } else {
            this.mc.getTextureManager().bindTexture(TEXTURE_EMPTY);
        }
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1 = this.getCookProgressScaled(126);
        this.drawTexturedModalRect(k + 12, l + 23, 0, 166, i1, 38);

        if(tileFurnace instanceof TileEntityDragonforge && !((TileEntityDragonforge) this.tileFurnace).assembled()) {
            this.drawTexturedModalRect(k + 119, l + 35, 126, 166, 15, 15);
        }
    }

    private int getCookProgressScaled(int pixels) {
        int j = this.tileFurnace.getField(0);
        return j != 0 ? j * pixels / ((TileEntityDragonforge) tileFurnace).getMaxCookTime() : 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
}
