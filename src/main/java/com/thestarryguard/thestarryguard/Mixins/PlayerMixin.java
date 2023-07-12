package com.thestarryguard.thestarryguard.Mixins;

import com.thestarryguard.thestarryguard.Events.BlockPlaceEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class PlayerMixin{
    @Inject(at = @At(value = "HEAD"),method = "onPlaced")
    public void PlayerPlaceEvent(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci)
    {
        ActionResult result = BlockPlaceEvent.EVENT.invoker().interact(world,pos,state,placer,itemStack,ci);
    }
}
