package com.thestarryguard.thestarryguard.Mixins;

import com.thestarryguard.thestarryguard.Events.BucketUseEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.item.BucketItem.class)
public class BucketMixin {
    @Inject(at = @At(value = "RETURN"), method = "use")
    public void onUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        Item item = (Item) (Object) this;
        BucketUseEvent.EVENT.invoker().interact(world, user, hand, item);
    }
}
