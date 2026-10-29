package bee.post.entity;

import bee.post.block.PackageBlock;
import bee.post.block.Stamp;
import bee.post.registry.MailBlocks;
import bee.post.registry.MailEntityTypes;
import bee.post.registry.MailItemComponents;
import com.jme3.math.Vector3f;
import eu.pb4.physicstoys.other.ShapeUtil;
import eu.pb4.rayon.api.EntityPhysicsElement;
import eu.pb4.rayon.impl.bullet.collision.body.ElementRigidBody;
import eu.pb4.rayon.impl.bullet.collision.body.EntityRigidBody;
import eu.pb4.rayon.impl.bullet.collision.body.shape.MinecraftShape;
import eu.pb4.rayon.impl.bullet.math.Convert;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class PackageEntity extends Entity implements EntityPhysicsElement {
    private int timeStill = 0;
    public final BlockState state;
    public final ArrayList<Stamp> stamps;
    private final EntityRigidBody body = new EntityRigidBody(this);

    public PackageEntity(EntityType<PackageEntity> entityType, Level level) {
        super(entityType, level);
        this.state = MailBlocks.PACKAGE.defaultBlockState();
        this.stamps = new ArrayList<>();
    }

    public PackageEntity(Level level, ItemStack stack) {
        super(MailEntityTypes.PACKAGE, level);

        this.stamps = new ArrayList<>(stack.getOrDefault(MailItemComponents.STAMPS, List.of()));

        this.state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState()
                .setValue(PackageBlock.DAMAGED, stack.getOrDefault(MailItemComponents.DAMAGED, false))
                .setValue(PackageBlock.TAPED, stack.getOrDefault(MailItemComponents.TAPED, false));

        this.body.setMass(10);
        this.body.setFriction(2.5f);
        this.body.setBuoyancyType(ElementRigidBody.BuoyancyType.WATER);
        this.body.setLinearVelocity(new Vector3f());
        this.body.setAngularVelocity(new Vector3f());
        this.body.setPhysicsLocation(Convert.toBullet(this.position()));
    }

    @Override
    public @Nullable EntityRigidBody getRigidBody() {
        return body;
    }

    @Override
    public void push(Entity entity) {
        final var box = body.getCurrentBoundingBox();
        final var location = body.getPhysicsLocation(new Vector3f()).subtract(new Vector3f(0, -box.getYExtent(), 0));
        final var mass = body.getMass();

        final var vanillaBox = body.getCurrentMinecraftBoundingBox();

        final var entityPos = Convert.toBullet(entity.position().add(0, entity.getBoundingBox().getYsize(), 0));
        final var normal = location.subtract(entityPos).multLocal(new Vector3f(1, 0, 1)).normalize();

        final var intersection = entity.getBoundingBox().intersect(vanillaBox);
        final var force = normal.clone()
                .multLocal((float) intersection.getSize() / (float) vanillaBox.getSize())
                .multLocal(mass)
                .multLocal(new Vector3f(1, 0, 1));
        body.applyCentralImpulse(force);

    }

    @Override
    public void push(double deltaX, double deltaY, double deltaZ) {
        var x = new Vector3f((float) deltaX, (float) deltaY, (float) deltaZ);
        x.mult(100).mult(this.body.getMass());
        this.body.applyCentralImpulse(x);
    }

    @Override
    public MinecraftShape.Convex createShape() {
        return ShapeUtil.CUBE;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getDeltaMovement().length() < 0.1) {
            this.timeStill = 0;
        } else this.timeStill++;

        if (timeStill / 20 > 5) {
            if (level().getBlockState(blockPosition()).canBeReplaced()) {
                level().setBlockAndUpdate(blockPosition(), state);
                this.remove(RemovalReason.DISCARDED);
            }
        }

    }

    @Override
    protected @NonNull AABB makeBoundingBox(@NonNull Vec3 vec3) {
        return this.body.getCurrentMinecraftBoundingBox();
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
