package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.client.model.util.IEntityLivingBaseRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class ModelBloodedLightningDragonArmor extends ModelLightningDragonArmor {
	
	private static final ResourceLocation BLOOD_FRAME_1 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor1.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_1 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor_legs1.png");
	private static final ResourceLocation BLOOD_FRAME_2 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor2.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_2 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor_legs2.png");
	private static final ResourceLocation BLOOD_FRAME_3 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor3.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_3 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor_legs3.png");
	private static final ResourceLocation BLOOD_FRAME_4 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor4.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_4 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor_legs4.png");
	private static final ResourceLocation BLOOD_FRAME_5 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor5.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_5 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor_legs5.png");
	private static final ResourceLocation BLOOD_FRAME_6 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor6.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_6 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor_legs6.png");
	private static final ResourceLocation BLOOD_FRAME_7 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor7.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_7 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor_legs7.png");
	private static final ResourceLocation BLOOD_FRAME_8 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor8.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_8 = new ResourceLocation("iceandfire:textures/models/armor/" + "shocked" + "_armor_legs8.png");
	
	public ModelRenderer head_expanded;
	public ModelRenderer head_jaw;
	public ModelRenderer head_snout;
	public ModelRenderer head_rightTeethUpper;
	public ModelRenderer head_leftTeethUpper;
	public ModelRenderer head_rightTeethLower;
	public ModelRenderer head_leftTeethLower;
	public ModelRenderer head_fire1;
	public ModelRenderer head_fire2;
	public ModelRenderer rightHorn_upper1;
	public ModelRenderer rightHorn_upper2;
	public ModelRenderer rightHorn_middle;
	public ModelRenderer rightHorn_lower;
	public ModelRenderer leftHorn_upper1;
	public ModelRenderer leftHorn_upper2;
	public ModelRenderer leftHorn_middle;
	public ModelRenderer leftHorn_lower;
	public ModelRenderer headWear_expanded;
	public ModelRenderer body_expanded;
	public ModelRenderer body_fire1;
	public ModelRenderer body_fire2;
	public ModelRenderer body_bone1;
	public ModelRenderer body_bone2;
	public ModelRenderer body_bone3;
	public ModelRenderer rightArm_expanded;
	public ModelRenderer rightArm_fire1;
	public ModelRenderer rightArm_fire2;
	public ModelRenderer rightArm_bone1;
	public ModelRenderer rightArm_bone2;
	public ModelRenderer leftArm_expanded;
	public ModelRenderer leftArm_fire1;
	public ModelRenderer leftArm_bone1;
	public ModelRenderer leftArm_bone2;
	public ModelRenderer rightLeg_expanded;
	public ModelRenderer rightLeg_bone1;
	public ModelRenderer rightLeg_bone2;
	public ModelRenderer rightLeg_bone3;
	public ModelRenderer rightLeg_fire1;
	public ModelRenderer rightLeg_fire1_r1;
	public ModelRenderer rightLeg_fire2;
	public ModelRenderer leftLeg_expanded;
	public ModelRenderer leftLeg_bone1;
	public ModelRenderer leftLeg_bone2;
	public ModelRenderer leftLeg_bone3;
	public ModelRenderer leftLeg_fire1;
	public ModelRenderer leftLeg_fire2;
	
	private final boolean LEGS;
	
	public ModelBloodedLightningDragonArmor(float modelSize, boolean legs) {
		super(modelSize, legs);
		this.LEGS = legs;
		
		//Head
		
		this.head_expanded = new ModelRenderer(this);
		this.head_expanded.cubeList.add(new ModelBox(head_expanded, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.025F, false));
		
		this.head_jaw = new ModelRenderer(this);
		this.head_jaw.setRotationPoint(0.0F, -5.4F, 0.0F);
		this.setRotateAngle(head_jaw, -0.0911F, 0.0F, 0.0F);
		this.head_jaw.cubeList.add(new ModelBox(head_jaw, 6, 51, -3.5F, 4.0F, -7.4F, 7, 2, 5, 0.025F, false));
		
		this.head_snout = new ModelRenderer(this);
		this.head_snout.setRotationPoint(0.0F, -5.6F, 0.0F);
		this.setRotateAngle(head_snout, 0.0456F, 0.0F, 0.0F);
		this.head_snout.cubeList.add(new ModelBox(head_snout, 6, 44, -3.5F, -2.8F, -8.8F, 7, 2, 5, 0.025F, false));
		
		this.head_rightTeethUpper = new ModelRenderer(this);
		this.head_rightTeethUpper.setRotationPoint(0.0F, -1.0F, 0.0F);
		this.head_rightTeethUpper.cubeList.add(new ModelBox(head_rightTeethUpper, 6, 34, -3.6F, 0.1F, -8.9F, 4, 1, 5, 0.025F, false));
		this.head_snout.addChild(head_rightTeethUpper);
		
		this.head_leftTeethUpper = new ModelRenderer(this);
		this.head_leftTeethUpper.setRotationPoint(0.0F, -1.0F, 0.0F);
		this.head_leftTeethUpper.cubeList.add(new ModelBox(head_leftTeethUpper, 6, 34, -0.4F, 0.1F, -8.9F, 4, 1, 5, 0.025F, true));
		this.head_snout.addChild(head_leftTeethUpper);
		
		this.head_rightTeethLower = new ModelRenderer(this);
		this.head_rightTeethLower.setRotationPoint(0.0F, 3.0F, 1.4F);
		this.head_rightTeethLower.cubeList.add(new ModelBox(head_rightTeethLower, 6, 34, -3.6F, 0.1F, -8.9F, 4, 1, 5, 0.025F, false));
		this.head_jaw.addChild(head_rightTeethLower);
		
		this.head_leftTeethLower = new ModelRenderer(this);
		this.head_leftTeethLower.setRotationPoint(0.0F, 3.0F, 1.4F);
		this.head_leftTeethLower.cubeList.add(new ModelBox(head_leftTeethLower, 6, 34, -0.4F, 0.1F, -8.9F, 4, 1, 5, 0.025F, true));
		this.head_jaw.addChild(head_leftTeethLower);
		
		this.head_fire1 = new ModelRenderer(this);
		this.head_fire1.setRotationPoint(-5.025F, -9.6F, 2.9F);
		this.setRotateAngle(head_fire1, -1.0741F, 0.1152F, -1.5086F);
		this.head_fire1.cubeList.add(new ModelBox(head_fire1, 54, 55, -4.0F, -6.0F, -1.0F, 5, 9, 0, 0.0F, false));
		
		this.head_fire2 = new ModelRenderer(this);
		this.head_fire2.setRotationPoint(3.1F, -5.1F, -2.25F);
		this.setRotateAngle(head_fire2, -1.2141F, -0.1231F, 1.4468F);
		this.head_fire2.cubeList.add(new ModelBox(head_fire2, 32, 54, -1.0F, -12.0F, -1.0F, 5, 10, 0, 0.0F, false));
		
		//Right horns
		
		this.rightHorn_upper1 = new ModelRenderer(this);
		this.rightHorn_upper1.setRotationPoint(-3.6F, -8.0F, 0.0F);
		this.setRotateAngle(rightHorn_upper1, 0.4363323129985824F, -0.33161255787892263F, -0.19198621771937624F);
		this.rightHorn_upper1.cubeList.add(new ModelBox(rightHorn_upper1, 48, 44, -1.0F, -0.5F, 0.0F, 2, 3, 5, 0.025F, false));
		
		this.rightHorn_upper2 = new ModelRenderer(this);
		this.rightHorn_upper2.setRotationPoint(0.0F, 0.3F, 4.5F);
		this.setRotateAngle(rightHorn_upper2, -0.07504915783575616F, 0.5009094953223726F, 0.0F);
		this.rightHorn_upper2.cubeList.add(new ModelBox(rightHorn_upper2, 46, 36, -0.5F, -0.8F, 0.0F, 1, 2, 5, 0.025F, false));
		this.rightHorn_upper1.addChild(rightHorn_upper2);
		
		this.rightHorn_middle = new ModelRenderer(this);
		this.rightHorn_middle.setRotationPoint(-4.0F, -3.0F, 0.7F);
		this.setRotateAngle(rightHorn_middle, -0.06981317007977318F, -0.4886921905584123F, -0.08726646259971647F);
		this.rightHorn_middle.cubeList.add(new ModelBox(rightHorn_middle, 47, 37, -0.5F, -0.8F, 0.0F, 1, 2, 4, 0.025F, false));
		
		this.rightHorn_lower = new ModelRenderer(this);
		this.rightHorn_lower.setRotationPoint(-4.0F, -5.1F, 0.1F);
		this.setRotateAngle(rightHorn_lower, 0.12217304763960307F, -0.3141592653589793F, -0.03490658503988659F);
		this.rightHorn_lower.cubeList.add(new ModelBox(rightHorn_lower, 46, 36, -0.5F, -0.8F, 0.0F, 1, 2, 5, 0.025F, false));
		
		//Left horns
		
		this.leftHorn_upper1 = new ModelRenderer(this);
		this.leftHorn_upper1.setRotationPoint(3.6F, -8.0F, 0.0F);
		this.setRotateAngle(leftHorn_upper1, 0.4363323129985824F, 0.33161255787892263F, 0.19198621771937624F);
		this.leftHorn_upper1.cubeList.add(new ModelBox(leftHorn_upper1, 48, 44, -1.0F, -0.5F, 0.0F, 2, 3, 5, 0.025F, true));
		
		this.leftHorn_upper2 = new ModelRenderer(this);
		this.leftHorn_upper2.setRotationPoint(0.0F, 0.3F, 4.5F);
		this.setRotateAngle(leftHorn_upper2, -0.07504915783575616F, -0.5009094953223726F, 0.0F);
		this.leftHorn_upper2.cubeList.add(new ModelBox(leftHorn_upper2, 46, 36, -0.5F, -0.8F, 0.0F, 1, 2, 5, 0.025F, true));
		this.leftHorn_upper1.addChild(leftHorn_upper2);
		
		this.leftHorn_middle = new ModelRenderer(this);
		this.leftHorn_middle.setRotationPoint(4.0F, -3.0F, 0.7F);
		this.setRotateAngle(leftHorn_middle, -0.06981317007977318F, 0.4886921905584123F, 0.08726646259971647F);
		this.leftHorn_middle.cubeList.add(new ModelBox(leftHorn_middle, 47, 37, -0.5F, -0.8F, 0.0F, 1, 2, 4, 0.025F, true));
		
		this.leftHorn_lower = new ModelRenderer(this);
		this.leftHorn_lower.setRotationPoint(4.0F, -5.1F, -0.1F);
		this.setRotateAngle(leftHorn_lower, 0.12217304763960307F, 0.3141592653589793F, 0.03490658503988659F);
		this.leftHorn_lower.cubeList.add(new ModelBox(leftHorn_lower, 46, 36, -0.5F, -0.8F, 0.0F, 1, 2, 5, 0.025F, true));
		
		//Headwear
		
		this.headWear_expanded = new ModelRenderer(this);
		this.headWear_expanded.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.headWear_expanded.cubeList.add(new ModelBox(headWear_expanded, 32, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F + 0.025F, false));
		
		//Body
		
		this.body_expanded = new ModelRenderer(this);
		this.body_expanded.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.body_expanded.cubeList.add(new ModelBox(body_expanded, 16, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.025F, false));
		
		this.body_fire1 = new ModelRenderer(this);
		this.body_fire1.setRotationPoint(2.35F, 7.5F, -2.025F);
		this.setRotateAngle(body_fire1, 0.0F, -0.2487F, 0.0F);
		this.body_fire1.cubeList.add(new ModelBox(body_fire1, 43, 55, 0.75F, -8.5F, -2.0F, 5, 9, 0, 0.0F, false));
		
		this.body_fire2 = new ModelRenderer(this);
		this.body_fire2.setRotationPoint(-2.775F, 9.5F, -3.625F);
		this.setRotateAngle(body_fire2, 0.0F, 0.2487F, 0.0F);
		this.body_fire2.cubeList.add(new ModelBox(body_fire2, 31, 54, -3.0F, -10.0F, -1.0F, 5, 10, 0, 0.0F, false));
		
		this.body_bone1 = new ModelRenderer(this);
		this.body_bone1.setRotationPoint(0.0F, 0.9F, 0.2F);
		this.setRotateAngle(body_bone1, 1.1839F, 0.0F, 0.0F);
		this.body_bone1.cubeList.add(new ModelBox(body_bone1, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, false));
		
		this.body_bone2 = new ModelRenderer(this);
		this.body_bone2.setRotationPoint(0.0F, 3.5F, 0.6F);
		this.setRotateAngle(body_bone2, 1.1839F, 0.0F, 0.0F);
		this.body_bone2.cubeList.add(new ModelBox(body_bone2, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, false));
		
		this.body_bone3 = new ModelRenderer(this);
		this.body_bone3.setRotationPoint(0.0F, 6.4F, 0.0F);
		this.setRotateAngle(body_bone3, 1.1839F, 0.0F, 0.0F);
		this.body_bone3.cubeList.add(new ModelBox(body_bone3, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, false));
		
		//Right arm
		
		this.rightArm_expanded = new ModelRenderer(this);
		this.rightArm_expanded.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightArm_expanded.cubeList.add(new ModelBox(rightArm_expanded, 40, 16, -3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.026F, false));
		
		this.rightArm_fire1 = new ModelRenderer(this);
		this.rightArm_fire1.setRotationPoint(-2.75F, 5.0F, 1.1F);
		this.setRotateAngle(rightArm_fire1, 0.0F, 1.2567F, 0.0F);
		this.rightArm_fire1.cubeList.add(new ModelBox(rightArm_fire1, 44, 55, -0.75F, -5.0F, -1.25F, 5, 8, 0, 0.0F, false));
		
		this.rightArm_fire2 = new ModelRenderer(this);
		this.rightArm_fire2.setRotationPoint(-0.25F, 3.25F, 3.35F);
		this.setRotateAngle(rightArm_fire2, 0.0F, -0.1396F, 0.0F);
		this.rightArm_fire2.cubeList.add(new ModelBox(rightArm_fire2, 35, 57, -2.0F, -5.0F, -1.0F, 3, 5, 0, 0.0F, false));
		
		this.rightArm_bone1 = new ModelRenderer(this);
		this.rightArm_bone1.setRotationPoint(-0.5F, -1.2F, 0.0F);
		this.setRotateAngle(rightArm_bone1, -3.1416F, 0.0F, -0.1745F);
		this.rightArm_bone1.cubeList.add(new ModelBox(rightArm_bone1, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, false));
		
		this.rightArm_bone2 = new ModelRenderer(this);
		this.rightArm_bone2.setRotationPoint(-1.8F, -0.1F, 0.0F);
		this.setRotateAngle(rightArm_bone2, -3.1416F, 0.0F, -0.2618F);
		this.rightArm_bone2.cubeList.add(new ModelBox(rightArm_bone2, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, false));
		
		//Left arm
		
		this.leftArm_expanded = new ModelRenderer(this);
		this.leftArm_expanded.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftArm_expanded.cubeList.add(new ModelBox(leftArm_expanded, 40, 16, -1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.026F, true));
		
		this.leftArm_fire1 = new ModelRenderer(this);
		this.leftArm_fire1.setRotationPoint(3.0F, 5.5F, 0.1F);
		this.setRotateAngle(leftArm_fire1, 0.0F, -1.405F, 0.0F);
		this.leftArm_fire1.cubeList.add(new ModelBox(leftArm_fire1, 54, 55, -2.0F, -5.0F, -0.5F, 5, 9, 0, 0.0F, false));
		
		this.leftArm_bone1 = new ModelRenderer(this);
		this.leftArm_bone1.setRotationPoint(0.5F, -1.2F, 0.0F);
		this.setRotateAngle(leftArm_bone1, -3.1416F, 0.0F, 0.1745F);
		this.leftArm_bone1.cubeList.add(new ModelBox(leftArm_bone1, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, true));
		
		this.leftArm_bone2 = new ModelRenderer(this);
		this.leftArm_bone2.setRotationPoint(1.8F, -0.1F, 0.0F);
		this.setRotateAngle(leftArm_bone2, -3.1416F, 0.0F, 0.2618F);
		this.leftArm_bone2.cubeList.add(new ModelBox(leftArm_bone2, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, true));
		
		//Right leg
		
		this.rightLeg_expanded = new ModelRenderer(this);
		this.rightLeg_expanded.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.rightLeg_expanded.cubeList.add(new ModelBox(rightLeg_expanded, 0, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.026F, false));
		
		this.rightLeg_fire1 = new ModelRenderer(this);
		this.rightLeg_fire1.setRotationPoint(-3.625F, 10.75F, -2.625F);
		this.setRotateAngle(rightLeg_fire1, 0.0F, 0.6414F, 0.0F);
		
		this.rightLeg_fire1_r1 = new ModelRenderer(this);
		this.rightLeg_fire1_r1.setRotationPoint(3.525F, 2.25F, 4.625F);
		this.setRotateAngle(rightLeg_fire1_r1, 0.0F, -0.3927F, 0.0F);
		this.rightLeg_fire1_r1.cubeList.add(new ModelBox(rightLeg_fire1_r1, 54, 55, -4.525F, -12.25F, -2.625F, 5, 9, 0, 0.0F, false));
		this.rightLeg_fire1.addChild(rightLeg_fire1_r1);
		
		this.rightLeg_fire2 = new ModelRenderer(this);
		this.rightLeg_fire2.setRotationPoint(-1.625F, 12.75F, 1.625F);
		this.setRotateAngle(rightLeg_fire2, 0.0F, -0.2487F, 0.0F);
		this.rightLeg_fire2.cubeList.add(new ModelBox(rightLeg_fire2, 50, 54, -2.0F, -10.0F, 1.15F, 5, 10, 0, 0.0F, false));
		
		this.rightLeg_bone1 = new ModelRenderer(this);
		this.rightLeg_bone1.setRotationPoint(0.0F, 5.0F, 0.4F);
		this.setRotateAngle(rightLeg_bone1, -1.4114F, 0.0F, 0.0F);
		this.rightLeg_bone1.cubeList.add(new ModelBox(rightLeg_bone1, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, false));
		
		this.rightLeg_bone2 = new ModelRenderer(this);
		this.rightLeg_bone2.setRotationPoint(-0.7F, 3.6F, -0.4F);
		this.setRotateAngle(rightLeg_bone2, -1.4114F, 0.0F, 0.0F);
		this.rightLeg_bone2.cubeList.add(new ModelBox(rightLeg_bone2, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, false));
		
		this.rightLeg_bone3 = new ModelRenderer(this);
		this.rightLeg_bone3.setRotationPoint(-0.8F, 0.0F, -0.8F);
		this.setRotateAngle(rightLeg_bone3, -1.2217F, 1.2217F, -0.1745F);
		this.rightLeg_bone3.cubeList.add(new ModelBox(rightLeg_bone3, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, false));
		
		//Left leg
		
		this.leftLeg_expanded = new ModelRenderer(this);
		this.leftLeg_expanded.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.leftLeg_expanded.cubeList.add(new ModelBox(leftLeg_expanded, 0, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.024F, true));
		
		this.leftLeg_fire1 = new ModelRenderer(this);
		this.leftLeg_fire1.setRotationPoint(0.875F, 9.5F, 1.625F);
		this.setRotateAngle(leftLeg_fire1, 0.0F, 0.2487F, 0.0F);
		this.leftLeg_fire1.cubeList.add(new ModelBox(leftLeg_fire1, 44, 55, -3.0F, -6.5F, 1.5F, 5, 9, 0, 0.0F, true));
		
		this.leftLeg_fire2 = new ModelRenderer(this);
		this.leftLeg_fire2.setRotationPoint(0.375F, 11.5F, -1.875F);
		this.setRotateAngle(leftLeg_fire2, 0.0F, -0.2138F, 0.0F);
		this.leftLeg_fire2.cubeList.add(new ModelBox(leftLeg_fire2, 32, 54, -2.25F, -9.5F, -1.25F, 5, 10, 0, 0.0F, false));
		
		this.leftLeg_bone1 = new ModelRenderer(this);
		this.leftLeg_bone1.setRotationPoint(0.0F, 5.0F, 0.4F);
		this.setRotateAngle(leftLeg_bone1, -1.4114F, 0.0F, 0.0F);
		this.leftLeg_bone1.cubeList.add(new ModelBox(leftLeg_bone1, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, true));
		
		this.leftLeg_bone2 = new ModelRenderer(this);
		this.leftLeg_bone2.setRotationPoint(0.7F, 3.6F, -0.4F);
		this.setRotateAngle(leftLeg_bone2, -1.4114F, 0.0F, 0.0F);
		this.leftLeg_bone2.cubeList.add(new ModelBox(leftLeg_bone2, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, true));
		
		this.leftLeg_bone3 = new ModelRenderer(this);
		this.leftLeg_bone3.setRotationPoint(0.8F, 0.0F, -0.8F);
		this.setRotateAngle(leftLeg_bone3, -1.2217F, -1.2217F, 0.1745F);
		this.leftLeg_bone3.cubeList.add(new ModelBox(leftLeg_bone3, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, true));
		
		if(this.LEGS) {
			this.bipedRightLeg.addChild(rightLeg_expanded);
			this.bipedRightLeg.addChild(rightLeg_bone1);
			this.bipedRightLeg.addChild(rightLeg_bone2);
			this.bipedRightLeg.addChild(rightLeg_bone3);
			
			this.bipedLeftLeg.addChild(leftLeg_expanded);
			this.bipedLeftLeg.addChild(leftLeg_bone1);
			this.bipedLeftLeg.addChild(leftLeg_bone2);
			this.bipedLeftLeg.addChild(leftLeg_bone3);
		}
		else {
			this.bipedHead.addChild(head_expanded);
			this.bipedHead.addChild(head_jaw);
			this.bipedHead.addChild(head_snout);
			this.bipedHead.addChild(head_fire1);
			this.bipedHead.addChild(head_fire2);
			
			this.bipedHead.addChild(rightHorn_upper1);
			this.bipedHead.addChild(rightHorn_middle);
			this.bipedHead.addChild(rightHorn_lower);
			
			this.bipedHead.addChild(leftHorn_upper1);
			this.bipedHead.addChild(leftHorn_middle);
			this.bipedHead.addChild(leftHorn_lower);
			
			this.bipedHeadwear.addChild(headWear_expanded);
			
			this.bipedBody.addChild(body_expanded);
			this.bipedBody.addChild(body_fire1);
			this.bipedBody.addChild(body_fire2);
			this.bipedBody.addChild(body_bone1);
			this.bipedBody.addChild(body_bone2);
			this.bipedBody.addChild(body_bone3);
			
			this.bipedRightArm.addChild(rightArm_expanded);
			this.bipedRightArm.addChild(rightArm_fire1);
			this.bipedRightArm.addChild(rightArm_fire2);
			this.bipedRightArm.addChild(rightArm_bone1);
			this.bipedRightArm.addChild(rightArm_bone2);
			
			this.bipedLeftArm.addChild(leftArm_expanded);
			this.bipedLeftArm.addChild(leftArm_fire1);
			this.bipedLeftArm.addChild(leftArm_bone1);
			this.bipedLeftArm.addChild(leftArm_bone2);
			
			this.bipedRightLeg.addChild(rightLeg_fire1);
			this.bipedRightLeg.addChild(rightLeg_fire2);
			
			this.bipedLeftLeg.addChild(leftLeg_fire1);
			this.bipedLeftLeg.addChild(leftLeg_fire2);
		}
	}
	
	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		
		if(!(entityIn instanceof EntityLivingBase) || !((IEntityLivingBaseRenderContext)entityIn).iceAndFire$getGlintContext()) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(this.getFrameForAge(ageInTicks));
			
			GlStateManager.pushMatrix();
			GlStateManager.enableBlend();
			
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			
			super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
			
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}
	
	private ResourceLocation getFrameForAge(float ageInTicks) {
		ResourceLocation frameTexture;
		int frameInt = ((int)ageInTicks)%16;
		if(frameInt < 2) {
			frameTexture = this.LEGS ? BLOOD_FRAME_LEGS_1 : BLOOD_FRAME_1;
		}
		else if(frameInt < 4) {
			frameTexture = this.LEGS ? BLOOD_FRAME_LEGS_2 : BLOOD_FRAME_2;
		}
		else if(frameInt < 6) {
			frameTexture = this.LEGS ? BLOOD_FRAME_LEGS_3 : BLOOD_FRAME_3;
		}
		else if(frameInt < 8) {
			frameTexture = this.LEGS ? BLOOD_FRAME_LEGS_4 : BLOOD_FRAME_4;
		}
		else if(frameInt < 10) {
			frameTexture = this.LEGS ? BLOOD_FRAME_LEGS_5 : BLOOD_FRAME_5;
		}
		else if(frameInt < 12) {
			frameTexture = this.LEGS ? BLOOD_FRAME_LEGS_6 : BLOOD_FRAME_6;
		}
		else if(frameInt < 14) {
			frameTexture = this.LEGS ? BLOOD_FRAME_LEGS_7 : BLOOD_FRAME_7;
		}
		else {
			frameTexture = this.LEGS ? BLOOD_FRAME_LEGS_8 : BLOOD_FRAME_8;
		}
		return frameTexture;
	}
}