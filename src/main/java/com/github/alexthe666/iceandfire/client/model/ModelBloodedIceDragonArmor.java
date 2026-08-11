package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.client.model.util.IEntityLivingBaseRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class ModelBloodedIceDragonArmor extends ModelIceDragonArmor {
	
	private static final ResourceLocation BLOOD_FRAME_1 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor1.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_1 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor_legs1.png");
	private static final ResourceLocation BLOOD_FRAME_2 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor2.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_2 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor_legs2.png");
	private static final ResourceLocation BLOOD_FRAME_3 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor3.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_3 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor_legs3.png");
	private static final ResourceLocation BLOOD_FRAME_4 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor4.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_4 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor_legs4.png");
	private static final ResourceLocation BLOOD_FRAME_5 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor5.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_5 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor_legs5.png");
	private static final ResourceLocation BLOOD_FRAME_6 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor6.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_6 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor_legs6.png");
	private static final ResourceLocation BLOOD_FRAME_7 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor7.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_7 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor_legs7.png");
	private static final ResourceLocation BLOOD_FRAME_8 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor8.png");
	private static final ResourceLocation BLOOD_FRAME_LEGS_8 = new ResourceLocation("iceandfire:textures/models/armor/" + "iced" + "_armor_legs8.png");
	
	public ModelRenderer head_expanded;
	public ModelRenderer head_jaw;
	public ModelRenderer head_snout;
	public ModelRenderer head_rightTeeth;
	public ModelRenderer head_leftTeeth;
	public ModelRenderer head_fire1;
	public ModelRenderer head_fire2;
	public ModelRenderer head_fire3;
	public ModelRenderer head_fire4;
	
	public ModelRenderer rightHorn_upper1;
	public ModelRenderer rightHorn_upper2;
	public ModelRenderer rightHorn_upperMiddle;
	public ModelRenderer rightHorn_lower;
	
	public ModelRenderer leftHorn_upper1;
	public ModelRenderer leftHorn_upper2;
	public ModelRenderer leftHorn_upperMiddle;
	public ModelRenderer leftHorn_lower;
	
	public ModelRenderer headWear_expanded;
	
	public ModelRenderer body_expanded;
	public ModelRenderer body_bone1;
	public ModelRenderer body_bone2;
	public ModelRenderer body_bone3;
	public ModelRenderer body_fire1;
	public ModelRenderer body_fire1_r1;
	public ModelRenderer body_fire1_r2;
	public ModelRenderer body_fire2;
	
	public ModelRenderer rightArm_expanded;
	public ModelRenderer rightArm_bone1;
	public ModelRenderer rightArm_bone2;
	public ModelRenderer rightArm_fire1;
	public ModelRenderer rightArm_fire2;
	public ModelRenderer rightArm_fire3;
	
	public ModelRenderer leftArm_expanded;
	public ModelRenderer leftArm_bone1;
	public ModelRenderer leftArm_bone2;
	public ModelRenderer leftArm_fire1;
	public ModelRenderer leftArm_fire2;
	
	public ModelRenderer rightLeg_expanded;
	public ModelRenderer rightLeg_bone1;
	public ModelRenderer rightLeg_bone2;
	public ModelRenderer rightLeg_bone3;
	public ModelRenderer rightLeg_fire1;
	public ModelRenderer rightLeg_fire2;
	public ModelRenderer rightLeg_fire2_r1;
	public ModelRenderer rightLeg_fire3;
	
	public ModelRenderer leftLeg_expanded;
	public ModelRenderer leftLeg_bone1;
	public ModelRenderer leftLeg_bone2;
	public ModelRenderer leftLeg_bone3;
	public ModelRenderer leftLeg_fire1;
	public ModelRenderer leftLeg_fire2;
	public ModelRenderer leftLeg_fire3;
	public ModelRenderer leftLeg_fire4;
	public ModelRenderer leftLeg_fire4_r1;
	
	private final boolean LEGS;
	
	public ModelBloodedIceDragonArmor(float modelSize, boolean legs) {
		super(modelSize, legs);
		this.LEGS = legs;
		
		//Head
		
		this.head_expanded = new ModelRenderer(this);
		this.head_expanded.cubeList.add(new ModelBox(this.head_expanded, 0, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.025F, false));
		
		this.head_jaw = new ModelRenderer(this);
		this.head_jaw.setRotationPoint(0.0F, -5.4F, 0.0F);
		this.setRotateAngle(head_jaw, -0.091106186954104F, -0.0F, 0.0F);
		this.head_jaw.cubeList.add(new ModelBox(head_jaw, 6, 51, -3.5F, 4.0F, -7.4F, 7, 2, 5, 0.025F, false));
		
		this.head_snout = new ModelRenderer(this);
		this.head_snout.setRotationPoint(0.0F, -5.6F, 0.0F);
		this.setRotateAngle(head_snout, 0.045553093477052F, -0.0F, 0.0F);
		this.head_snout.cubeList.add(new ModelBox(head_snout, 6, 44, -3.5F, -2.8F, -8.8F, 7, 2, 5, 0.025F, false));
		
		this.head_rightTeeth = new ModelRenderer(this);
		this.head_rightTeeth.setRotationPoint(0.0F, 3.0F, 1.4F);
		this.head_rightTeeth.cubeList.add(new ModelBox(head_rightTeeth, 6, 34, -3.6F, 0.1F, -8.9F, 4, 1, 5, 0.025F, false));
		this.head_jaw.addChild(head_rightTeeth);
		
		this.head_leftTeeth = new ModelRenderer(this);
		this.head_leftTeeth.setRotationPoint(0.0F, 3.0F, 1.4F);
		this.head_leftTeeth.cubeList.add(new ModelBox(head_leftTeeth, 6, 34, -0.4F, 0.1F, -8.9F, 4, 1, 5, 0.025F, true));
		this.head_jaw.addChild(head_leftTeeth);
		
		this.head_fire1 = new ModelRenderer(this);
		this.head_fire1.setRotationPoint(-0.525F, 1.65F, 4.15F);
		this.setRotateAngle(head_fire1, 0.0F, 1.4704F, 0.0F);
		this.head_fire1.cubeList.add(new ModelBox(head_fire1, 48, 56, -2.0F, -5.0F, 3.0F, 1, 6, 0, 0.0F, true));
		this.head_fire1.cubeList.add(new ModelBox(head_fire1, 60, 56, -2.0F, -10.0F, -1.0F, 1, 6, 0, 0.0F, true));
		
		this.head_fire2 = new ModelRenderer(this);
		this.head_fire2.setRotationPoint(7.1F, -0.1F, 2.75F);
		this.setRotateAngle(head_fire2, 0.0F, -0.7854F, 0.0F);
		this.head_fire2.cubeList.add(new ModelBox(head_fire2, 32, 56, -1.0F, -6.0F, -1.0F, 1, 6, 0, 0.0F, false));
		
		this.head_fire3 = new ModelRenderer(this);
		this.head_fire3.setRotationPoint(-4.775F, -3.35F, -2.85F);
		this.setRotateAngle(head_fire3, 0.0F, 1.0777F, 0.0F);
		this.head_fire3.cubeList.add(new ModelBox(head_fire3, 44, 56, -0.75F, -3.0F, -0.5F, 1, 6, 0, 0.0F, true));
		this.head_fire3.cubeList.add(new ModelBox(head_fire3, 56, 56, -4.25F, -5.0F, 1.75F, 1, 6, 0, 0.0F, true));
		
		this.head_fire4 = new ModelRenderer(this);
		this.head_fire4.setRotationPoint(4.1F, -3.1F, -5.25F);
		this.setRotateAngle(head_fire4, 0.0F, -0.7854F, 0.0F);
		this.head_fire4.cubeList.add(new ModelBox(head_fire4, 52, 56, 2.75F, -4.0F, 0.5F, 1, 5, 0, 0.0F, false));
		
		//Right horns
		
		this.rightHorn_upper1 = new ModelRenderer(this);
		this.rightHorn_upper1.setRotationPoint(-3.6F, -8.0F, 1.0F);
		this.setRotateAngle(rightHorn_upper1, 0.3141592653589793F, -0.33161255787892263F, -0.19198621771937624F);
		this.rightHorn_upper1.cubeList.add(new ModelBox(rightHorn_upper1, 48, 44, -1.0F, -0.5F, 0.0F, 2, 3, 5, 0.025F, false));
		
		this.rightHorn_upper2 = new ModelRenderer(this);
		this.rightHorn_upper2.setRotationPoint(0.0F, 0.3F, 4.5F);
		this.setRotateAngle(rightHorn_upper2, -0.07504915783575616F, 0.0F, 0.0F);
		this.rightHorn_upper2.cubeList.add(new ModelBox(rightHorn_upper2, 46, 36, -0.5F, -0.8F, -0.0F, 1, 2, 5, 0.025F, false));
		this.rightHorn_upper1.addChild(rightHorn_upper2);
		
		this.rightHorn_upperMiddle = new ModelRenderer(this);
		this.rightHorn_upperMiddle.setRotationPoint(-1.0F, -7.5F, 1.9F);
		this.setRotateAngle(rightHorn_upperMiddle, 0.45378560551852565F, -0.3141592653589793F, -0.03490658503988659F);
		this.rightHorn_upperMiddle.cubeList.add(new ModelBox(rightHorn_upperMiddle, 46, 36, -0.5F, -0.8F, -0.0F, 1, 2, 5, 0.025F, false));
		
		this.rightHorn_lower = new ModelRenderer(this);
		this.rightHorn_lower.setRotationPoint(-4.0F, -4.0F, 0.7F);
		this.setRotateAngle(rightHorn_lower, -0.06981317007977318F, -0.4886921905584123F, -0.08726646259971647F);
		this.rightHorn_lower.cubeList.add(new ModelBox(rightHorn_lower, 46, 36, -0.5F, -0.8F, -0.0F, 1, 2, 5, 0.025F, false));
		
		//Left horns
		
		this.leftHorn_upper1 = new ModelRenderer(this);
		this.leftHorn_upper1.setRotationPoint(3.6F, -8.0F, 1.0F);
		this.setRotateAngle(leftHorn_upper1, 0.3141592653589793F, 0.33161255787892263F, 0.19198621771937624F);
		this.leftHorn_upper1.cubeList.add(new ModelBox(leftHorn_upper1, 48, 44, -1.0F, -0.5F, 0.0F, 2, 3, 5, 0.025F, true));
		
		this.leftHorn_upper2 = new ModelRenderer(this);
		this.leftHorn_upper2.setRotationPoint(0.0F, 0.3F, 4.5F);
		this.setRotateAngle(leftHorn_upper2, -0.07504915783575616F, 0.0F, 0.0F);
		this.leftHorn_upper2.cubeList.add(new ModelBox(leftHorn_upper2, 46, 36, -0.5F, -0.8F, -0.0F, 1, 2, 5, 0.025F, true));
		this.leftHorn_upper1.addChild(leftHorn_upper2);
		
		this.leftHorn_upperMiddle = new ModelRenderer(this);
		this.leftHorn_upperMiddle.setRotationPoint(1.0F, -7.5F, 1.9F);
		this.setRotateAngle(leftHorn_upperMiddle, 0.45378560551852565F, 0.3141592653589793F, 0.03490658503988659F);
		this.leftHorn_upperMiddle.cubeList.add(new ModelBox(leftHorn_upperMiddle, 46, 36, -0.5F, -0.8F, -0.0F, 1, 2, 5, 0.025F, true));
		
		this.leftHorn_lower = new ModelRenderer(this);
		this.leftHorn_lower.setRotationPoint(4.0F, -4.0F, 0.7F);
		this.setRotateAngle(leftHorn_lower, -0.06981317007977318F, 0.4886921905584123F, 0.08726646259971647F);
		this.leftHorn_lower.cubeList.add(new ModelBox(leftHorn_lower, 46, 36, -0.5F, -0.8F, -0.0F, 1, 2, 5, 0.025F, true));
		
		//Headwear
		
		this.headWear_expanded = new ModelRenderer(this);
		this.headWear_expanded.cubeList.add(new ModelBox(this.headWear_expanded, 32, 0, -4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F + 0.025F, false));
		
		//Body
		
		this.body_expanded = new ModelRenderer(this);
		this.body_expanded.cubeList.add(new ModelBox(this.body_expanded, 16, 16, -4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize + 0.025F, false));
		
		this.body_bone1 = new ModelRenderer(this);
		this.body_bone1.setRotationPoint(0.0F, 0.9F, 0.2F);
		this.setRotateAngle(body_bone1, 1.1838568316277536F, 0.0F, 0.0F);
		this.body_bone1.cubeList.add(new ModelBox(body_bone1, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, false));
		
		this.body_bone2 = new ModelRenderer(this);
		this.body_bone2.setRotationPoint(0.0F, 3.5F, 0.6F);
		this.setRotateAngle(body_bone2, 1.1838568316277536F, 0.0F, 0.0F);
		this.body_bone2.cubeList.add(new ModelBox(body_bone2, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, false));
		
		this.body_bone3 = new ModelRenderer(this);
		this.body_bone3.setRotationPoint(0.0F, 6.4F, 0.0F);
		this.setRotateAngle(body_bone3, 1.1838568316277536F, 0.0F, 0.0F);
		this.body_bone3.cubeList.add(new ModelBox(body_bone3, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, false));
		
		this.body_fire1 = new ModelRenderer(this);
		this.body_fire1.setRotationPoint(3.35F, 6.5F, -2.025F);
		this.setRotateAngle(body_fire1, 0.0F, -0.2487F, 0.0F);
		this.body_fire1.cubeList.add(new ModelBox(body_fire1, 60, 56, 1.0F, -7.0F, -1.0F, 1, 6, 0, 0.0F, false));
		
		this.body_fire1_r1 = new ModelRenderer(this);
		this.body_fire1_r1.setRotationPoint(-5.0F, 2.5F, 0.25F);
		this.setRotateAngle(body_fire1_r1, 0.0F, 0.4363F, 0.0F);
		this.body_fire1_r1.cubeList.add(new ModelBox(body_fire1_r1, 44, 56, -1.5F, -3.5F, 0.0F, 1, 6, 0, 0.0F, false));
		this.body_fire1.addChild(body_fire1_r1);
		
		this.body_fire1_r2 = new ModelRenderer(this);
		this.body_fire1_r2.setRotationPoint(0.0F, -0.25F, -1.0F);
		this.setRotateAngle(body_fire1_r2, 0.0F, 3.0543F, 0.0F);
		this.body_fire1_r2.cubeList.add(new ModelBox(body_fire1_r2, 40, 56, -0.5F, -2.5F, 0.0F, 1, 5, 0, 0.0F, true));
		this.body_fire1.addChild(body_fire1_r2);
		
		this.body_fire2 = new ModelRenderer(this);
		this.body_fire2.setRotationPoint(-2.775F, 4.5F, -1.625F);
		this.setRotateAngle(body_fire2, 0.0F, 0.2487F, 0.0F);
		this.body_fire2.cubeList.add(new ModelBox(body_fire2, 32, 56, -1.5F, -5.0F, -2.5F, 1, 5, 0, 0.0F, true));
		
		//Right arm
		
		this.rightArm_expanded = new ModelRenderer(this);
		this.rightArm_expanded.cubeList.add(new ModelBox(rightArm_expanded, 40, 16, -3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.026F, false));
		
		this.rightArm_bone1 = new ModelRenderer(this);
		this.rightArm_bone1.setRotationPoint(-0.5F, -1.2F, 0.0F);
		this.setRotateAngle(rightArm_bone1, -3.141592653589793F, 0.0F, -0.17453292519943295F);
		this.rightArm_bone1.cubeList.add(new ModelBox(rightArm_bone1, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, false));
		
		this.rightArm_bone2 = new ModelRenderer(this);
		this.rightArm_bone2.setRotationPoint(-1.8F, -0.1F, 0.0F);
		this.setRotateAngle(rightArm_bone2, -3.141592653589793F, 0.0F, -0.2617993877991494F);
		this.rightArm_bone2.cubeList.add(new ModelBox(rightArm_bone2, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, false));
		
		this.rightArm_fire1 = new ModelRenderer(this);
		this.rightArm_fire1.setRotationPoint(-0.75F, 7.75F, -1.9F);
		this.setRotateAngle(rightArm_fire1, 0.0F, 0.2531F, 0.0F);
		this.rightArm_fire1.cubeList.add(new ModelBox(rightArm_fire1, 32, 56, -0.75F, -5.0F, -0.75F, 1, 5, 0, 0.0F, false));
		
		this.rightArm_fire2 = new ModelRenderer(this);
		this.rightArm_fire2.setRotationPoint(-0.25F, 3.25F, 3.35F);
		this.setRotateAngle(rightArm_fire2, 0.0F, -0.1396F, 0.0F);
		this.rightArm_fire2.cubeList.add(new ModelBox(rightArm_fire2, 52, 56, 0.0F, -5.0F, -1.0F, 1, 5, 0, 0.0F, false));
		
		this.rightArm_fire3 = new ModelRenderer(this);
		this.rightArm_fire3.setRotationPoint(-2.75F, 1.75F, -0.4F);
		this.setRotateAngle(rightArm_fire3, 0.0F, 1.3003F, 0.0F);
		this.rightArm_fire3.cubeList.add(new ModelBox(rightArm_fire3, 36, 56, -1.75F, -4.0F, -0.75F, 1, 6, 0, 0.0F, false));
		
		//Left arm
		
		this.leftArm_expanded = new ModelRenderer(this);
		this.leftArm_expanded.cubeList.add(new ModelBox(leftArm_expanded, 40, 16, -1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize + 0.026F, true));
		
		this.leftArm_bone1 = new ModelRenderer(this);
		this.leftArm_bone1.setRotationPoint(0.5F, -1.2F, 0.0F);
		this.setRotateAngle(leftArm_bone1, -3.141592653589793F, 0.0F, 0.17453292519943295F);
		this.leftArm_bone1.cubeList.add(new ModelBox(leftArm_bone1, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, true));
		
		this.leftArm_bone2 = new ModelRenderer(this);
		this.leftArm_bone2.setRotationPoint(1.8F, -0.1F, 0.0F);
		this.setRotateAngle(leftArm_bone2, -3.141592653589793F, 0.0F, 0.2617993877991494F);
		this.leftArm_bone2.cubeList.add(new ModelBox(leftArm_bone2, 36, 38, -0.5F, 0.0F, -0.5F, 1, 3, 1, 0.025F, true));
		
		this.leftArm_fire1 = new ModelRenderer(this);
		this.leftArm_fire1.setRotationPoint(1.5F, 1.5F, -1.9F);
		this.setRotateAngle(leftArm_fire1, 0.0F, -0.1396F, 0.0F);
		this.leftArm_fire1.cubeList.add(new ModelBox(leftArm_fire1, 35, 57, -2.0F, -5.0F, -1.0F, 3, 5, 0, 0.0F, true));
		
		this.leftArm_fire2 = new ModelRenderer(this);
		this.leftArm_fire2.setRotationPoint(2.75F, 1.75F, -0.4F);
		this.setRotateAngle(leftArm_fire2, 0.0F, -1.3003F, 0.0F);
		this.leftArm_fire2.cubeList.add(new ModelBox(leftArm_fire2, 44, 56, -1.25F, -5.0F, -1.25F, 1, 6, 0, 0.0F, true));
		this.leftArm_fire2.cubeList.add(new ModelBox(leftArm_fire2, 32, 56, 1.0F, -2.25F, -0.5F, 1, 6, 0, 0.0F, true));
		
		//Right leg
		
		this.rightLeg_expanded = new ModelRenderer(this);
		this.rightLeg_expanded.cubeList.add(new ModelBox(rightLeg_expanded, 0, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.026F, false));
		
		this.rightLeg_bone1 = new ModelRenderer(this);
		this.rightLeg_bone1.setRotationPoint(0.0F, 5.0F, 0.4F);
		this.setRotateAngle(rightLeg_bone1, -1.4114477660878142F, 0.0F, 0.0F);
		this.rightLeg_bone1.cubeList.add(new ModelBox(rightLeg_bone1, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, false));
		
		this.rightLeg_bone2 = new ModelRenderer(this);
		this.rightLeg_bone2.setRotationPoint(-0.7F, 3.6F, -0.4F);
		this.setRotateAngle(rightLeg_bone2, -1.4114477660878142F, 0.0F, 0.0F);
		this.rightLeg_bone2.cubeList.add(new ModelBox(rightLeg_bone2, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, false));
		
		this.rightLeg_bone3 = new ModelRenderer(this);
		this.rightLeg_bone3.setRotationPoint(-0.8F, 0.0F, -0.8F);
		this.setRotateAngle(rightLeg_bone3, -1.2217304763960306F, 1.2217304763960306F, -0.17453292519943295F);
		this.rightLeg_bone3.cubeList.add(new ModelBox(rightLeg_bone3, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, false));
		
		this.rightLeg_fire1 = new ModelRenderer(this);
		this.rightLeg_fire1.setRotationPoint(-1.625F, 8.75F, -1.625F);
		this.setRotateAngle(rightLeg_fire1, 0.0F, 0.2487F, 0.0F);
		this.rightLeg_fire1.cubeList.add(new ModelBox(rightLeg_fire1, 60, 56, -0.25F, -4.75F, -1.0F, 1, 6, 0, 0.0F, false));
		
		this.rightLeg_fire2 = new ModelRenderer(this);
		this.rightLeg_fire2.setRotationPoint(-1.625F, 12.75F, 1.625F);
		this.setRotateAngle(rightLeg_fire2, 0.0F, -0.2487F, 0.0F);
		this.rightLeg_fire2.cubeList.add(new ModelBox(rightLeg_fire2, 44, 56, 0.0F, -6.0F, 1.0F, 1, 6, 0, 0.0F, false));
		
		this.rightLeg_fire2_r1 = new ModelRenderer(this);
		this.rightLeg_fire2_r1.setRotationPoint(-1.8473F, -5.5F, -1.5711F);
		this.setRotateAngle(rightLeg_fire2_r1, 0.0F, -1.6144F, 0.0F);
		this.rightLeg_fire2_r1.cubeList.add(new ModelBox(rightLeg_fire2_r1, 52, 56, -1.0F, -2.5F, 0.0F, 1, 5, 0, 0.0F, false));
		this.rightLeg_fire2.addChild(rightLeg_fire2_r1);
		
		this.rightLeg_fire3 = new ModelRenderer(this);
		this.rightLeg_fire3.setRotationPoint(-2.625F, 2.75F, -1.625F);
		this.setRotateAngle(rightLeg_fire3, 0.0F, 0.2487F, 0.0F);
		this.rightLeg_fire3.cubeList.add(new ModelBox(rightLeg_fire3, 35, 57, -1.0F, -5.0F, -1.0F, 2, 5, 0, 0.0F, false));
		
		//Left leg
		
		this.leftLeg_expanded = new ModelRenderer(this);
		this.leftLeg_expanded.cubeList.add(new ModelBox(leftLeg_expanded, 0, 16, -2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize + 0.024F, true));
		
		this.leftLeg_bone1 = new ModelRenderer(this);
		this.leftLeg_bone1.setRotationPoint(0.0F, 5.0F, 0.4F);
		this.setRotateAngle(leftLeg_bone1, -1.4114477660878142F, 0.0F, 0.0F);
		this.leftLeg_bone1.cubeList.add(new ModelBox(leftLeg_bone1, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, true));
		
		this.leftLeg_bone2 = new ModelRenderer(this);
		this.leftLeg_bone2.setRotationPoint(0.7F, 3.6F, -0.4F);
		this.setRotateAngle(leftLeg_bone2, -1.4114477660878142F, 0.0F, 0.0F);
		this.leftLeg_bone2.cubeList.add(new ModelBox(leftLeg_bone2, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, true));
		
		this.leftLeg_bone3 = new ModelRenderer(this);
		this.leftLeg_bone3.setRotationPoint(0.8F, -0.0F, -0.8F);
		this.setRotateAngle(leftLeg_bone3, -1.2217304763960306F, -1.2217304763960306F, 0.17453292519943295F);
		this.leftLeg_bone3.cubeList.add(new ModelBox(leftLeg_bone3, 0, 34, -0.5F, 0.0F, 0.0F, 1, 3, 1, 0.025F, true));
		
		this.leftLeg_fire1 = new ModelRenderer(this);
		this.leftLeg_fire1.setRotationPoint(0.875F, 9.5F, 1.625F);
		this.setRotateAngle(leftLeg_fire1, 0.0F, 0.2487F, 0.0F);
		this.leftLeg_fire1.cubeList.add(new ModelBox(leftLeg_fire1, 48, 56, 0.0F, -5.0F, 1.0F, 1, 6, 0, 0.0F, true));
		
		this.leftLeg_fire2 = new ModelRenderer(this);
		this.leftLeg_fire2.setRotationPoint(0.875F, 11.5F, -1.625F);
		this.setRotateAngle(leftLeg_fire2, 0.0F, -0.2487F, 0.0F);
		this.leftLeg_fire2.cubeList.add(new ModelBox(leftLeg_fire2, 48, 56, 1.0F, -5.0F, -1.0F, 1, 5, 0, 0.0F, true));
		
		this.leftLeg_fire3 = new ModelRenderer(this);
		this.leftLeg_fire3.setRotationPoint(0.575F, 2.75F, -1.625F);
		this.setRotateAngle(leftLeg_fire3, 0.0F, -0.144F, 0.0F);
		this.leftLeg_fire3.cubeList.add(new ModelBox(leftLeg_fire3, 56, 56, 0.0F, -5.0F, -1.0F, 1, 6, 0, 0.0F, false));
		
		this.leftLeg_fire4 = new ModelRenderer(this);
		this.leftLeg_fire4.setRotationPoint(3.4723F, 7.25F, 0.0539F);
		
		this.leftLeg_fire4_r1 = new ModelRenderer(this);
		this.leftLeg_fire4_r1.setRotationPoint(-0.6F, 2.0F, -0.5F);
		this.setRotateAngle(leftLeg_fire4_r1, 0.0F, 2.0071F, 0.0F);
		this.leftLeg_fire4_r1.cubeList.add(new ModelBox(leftLeg_fire4_r1, 36, 56, 0.0F, -3.5F, 0.0F, 1, 6, 0, 0.0F, true));
		this.leftLeg_fire4.addChild(leftLeg_fire4_r1);
		
		if(this.LEGS) {
			this.bipedRightLeg.addChild(rightLeg_expanded);
			this.bipedRightLeg.addChild(rightLeg_bone1);
			this.bipedRightLeg.addChild(rightLeg_bone2);
			this.bipedRightLeg.addChild(rightLeg_bone3);
			
			this.bipedLeftLeg.addChild(leftLeg_expanded);
			this.bipedLeftLeg.addChild(leftLeg_bone1);
			this.bipedLeftLeg.addChild(leftLeg_bone2);
			this.bipedLeftLeg.addChild(leftLeg_bone3);
			
			this.bipedRightLeg.addChild(rightLeg_fire3);
			
			this.bipedLeftLeg.addChild(leftLeg_fire3);
		}
		else {
			this.bipedHead.addChild(head_expanded);
			this.bipedHead.addChild(head_jaw);
			this.bipedHead.addChild(head_snout);
			this.bipedHead.addChild(head_fire1);
			this.bipedHead.addChild(head_fire2);
			this.bipedHead.addChild(head_fire3);
			this.bipedHead.addChild(head_fire4);
			
			this.bipedHead.addChild(rightHorn_upper1);
			this.bipedHead.addChild(rightHorn_upperMiddle);
			this.bipedHead.addChild(rightHorn_lower);
			
			this.bipedHead.addChild(leftHorn_upper1);
			this.bipedHead.addChild(leftHorn_upperMiddle);
			this.bipedHead.addChild(leftHorn_lower);
			
			this.bipedHeadwear.addChild(headWear_expanded);
			
			this.bipedBody.addChild(body_expanded);
			this.bipedBody.addChild(body_bone1);
			this.bipedBody.addChild(body_bone2);
			this.bipedBody.addChild(body_bone3);
			this.bipedBody.addChild(body_fire1);
			this.bipedBody.addChild(body_fire2);
			
			this.bipedRightArm.addChild(rightArm_expanded);
			this.bipedRightArm.addChild(rightArm_bone1);
			this.bipedRightArm.addChild(rightArm_bone2);
			this.bipedRightArm.addChild(rightArm_fire1);
			this.bipedRightArm.addChild(rightArm_fire2);
			this.bipedRightArm.addChild(rightArm_fire3);
			
			this.bipedLeftArm.addChild(leftArm_expanded);
			this.bipedLeftArm.addChild(leftArm_bone1);
			this.bipedLeftArm.addChild(leftArm_bone2);
			this.bipedLeftArm.addChild(leftArm_fire1);
			this.bipedLeftArm.addChild(leftArm_fire2);
			
			this.bipedRightLeg.addChild(rightLeg_fire1);
			this.bipedRightLeg.addChild(rightLeg_fire2);
			
			this.bipedLeftLeg.addChild(leftLeg_fire1);
			this.bipedLeftLeg.addChild(leftLeg_fire2);
			this.bipedLeftLeg.addChild(leftLeg_fire4);
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