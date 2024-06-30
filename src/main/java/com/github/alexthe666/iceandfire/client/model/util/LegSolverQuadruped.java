package com.github.alexthe666.iceandfire.client.model.util;

/*
  Code from JurassiCraft, used with permission
  By paul101
 */
public final class LegSolverQuadruped extends LegSolver {
	public final Leg backLeft, backRight, frontLeft, frontRight;

	public LegSolverQuadruped(float forwardCenter, float forward, float sideBack, float sideFront, float range) {
		super(
				new Leg(forwardCenter - forward, sideBack, range),
				new Leg(forwardCenter - forward, -sideBack, range),
				new Leg(forwardCenter + forward, sideFront, range),
				new Leg(forwardCenter + forward, -sideFront, range)
		);
		this.backLeft = this.legs[0];
		this.backRight = this.legs[1];
		this.frontLeft = this.legs[2];
		this.frontRight = this.legs[3];
	}
}
