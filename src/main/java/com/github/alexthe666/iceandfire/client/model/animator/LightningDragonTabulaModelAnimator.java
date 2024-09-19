package com.github.alexthe666.iceandfire.client.model.animator;

import com.github.alexthe666.iceandfire.client.model.util.EnumDragonAnimations;
import com.github.alexthe666.iceandfire.client.model.util.IIceAndFireTabulaModelAnimator;
import com.github.alexthe666.iceandfire.client.model.util.IceAndFireTabulaModel;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import net.ilexiconn.llibrary.LLibrary;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LightningDragonTabulaModelAnimator extends IceAndFireTabulaModelAnimator implements IIceAndFireTabulaModelAnimator<EntityLightningDragon> {

    private final IceAndFireTabulaModel[] walkPoses = {EnumDragonAnimations.WALK1.lightningdragon_model, EnumDragonAnimations.WALK2.lightningdragon_model, EnumDragonAnimations.WALK3.lightningdragon_model, EnumDragonAnimations.WALK4.lightningdragon_model};
    private final IceAndFireTabulaModel[] flyPoses = {EnumDragonAnimations.FLIGHT1.lightningdragon_model, EnumDragonAnimations.FLIGHT2.lightningdragon_model, EnumDragonAnimations.FLIGHT3.lightningdragon_model, EnumDragonAnimations.FLIGHT4.lightningdragon_model, EnumDragonAnimations.FLIGHT5.lightningdragon_model, EnumDragonAnimations.FLIGHT6.lightningdragon_model};
    private AdvancedModelRenderer[] neckParts;
    private AdvancedModelRenderer[] tailParts;
    private AdvancedModelRenderer[] tailPartsWBody;
    private AdvancedModelRenderer[] toesPartsL;
    private AdvancedModelRenderer[] toesPartsR;

    public LightningDragonTabulaModelAnimator() {
        super(EnumDragonAnimations.GROUND_POSE.lightningdragon_model);
    }

    @Override
    public void init(IceAndFireTabulaModel model) {
        neckParts = new AdvancedModelRenderer[]{model.getCube("Neck1"), model.getCube("Neck2"), model.getCube("Neck3"), model.getCube("Neck3"), model.getCube("Head")};
        tailParts = new AdvancedModelRenderer[]{model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
        tailPartsWBody = new AdvancedModelRenderer[]{model.getCube("BodyLower"), model.getCube("Tail1"), model.getCube("Tail2"), model.getCube("Tail3"), model.getCube("Tail4")};
        toesPartsL = new AdvancedModelRenderer[]{model.getCube("ToeL1"), model.getCube("ToeL2"), model.getCube("ToeL3")};
        toesPartsR = new AdvancedModelRenderer[]{model.getCube("ToeR1"), model.getCube("ToeR2"), model.getCube("ToeR3")};
    }

    @Override
    public void setRotationAngles(IceAndFireTabulaModel model, EntityLightningDragon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float rotationYaw, float rotationPitch, float scale) {
        model.resetToDefaultPose();
        animate(model, entity);
        boolean walking = !entity.isHovering() && !entity.isFlying() && entity.hoverProgress < 20 && entity.flyProgress < 20;
        int currentIndex = walking ? (entity.walkCycle / 10) : (entity.flightCycle / 10);
        int prevIndex = currentIndex - 1;
        if (prevIndex < 0) {
            prevIndex = walking ? 3 : 5;
        }
        IceAndFireTabulaModel currentPosition = walking ? walkPoses[currentIndex] : flyPoses[currentIndex];
        IceAndFireTabulaModel prevPosition = walking ? walkPoses[prevIndex] : flyPoses[prevIndex];
        float delta = ((walking ? entity.walkCycle : entity.flightCycle) / 10.0F) % 1.0F;
        float deltaTicks = delta + (LLibrary.PROXY.getPartialTicks() / 10.0F);

        for (AdvancedModelRenderer cube : model.getCubes().values()) {
            this.genderMob(entity, cube);
            if (walking && entity.flyProgress <= 0.0F && entity.hoverProgress <= 0.0F && entity.modelDeadProgress <= 0.0F) {
                if (prevPosition.getCube(cube.boxName) != null) {
                    AdvancedModelRenderer walkPart = EnumDragonAnimations.GROUND_POSE.lightningdragon_model.getCube(cube.boxName);
                    float prevX = prevPosition.getCube(cube.boxName).rotateAngleX;
                    float prevY = prevPosition.getCube(cube.boxName).rotateAngleY;
                    float prevZ = prevPosition.getCube(cube.boxName).rotateAngleZ;
                    float x = currentPosition.getCube(cube.boxName).rotateAngleX;
                    float y = currentPosition.getCube(cube.boxName).rotateAngleY;
                    float z = currentPosition.getCube(cube.boxName).rotateAngleZ;
                    if (isHorn(cube) || isWing(model, cube) && entity.getAnimation() == EntityDragonBase.ANIMATION_WINGBLAST) {
                        this.addToRotateAngle(cube, limbSwingAmount, walkPart.rotateAngleX, walkPart.rotateAngleY, walkPart.rotateAngleZ);
                    } else {
                        this.addToRotateAngle(cube, limbSwingAmount, prevX + deltaTicks * distance(prevX, x), prevY + deltaTicks * distance(prevY, y), prevZ + deltaTicks * distance(prevZ, z));
                    }
                }
            }
            if (entity.modelDeadProgress > 0.0F) {
                if (!isPartEqual(cube, EnumDragonAnimations.DEAD.lightningdragon_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumDragonAnimations.DEAD.lightningdragon_model.getCube(cube.boxName), entity.modelDeadProgress, 20, cube.boxName.equals("ThighR") || cube.boxName.equals("ThighL"));
                }
            }
            if (entity.sleepProgress > 0.0F) {
                if (!isPartEqual(cube, EnumDragonAnimations.SLEEPING_POSE.lightningdragon_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumDragonAnimations.SLEEPING_POSE.lightningdragon_model.getCube(cube.boxName), entity.sleepProgress, 20, false);
                }
            }
            if (entity.hoverProgress > 0.0F) {
                if (!isPartEqual(cube, EnumDragonAnimations.HOVERING_POSE.lightningdragon_model.getCube(cube.boxName)) && !isWing(model, cube) && !cube.boxName.contains("Tail")) {
                    transitionTo(cube, EnumDragonAnimations.HOVERING_POSE.lightningdragon_model.getCube(cube.boxName), entity.hoverProgress, 20, false);
                }
            }
            if (entity.flyProgress > 0.0F) {
                if (!isPartEqual(cube, EnumDragonAnimations.FLYING_POSE.lightningdragon_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumDragonAnimations.FLYING_POSE.lightningdragon_model.getCube(cube.boxName), entity.flyProgress - entity.diveProgress * 2, 20, false);
                }
            }
            if (entity.sitProgress > 0.0F) {
                if (!entity.isRiding()) {
                    if (!isPartEqual(cube, EnumDragonAnimations.SITTING_POSE.lightningdragon_model.getCube(cube.boxName))) {
                        transitionTo(cube, EnumDragonAnimations.SITTING_POSE.lightningdragon_model.getCube(cube.boxName), entity.sitProgress, 20, false);
                    }
                }
            }
            if (entity.ridingProgress > 0.0F) {
                if (!isHorn(cube) && EnumDragonAnimations.SIT_ON_PLAYER_POSE.lightningdragon_model.getCube(cube.boxName) != null && !isPartEqual(cube, EnumDragonAnimations.SIT_ON_PLAYER_POSE.lightningdragon_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumDragonAnimations.SIT_ON_PLAYER_POSE.lightningdragon_model.getCube(cube.boxName), entity.ridingProgress, 20, false);
                    if (cube.boxName.equals("BodyUpper")) {
                        cube.rotationPointZ += ((-12F - cube.rotationPointZ) / 20) * entity.ridingProgress;
                    }

                }
            }
            if (entity.tackleProgress > 0.0F) {
                if (!isPartEqual(EnumDragonAnimations.TACKLE.lightningdragon_model.getCube(cube.boxName), EnumDragonAnimations.FLYING_POSE.lightningdragon_model.getCube(cube.boxName)) && !isWing(model, cube)) {
                    transitionTo(cube, EnumDragonAnimations.TACKLE.lightningdragon_model.getCube(cube.boxName), entity.tackleProgress, 5, false);
                }
            }
            if (entity.diveProgress > 0.0F) {
                if (!isPartEqual(cube, EnumDragonAnimations.DIVING_POSE.lightningdragon_model.getCube(cube.boxName))) {
                    transitionTo(cube, EnumDragonAnimations.DIVING_POSE.lightningdragon_model.getCube(cube.boxName), entity.diveProgress, 10, false);
                }
            }
            if (entity.fireBreathProgress > 0.0F) {
                if (!isPartEqual(cube, EnumDragonAnimations.STREAM_BREATH.lightningdragon_model.getCube(cube.boxName)) && !isWing(model, cube) && !cube.boxName.contains("Finger")) {
                    transitionTo(cube, EnumDragonAnimations.STREAM_BREATH.lightningdragon_model.getCube(cube.boxName), entity.fireBreathProgress, 5, false);
                }
            }
            if (!walking) {
                float prevX = prevPosition.getCube(cube.boxName).rotateAngleX;
                float prevY = prevPosition.getCube(cube.boxName).rotateAngleY;
                float prevZ = prevPosition.getCube(cube.boxName).rotateAngleZ;
                float x = currentPosition.getCube(cube.boxName).rotateAngleX;
                float y = currentPosition.getCube(cube.boxName).rotateAngleY;
                float z = currentPosition.getCube(cube.boxName).rotateAngleZ;
                this.setRotateAngle(cube, 1F, prevX + deltaTicks * distance(prevX, x), prevY + deltaTicks * distance(prevY, y), prevZ + deltaTicks * distance(prevZ, z));
            }
        }
        float speed_walk = 0.2F;
        float speed_idle = entity.isSleeping() ? 0.025F : 0.05F;
        float speed_fly = 0.2F;
        float degree_walk = 0.5F;
        float degree_idle = entity.isSleeping() ? 0.25F : 0.5F;
        float degree_fly = 0.5F;
        if (!entity.isAIDisabled()) {
            model.faceTarget(rotationYaw, rotationPitch, 2, neckParts);

            if (!walking) {
                model.bob(model.getCube("BodyUpper"), -speed_fly, degree_fly * 5, false, ageInTicks, 1);
                model.walk(model.getCube("BodyUpper"), -speed_fly, degree_fly * 0.1F, false, 0, 0, ageInTicks, 1);
                model.chainWave(tailPartsWBody, speed_fly, degree_fly * -0.1F, 0, ageInTicks, 1);
                model.chainWave(neckParts, speed_fly, degree_fly * 0.2F, -4, ageInTicks, 1);
                model.chainWave(toesPartsL, speed_fly, degree_fly * 0.2F, -2, ageInTicks, 1);
                model.chainWave(toesPartsR, speed_fly, degree_fly * 0.2F, -2, ageInTicks, 1);
                model.walk(model.getCube("ThighR"), -speed_fly, degree_fly * 0.1F, false, 0, 0, ageInTicks, 1);
                model.walk(model.getCube("ThighL"), -speed_fly, degree_fly * 0.1F, true, 0, 0, ageInTicks, 1);
            } else {
                model.bob(model.getCube("BodyUpper"), speed_walk * 2, degree_walk * 1.7F, false, limbSwing, limbSwingAmount);
                model.bob(model.getCube("ThighR"), speed_walk, degree_walk * 1.7F, false, limbSwing, limbSwingAmount);
                model.bob(model.getCube("ThighL"), speed_walk, degree_walk * 1.7F, false, limbSwing, limbSwingAmount);
                model.chainSwing(tailParts, speed_walk, degree_walk * 0.25F, -2, limbSwing, limbSwingAmount);
                model.chainWave(tailParts, speed_walk, degree_walk * 0.15F, 2, limbSwing, limbSwingAmount);
                model.chainSwing(neckParts, speed_walk, degree_walk * 0.15F, 2, limbSwing, limbSwingAmount);
                model.chainWave(neckParts, speed_walk, degree_walk * 0.05F, -2, limbSwing, limbSwingAmount);
                model.chainSwing(tailParts, speed_idle, degree_idle * 0.25F, -2, ageInTicks, 1);
                model.chainWave(tailParts, speed_idle, degree_idle * 0.15F, -2, ageInTicks, 1);
                model.chainWave(neckParts, speed_idle, degree_idle * -0.15F, -3, ageInTicks, 1);
                model.walk(model.getCube("Neck1"), speed_idle, degree_idle * 0.05F, false, 0, 0, ageInTicks, 1);
            }
            model.bob(model.getCube("BodyUpper"), speed_idle, degree_idle * 1.3F, false, ageInTicks, 1);
            model.bob(model.getCube("ThighR"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
            model.bob(model.getCube("ThighL"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
            model.bob(model.getCube("armR1"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
            model.bob(model.getCube("armL1"), speed_idle, -degree_idle * 1.3F, false, ageInTicks, 1);
        }
        if (!entity.isModelDead()) {
            if (entity.turn_buffer != null && !entity.isBeingRidden() && !entity.isRiding() && entity.isBreathingFire()) {
                entity.turn_buffer.applyChainSwingBuffer(neckParts);
            }
            if (entity.tail_buffer != null && !entity.isRiding()) {
                entity.tail_buffer.applyChainSwingBuffer(tailPartsWBody);
            }
        }
    }

    private void genderMob(EntityLightningDragon entity, AdvancedModelRenderer cube) {
        if (!entity.isMale()) {
            AdvancedModelRenderer maleBox = EnumDragonAnimations.MALE.lightningdragon_model.getCube(cube.boxName);
            AdvancedModelRenderer femaleBox = EnumDragonAnimations.FEMALE.lightningdragon_model.getCube(cube.boxName);
            if (maleBox == null || femaleBox == null) {
                return;
            }
            float x = femaleBox.rotateAngleX;
            float y = femaleBox.rotateAngleY;
            float z = femaleBox.rotateAngleZ;
            if (x != maleBox.rotateAngleX || y != maleBox.rotateAngleY || z != maleBox.rotateAngleZ) {
                this.setRotateAngle(cube, 1F, x, y, z);
            }
        }
    }

    @Override
    public void transitionTo(AdvancedModelRenderer from, AdvancedModelRenderer to, float timer, float maxTime, boolean oldFashioned) {
        if (to != null) {
            super.transitionTo(from, to, timer, maxTime, oldFashioned);
        }
    }

    private boolean isWing(IceAndFireTabulaModel model, AdvancedModelRenderer modelRenderer) {
        return isComponentOf(model, "armL1", modelRenderer) || isComponentOf(model, "armR1", modelRenderer);
    }

    private boolean isHorn(AdvancedModelRenderer modelRenderer) {
        return modelRenderer.boxName.contains("Horn");
    }

    public void animate(IceAndFireTabulaModel model, EntityLightningDragon entity) {
        model.llibAnimator.update(entity);
        model.llibAnimator.setAnimation(EntityLightningDragon.ANIMATION_SPEAK);
        model.llibAnimator.startKeyframe(5);
        this.rotate(model.llibAnimator, model.getCube("Jaw"), 18, 0, 0);
        model.llibAnimator.move(model.getCube("Jaw"), 0, 0, 0.2F);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.setStaticKeyframe(5);
        model.llibAnimator.startKeyframe(5);
        this.rotate(model.llibAnimator, model.getCube("Jaw"), 18, 0, 0);
        model.llibAnimator.move(model.getCube("Jaw"), 0, 0, 0.2F);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(5);
        model.llibAnimator.setAnimation(EntityLightningDragon.ANIMATION_BITE);
        model.llibAnimator.startKeyframe(15);
        this.rotate(model.llibAnimator, model.getCube("Neck1"), -12, 0, 0);
        this.rotate(model.llibAnimator, model.getCube("Neck2"), -5, 0, 0);
        this.rotate(model.llibAnimator, model.getCube("Neck3"), 5, 0, 0);
        this.rotate(model.llibAnimator, model.getCube("Head"), 36, 0, 0);
        this.rotate(model.llibAnimator, model.getCube("Jaw"), 20, 0, 0);
        model.llibAnimator.move(model.getCube("Jaw"), 0, 0, 0.2F);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        this.rotate(model.llibAnimator, model.getCube("Neck1"), -2, 0, 0);
        this.rotate(model.llibAnimator, model.getCube("Neck2"), 10, 0, 0);
        this.rotate(model.llibAnimator, model.getCube("Neck3"), 10, 0, 0);
        this.rotate(model.llibAnimator, model.getCube("Head"), 20, 0, 0);
        this.rotate(model.llibAnimator, model.getCube("Jaw"), 20, 0, 0);
        model.llibAnimator.move(model.getCube("Jaw"), 0, 0, 0.2F);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
        model.llibAnimator.setAnimation(EntityLightningDragon.ANIMATION_SHAKEPREY);
        model.llibAnimator.startKeyframe(15);
        moveToPose(model, EnumDragonAnimations.GRAB1.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.GRAB2.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.GRAB_SHAKE1.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.GRAB_SHAKE2.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.GRAB_SHAKE3.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
        model.llibAnimator.setAnimation(EntityLightningDragon.ANIMATION_TAILWHACK);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.TAIL_WHIP1.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.TAIL_WHIP2.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.TAIL_WHIP3.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
        model.llibAnimator.setAnimation(EntityLightningDragon.ANIMATION_WINGBLAST);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.WING_BLAST1.lightningdragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumDragonAnimations.WING_BLAST2.lightningdragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumDragonAnimations.WING_BLAST3.lightningdragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumDragonAnimations.WING_BLAST4.lightningdragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumDragonAnimations.WING_BLAST5.lightningdragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumDragonAnimations.WING_BLAST6.lightningdragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(5);
        moveToPose(model, EnumDragonAnimations.WING_BLAST5.lightningdragon_model);
        model.llibAnimator.move(model.getCube("BodyUpper"), 0, -4F, 0);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
        model.llibAnimator.setAnimation(EntityLightningDragon.ANIMATION_FIRECHARGE);
        model.llibAnimator.startKeyframe(15);
        moveToPose(model, EnumDragonAnimations.STREAM_CHARGE.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
        model.llibAnimator.setAnimation(EntityLightningDragon.ANIMATION_ROAR);
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.ROAR1.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.ROAR2.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.startKeyframe(10);
        moveToPose(model, EnumDragonAnimations.ROAR3.lightningdragon_model);
        model.llibAnimator.endKeyframe();
        model.llibAnimator.resetKeyframe(10);
        model.reset();
    }
}
