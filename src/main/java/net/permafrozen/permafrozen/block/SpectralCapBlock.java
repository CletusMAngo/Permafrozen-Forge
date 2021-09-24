package net.permafrozen.permafrozen.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class SpectralCapBlock extends PlantBlock implements BlockEntityProvider {
    public SpectralCapBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public void onLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (entity.bypassesLandingEffects()) {
            super.onLandedUpon(world, state, pos, entity, fallDistance);
        } else {
            entity.handleFallDamage(fallDistance, 0.0F, DamageSource.FALL);
        }

    }

    public void onEntityLand(BlockView world, Entity entity) {
        if (entity.bypassesLandingEffects()) {
            super.onEntityLand(world, entity);
        } else {
            this.bounce(entity, entity.getVelocity());
        }

    }

    private void bounce(Entity entity, Vec3d vec3d) {
        double d = entity instanceof LivingEntity ? 1.0D : 0.8D;
        entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);

    }

    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        double d = Math.abs(entity.getVelocity().y);
        if (d < 0.1D && !entity.bypassesSteppingEffects()) {
            bounce(entity, entity.getVelocity().add(0, 1, 0));

        }

        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SpectralCapBlockEntity(pos, state);
    }
}
