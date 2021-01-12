package pepjebs.mapatlases.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.LecternBlock;
import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.LecternBlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pepjebs.mapatlases.MapAtlasesMod;

@Mixin(LecternBlockEntityRenderer.class)
public class MapAtlasesLecternBlockEntityRenderer {

    private static final SpriteIdentifier ATLAS_TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("entity/lectern_atlas"));

    @Shadow
    @Final
    private BookModel book;

    @Inject(
            method = "render",
            at = @At("INVOKE"),
            cancellable = true
    )
    private void renderAtlasBookEntity(LecternBlockEntity lecternBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j, CallbackInfo cir) {
        BlockState blockState = lecternBlockEntity.getCachedState();
        // The client doesn't have access to "lecternBlockEntity.getBook().getItem()", so this doesn't work...
        if (blockState.get(LecternBlock.HAS_BOOK) && lecternBlockEntity.getBook().getItem() == MapAtlasesMod.MAP_ATLAS) {
            MapAtlasesMod.LOGGER.info("Rendering Atlas...");
            matrixStack.push();
            matrixStack.translate(0.5D, 1.0625D, 0.5D);
            float g = (blockState.get(LecternBlock.FACING)).rotateYClockwise().asRotation();
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-g));
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(67.5F));
            matrixStack.translate(0.0D, -0.125D, 0.0D);
            this.book.setPageAngles(0.0F, 0.1F, 0.9F, 1.2F);
            VertexConsumer vertexConsumer = ATLAS_TEXTURE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
            this.book.method_24184(matrixStack, vertexConsumer, i, j, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStack.pop();
            cir.cancel();
        }
    }
}
