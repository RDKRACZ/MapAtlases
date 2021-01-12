package pepjebs.mapatlases.mixin;

import net.minecraft.block.entity.LecternBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pepjebs.mapatlases.MapAtlasesMod;

@Mixin(LecternBlockEntity.class)
public class MapAtlasesLecternBlockEntity {

    @Shadow
    private ItemStack book;

    @Inject(
            method = "hasBook",
            at = @At("RETURN"),
            cancellable = true
    )
    private void hasAtlas(CallbackInfoReturnable<Boolean> cir) {
        if (book.getItem() == Registry.ITEM.get(new Identifier(MapAtlasesMod.MOD_ID, "atlas"))) {
            cir.setReturnValue(true);
        }
    }
}
