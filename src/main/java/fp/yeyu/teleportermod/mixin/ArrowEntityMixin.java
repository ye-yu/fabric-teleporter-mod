package fp.yeyu.teleportermod.mixin;

import fp.yeyu.teleportermod.TeleporterMod;
import fp.yeyu.teleportermod.items.teleporterarrow.ArrowOfTeleportationItemEntity;
import net.minecraft.client.render.entity.ArrowEntityRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowEntityRenderer.class)
public class ArrowEntityMixin {
    @Inject(at=@At("HEAD"), method="getTexture", cancellable = true)
    public void getTexture(ArrowEntity arrowEntity, CallbackInfoReturnable<Identifier> cir){
        if (arrowEntity instanceof ArrowOfTeleportationItemEntity) {
            cir.setReturnValue( new Identifier(TeleporterMod.NAMESPACE, "textures/entity/arrow.png"));
        }
    }
}
