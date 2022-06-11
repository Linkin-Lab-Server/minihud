package fi.dy.masa.minihud.renderer;

import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;

public class RenderObjectVbo extends RenderObjectBase
{
    protected final VertexBuffer vertexBuffer;
    protected final VertexFormat format;
    protected final boolean hasTexture;

    public RenderObjectVbo(VertexFormat.DrawMode glMode, VertexFormat format, Supplier<Shader> shader)
    {
        super(glMode, shader);

        this.vertexBuffer = new VertexBuffer();
        this.format = format;

        boolean hasTexture = false;

        // This isn't really that nice and clean, but it'll do for now...
        for (VertexFormatElement el : this.format.getElements())
        {
            if (el.getType() == VertexFormatElement.Type.UV)
            {
                hasTexture = true;
                break;
            }
        }

        this.hasTexture = hasTexture;
    }

    @Override
    public void uploadData(BufferBuilder buffer)
    {
        BufferBuilder.BuiltBuffer renderBuffer = buffer.end();
        this.vertexBuffer.bind();
        this.vertexBuffer.upload(renderBuffer);
        VertexBuffer.unbind();
    }

    @Override
    public void draw(MatrixStack matrixStack, Matrix4f projMatrix)
    {
        if (this.hasTexture)
        {
            RenderSystem.enableTexture();
        }

        RenderSystem.setShader(this.getShader());

        this.vertexBuffer.bind();
        this.vertexBuffer.draw(matrixStack.peek().getPositionMatrix(), projMatrix, this.getShader().get());
        VertexBuffer.unbind();

        if (this.hasTexture)
        {
            RenderSystem.disableTexture();
        }
    }

    @Override
    public void deleteGlResources()
    {
        this.vertexBuffer.close();
    }
}
