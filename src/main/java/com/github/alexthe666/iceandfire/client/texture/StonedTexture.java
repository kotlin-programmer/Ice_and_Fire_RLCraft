package com.github.alexthe666.iceandfire.client.texture;

import com.github.alexthe666.iceandfire.IceAndFire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.nio.ByteBuffer;

@SideOnly(Side.CLIENT)
public class StonedTexture extends AbstractTexture {

    private static final ColorConvertOp desaturateConverter = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
    private static final ResourceLocation VANILLA_STONE = new ResourceLocation("textures/blocks/stone.png");
    private static BufferedImage tiledStone128 = null;
    private final ResourceLocation primaryTexture;

    public StonedTexture(ResourceLocation primaryTexture) {
        this.primaryTexture = primaryTexture;
    }

    //Slow but results get cached, shouldnt be too much performance hit unless initially loading a large statue hall or something
    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        IResource iresource = null;
        try {
            BufferedImage entityBuffer = null;
            
            //Read entity texture
            try {
                iresource = resourceManager.getResource(primaryTexture);
                entityBuffer = TextureUtil.readBufferedImage(iresource.getInputStream());
            }
            catch(Exception ignored) {}
            finally {
                IOUtils.closeQuietly(iresource);
            }
            
            //If above failed, likely custom texture (horse), attempt to read active texture to buffered image
            if(entityBuffer == null) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(primaryTexture);
                //There has to be a better way to do this but im bad at render and atleast the results are cached
                //https://computergraphics.stackexchange.com/questions/4936/lwjgl-opengl-get-bufferedimage-from-texture-id
                int format = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_INTERNAL_FORMAT);
                int width = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
                int height = GlStateManager.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
                int channels = format == GL11.GL_RGB ? 3 : 4;
                
                ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * channels);
                entityBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                
                GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, format, GL11.GL_UNSIGNED_BYTE, buffer);
                for(int x = 0; x < width; ++x) {
                    for(int y = 0; y < height; ++y) {
                        int i = (x + y * width) * channels;
                        int r = buffer.get(i) & 0xFF;
                        int g = buffer.get(i + 1) & 0xFF;
                        int b = buffer.get(i + 2) & 0xFF;
                        int a = channels == 4 ? buffer.get(i + 3) & 0xFF : 255;
                        entityBuffer.setRGB(x, y, (a << 24) | (r << 16) | (g << 8) | b);
                    }
                }
            }
            
            //Desaturate entity texture
            //This takes the most performance hit, its not super noticable on vanilla but a bit of a harder hit when using high res texture packs
            //At least results are cached to lessen hit
            desaturateConverter.filter(entityBuffer, entityBuffer);
            BufferedImage finalImage = new BufferedImage(entityBuffer.getWidth(), entityBuffer.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graph = finalImage.createGraphics();
            graph.drawImage(entityBuffer, 0, 0, null);
            
            //Create and cache 128x tiled stone texture if it doesn't exist, helps slightly with performance when tiling large textures
            if(tiledStone128 == null) {
                iresource = resourceManager.getResource(VANILLA_STONE);
                BufferedImage vanillaStoneBuffer = TextureUtil.readBufferedImage(iresource.getInputStream());
                tiledStone128 = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
                Graphics2D tiledStoneGraphics = tiledStone128.createGraphics();
                for(int x = 0; x < tiledStone128.getWidth(); x += vanillaStoneBuffer.getWidth()) {
                    for(int y = 0; y < tiledStone128.getHeight(); y += vanillaStoneBuffer.getHeight()) {
                        tiledStoneGraphics.drawImage(vanillaStoneBuffer, x, y, null);
                    }
                }
            }
            
            //Tile stone across desaturated texture
            //This could be done directly in render with gl blend, but I can not get it to look correct so whatever
            graph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8F));
            for(int x = 0; x < finalImage.getWidth(); x += tiledStone128.getWidth()) {
                for(int y = 0; y < finalImage.getHeight(); y += tiledStone128.getHeight()) {
                    graph.drawImage(tiledStone128, x, y, null);
                }
            }
            
            TextureUtil.uploadTextureImage(this.getGlTextureId(), finalImage);
        }
        catch(Exception ex) {
            IceAndFire.logger.error("Couldn't load desaturated layered image", ex);
        }
        finally {
            IOUtils.closeQuietly(iresource);
        }
    }
}