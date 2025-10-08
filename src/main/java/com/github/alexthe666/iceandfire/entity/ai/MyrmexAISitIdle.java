package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityMyrmexBase;
import net.minecraft.entity.ai.EntityAIBase;

public class MyrmexAISitIdle extends EntityAIBase {
    private final EntityMyrmexBase myrmex;
    private double lookX;
    private double lookZ;
    private int idleTime;

    public MyrmexAISitIdle(EntityMyrmexBase entityIn) {
        this.myrmex = entityIn;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        return this.myrmex.getRNG().nextFloat() < 0.5F && !this.myrmex.shouldLeaveHive() && this.myrmex.isOnResin() && !this.myrmex.canSeeSky();
    }

    public void startExecuting() {
        double d0 = (Math.PI * 2D) * this.myrmex.getRNG().nextDouble();
        this.lookX = Math.cos(d0);
        this.lookZ = Math.sin(d0);
        this.idleTime = 200 + this.myrmex.getRNG().nextInt(200);
    }

    public boolean shouldContinueExecuting() {
        return this.idleTime >= 0 && this.myrmex.getAttackTarget() == null;
    }

    public void updateTask() {
        --this.idleTime;
        this.myrmex.getLookHelper().setLookPosition(this.myrmex.posX + this.lookX, this.myrmex.posY + (double)this.myrmex.getEyeHeight(), this.myrmex.posZ + this.lookZ, (float)this.myrmex.getHorizontalFaceSpeed(), (float)this.myrmex.getVerticalFaceSpeed());
    }
}