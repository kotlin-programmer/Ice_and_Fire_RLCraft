package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.EntityGhost;
import net.ilexiconn.llibrary.client.model.ModelAnimator;
import net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGhost extends ModelDragonBase {

    public AdvancedModelRenderer head;
    public AdvancedModelRenderer body;
    public AdvancedModelRenderer armRight;
    public AdvancedModelRenderer armLeft;
    public AdvancedModelRenderer legRight;
    public AdvancedModelRenderer legLeft;
    public AdvancedModelRenderer robe;
    public AdvancedModelRenderer mask;
    public AdvancedModelRenderer hood;
    public AdvancedModelRenderer jaw;
    public AdvancedModelRenderer sleeveRight;
    public AdvancedModelRenderer robeLowerRight;
    public AdvancedModelRenderer robeLowerLeft;
    public AdvancedModelRenderer sleeveLeft;
    private final ModelRenderer shopping_list;
    private final ModelAnimator animator;

    @Override
    public void renderStatue() {
        this.resetToDefaultPose();
    }

    public ModelGhost(float modelScale) {
        this.textureWidth = 128;
        this.textureHeight = 64;
        this.sleeveRight = new AdvancedModelRenderer(this, 33, 35);
        this.sleeveRight.setRotationPoint(0.0F, -0.1F, 0.0F);
        this.sleeveRight.addBox(-2.2F, -2.0F, -2.0F, 3, 12, 4, modelScale);
        this.body = new AdvancedModelRenderer(this, 16, 16);
        this.body.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelScale);
        this.robeLowerRight = new AdvancedModelRenderer(this, 48, 35);
        this.robeLowerRight.mirror = true;
        this.robeLowerRight.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.robeLowerRight.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.armLeft = new AdvancedModelRenderer(this, 40, 16);
        this.armLeft.mirror = true;
        this.armLeft.setRotationPoint(5.0F, 2.0F, -0.0F);
        this.armLeft.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelScale);
        this.setRotateAngle(armLeft, -1.4570009181544104F, 0.10000736647217022F, -0.10000736647217022F);
        this.jaw = new AdvancedModelRenderer(this, 98, 8);
        this.jaw.setRotationPoint(0.0F, -1.4F, 0.2F);
        this.jaw.addBox(-2.0F, 0.0F, -4.5F, 4, 2, 8, modelScale);
        this.setRotateAngle(jaw, 0.13665927909957545F, 0.0F, 0.0F);
        this.hood = new AdvancedModelRenderer(this, 60, 0);
        this.hood.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.hood.addBox(-4.5F, -8.6F, -4.5F, 9, 9, 9, modelScale);
        this.legLeft = new AdvancedModelRenderer(this, 0, 16);
        this.legLeft.mirror = true;
        this.legLeft.setRotationPoint(2.2F, 12.0F, 0.1F);
        this.legLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelScale);
        this.head = new AdvancedModelRenderer(this, 0, 0);
        this.head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelScale);
        this.mask = new AdvancedModelRenderer(this, 40, 8);
        this.mask.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.mask.addBox(-4.0F, -8.6F, -4.4F, 8, 8, 0, modelScale);
        this.armRight = new AdvancedModelRenderer(this, 40, 16);
        this.armRight.setRotationPoint(-5.0F, 2.0F, 0.0F);
        this.armRight.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, modelScale);
        this.setRotateAngle(armRight, -1.4570009181544104F, -0.10000736647217022F, 0.10000736647217022F);
        this.robe = new AdvancedModelRenderer(this, 4, 34);
        this.robe.setRotationPoint(0.0F, 0.1F, 0.0F);
        this.robe.addBox(-4.5F, 0.0F, -2.5F, 9, 12, 5, modelScale);
        this.legRight = new AdvancedModelRenderer(this, 0, 16);
        this.legRight.setRotationPoint(-2.3F, 12.0F, 0.1F);
        this.legRight.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, modelScale);
        this.robeLowerLeft = new AdvancedModelRenderer(this, 48, 35);
        this.robeLowerLeft.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.robeLowerLeft.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelScale);
        this.sleeveLeft = new AdvancedModelRenderer(this, 33, 35);
        this.sleeveLeft.mirror = true;
        this.sleeveLeft.setRotationPoint(0.0F, -0.1F, 0.0F);
        this.sleeveLeft.addBox(-1.0F, -2.0F, -2.0F, 3, 12, 4, modelScale);
        this.armRight.addChild(this.sleeveRight);
        this.legRight.addChild(this.robeLowerRight);
        this.body.addChild(this.armLeft);
        this.mask.addChild(this.jaw);
        this.head.addChild(this.hood);
        this.body.addChild(this.legLeft);
        this.body.addChild(this.head);
        this.head.addChild(this.mask);
        this.body.addChild(this.armRight);
        this.body.addChild(this.robe);
        this.body.addChild(this.legRight);
        this.legLeft.addChild(this.robeLowerLeft);
        this.armLeft.addChild(this.sleeveLeft);
        animator = ModelAnimator.create();
        this.updateDefaultPose();

        shopping_list = new ModelRenderer(this);
        shopping_list.setRotationPoint(0.0F, 24.0F, 0.0F);
        shopping_list.cubeList.add(new ModelBox(shopping_list, 0, 0, -8.0F, -26.0F, -1.0F, 16, 18, 0, 0.0F, false));
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        animate((IAnimatedEntity) entity, f, f1, f2, f3, f4, 1);
        if (!((EntityGhost) entity).isHauntedShoppingList()) {
            this.body.render(f5);
        } else
            this.shopping_list.render(f5);
    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
        this.faceTarget(f3, f4, 1, this.head);
        float speed_walk = 0.6F;
        float speed_idle = 0.05F;
        float degree_walk = 1F;
        float degree_idle = 0.25F;

        float f12 = (float) Math.toRadians(-1.29f) + f1;
        if (f12 < 0.0F) {
            f12 = 0.0F;
        }
        if (f12 > Math.toRadians(20)) {
            f12 = (float) Math.toRadians(20);
        }
        this.body.rotateAngleX = f12;
        this.head.rotateAngleX = this.head.rotateAngleX - f12;
        this.armRight.rotateAngleX = this.armRight.rotateAngleX - f12;
        this.armLeft.rotateAngleX = this.armLeft.rotateAngleX - f12;

        this.walk(jaw, speed_idle * 2F, degree_idle * 0.5F, false, 0, 0.1F, f2, 1);
        this.walk(armRight, speed_idle * 1.5F, degree_idle * 0.4F, false, 2, 0.0F, f2, 1);
        this.walk(armLeft, speed_idle * 1.5F, degree_idle * 0.4F, true, 2, 0.0F, f2, 1);
        this.flap(armRight, speed_idle * 1.5F, degree_idle * 0.2F, false, 2, 0.2F, f2, 1);
        this.flap(armRight, speed_idle * 1.5F, degree_idle * 0.2F, true, 2, 0.2F, f2, 1);
        this.walk(legLeft, speed_idle * 1.5F, degree_idle * 0.4F, false, 2, 0.2F, f2, 1);
        this.walk(legRight, speed_idle * 1.5F, degree_idle * 0.4F, false, 2, 0.2F, f2, 1);
        this.flap(body, speed_idle * 1F, degree_idle * 0.1F, true, 3, 0, f2, 1);
        this.bob(body, speed_idle * 0.5F, degree_idle * 4.1F, false, f2, 1);
        this.bob(body, speed_walk * 0.75F, degree_walk * 2.1F, false, f, f1);

    }

    public void animate(IAnimatedEntity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.resetToDefaultPose();
        setRotationAngles(f, f1, f2, f3, f4, f5, (EntityGhost) entity);
        animator.update(entity);
        if (animator.setAnimation(EntityGhost.ANIMATION_SCARE)) {
            animator.startKeyframe(5);
            animator.move(head, 0, -2, 0);
            animator.move(armLeft, 0, 1, 0);
            animator.move(armRight, 0, 1, 0);
            this.rotateMinus(animator, head, 0, 20, 0);
            this.rotateMinus(animator, jaw, 20, 10, 0);
            this.rotateMinus(animator, armLeft, 30, -12.5F, -70F);
            this.rotateMinus(animator, armRight, -30, 12.5F, 70F);
            animator.endKeyframe();
            animator.startKeyframe(5);
            animator.move(head, 0, -1, 0);
            animator.move(armLeft, 0, -1, 0);
            animator.move(armRight, 0, -1, 0);
            this.rotateMinus(animator, head, 0, -20, 0);
            this.rotateMinus(animator, jaw, 20, -10, 0);
            this.rotateMinus(animator, armLeft, -20, -25, -140);
            this.rotateMinus(animator, armRight, 20, 25, 140);
            animator.endKeyframe();
            animator.startKeyframe(5);
            animator.move(head, 0, -2, 0);
            animator.move(armLeft, 0, 1, 0);
            animator.move(armRight, 0, 1, 0);
            this.rotateMinus(animator, head, 0, 20, 0);
            this.rotateMinus(animator, jaw, 20, 10, 0);
            this.rotateMinus(animator, armLeft, 30, -25, -140);
            this.rotateMinus(animator, armRight, -30, 25, 140);
            animator.endKeyframe();
            animator.startKeyframe(5);
            animator.move(head, 0, -1, 0);
            animator.move(armLeft, 0, -1, 0);
            animator.move(armRight, 0, -1, 0);
            this.rotateMinus(animator, head, 0, -20, 0);
            this.rotateMinus(animator, jaw, 20, -10, 0);
            this.rotateMinus(animator, armLeft, -20, -25, -140);
            this.rotateMinus(animator, armRight, 20, 25, 140);
            animator.endKeyframe();
            animator.resetKeyframe(10);
        }
        if (animator.setAnimation(EntityGhost.ANIMATION_HIT)) {
            animator.startKeyframe(5);
            animator.move(head, 0, -1, 0);
            this.rotateMinus(animator, body, 0, 0F, 0F);
            this.rotateMinus(animator, head, -20, 0F, 0F);
            this.rotateMinus(animator, jaw, 70, 0F, 0F);
            this.rotateMinus(animator, armRight, -180F, 0F, -30F);
            this.rotateMinus(animator, armLeft, -180F, 0F, 30F);
            animator.endKeyframe();
            animator.resetKeyframe(5);
        }
        animator.update(null);
    }
}
