package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.projectile.EntityDragonArrow;

public class ItemDragonArrow extends ItemGeneric {
    private final EntityDragonArrow.Type type;

    public ItemDragonArrow(String gameName, String name, EntityDragonArrow.Type type) {
        super(gameName, name);
        this.type = type;
    }

    public EntityDragonArrow.Type getType() {
        return this.type;
    }
}
