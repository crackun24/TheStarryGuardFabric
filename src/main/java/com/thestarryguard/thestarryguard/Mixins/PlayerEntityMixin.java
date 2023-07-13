package com.thestarryguard.thestarryguard.Mixins;

import com.mojang.authlib.GameProfile;
import com.thestarryguard.thestarryguard.Events.PlayerKillEntityEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Inject(at = @At(value = "RETURN"), method = "onKilledOther")
    private void onKill(ServerWorld world, LivingEntity other, CallbackInfoReturnable<Boolean> cir) {
        try {
            System.out.println(other.getName().getString());

            PlayerEntity killer = (PlayerEntity) (Object) this;
            if (other instanceof PlayerEntity)//判断是否为玩家实体
            {
                System.out.println("player");
                PlayerEntity death_player = (PlayerEntity) (Object) other;//获得击杀其它玩家的对象
            }
            PlayerKillEntityEvent.EVENT.invoker().interact(world, killer, other);

        } catch (Exception e) {
            e.printStackTrace();//FIXME
        }
    }
}
