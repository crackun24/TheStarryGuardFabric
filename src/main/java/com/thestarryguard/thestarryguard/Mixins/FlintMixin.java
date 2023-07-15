package com.thestarryguard.thestarryguard.Mixins;

import com.thestarryguard.thestarryguard.Events.PlayerFireBlockEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlintAndSteelItem.class)
public class FlintMixin {
    @Inject(at = @At(value = "RETURN"), method = "useOnBlock")
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir)//在方块上面使用
    {
        World world = context.getWorld();
        BlockState block = world.getBlockState(context.getBlockPos());//获取方块的状态
        BlockPos pos = context.getBlockPos();
        PlayerEntity player = context.getPlayer();//获取玩家对象

        PlayerFireBlockEvent.EVENT.invoker().interact(world, player, block,pos);
    }
}
