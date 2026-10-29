package bee.post.entity;

import bee.post.block.PackageBlock;
import bee.post.block.Stamp;
import bee.post.registry.MailEntityTypes;
import bee.post.registry.MailItemComponents;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;
import eu.pb4.rayon.api.EntityPhysicsElement;
import eu.pb4.rayon.impl.bullet.collision.body.EntityRigidBody;
import eu.pb4.rayon.impl.bullet.collision.body.shape.MinecraftShape;
import eu.pb4.rayon.impl.bullet.collision.space.MinecraftSpace;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PackageEntity extends Entity implements EntityPhysicsElement {
    private int timeStill = 0;
    public final BlockState state;
    public final ArrayList<Stamp> stamps;

    public PackageEntity(EntityType<PackageEntity> entityType, Level level) {
        super(entityType, level);
        this.state = null;
        this.stamps = new ArrayList<>();
    }

    public PackageEntity(Level level, ItemStack stack) {
        super(MailEntityTypes.PACKAGE, level);

        this.stamps = new ArrayList<>(stack.getOrDefault(MailItemComponents.STAMPS, List.of()));

        this.state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState()
                .setValue(PackageBlock.DAMAGED, stack.getOrDefault(MailItemComponents.DAMAGED, false))
                .setValue(PackageBlock.TAPED, stack.getOrDefault(MailItemComponents.TAPED, false));
    }

    @Override
    public @Nullable EntityRigidBody getRigidBody() {
        return new EntityRigidBody(
                this,
                MinecraftSpace.get(level()),
                new MinecraftShape.Box(new BoundingBox(
                        new Vector3f((float) this.getBoundingBox().minX, (float) this.getBoundingBox().minY, (float) this.getBoundingBox().minZ),
                        new Vector3f((float) this.getBoundingBox().maxX, (float) this.getBoundingBox().maxY, (float) this.getBoundingBox().maxZ)
                )));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getDeltaMovement().length() > 0.1) {
            this.timeStill = 0;
        } else this.timeStill++;

        if (timeStill / 20 > 5) {
            if (level().getBlockState(blockPosition()).canBeReplaced()) {
                level().setBlockAndUpdate(blockPosition(), state);
            }
        }

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    @Override
    public boolean hurtServer(ServerLevel serverLevel, DamageSource damageSource, float f) {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput valueInput) {

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput valueOutput) {

    }
}
