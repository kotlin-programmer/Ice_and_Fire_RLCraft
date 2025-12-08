package com.github.alexthe666.iceandfire.capability.entityproperties;

import com.github.alexthe666.iceandfire.api.IEntityPropertiesCapability;

public class EntityPropertiesCapability implements IEntityPropertiesCapability {
    private final Data data = new Data();
    private final Data previousData = new Data();

    @Override
    public boolean isDeathwormLaunched() {
        return this.data.isDeathwormLaunched;
    }

    @Override
    public void setDeathwormLaunched(boolean deathwormLaunched) {
        this.data.isDeathwormLaunched = deathwormLaunched;
    }

    @Override
    public boolean isDeathwormReceded() {
        return this.data.isDeathwormReceded;
    }

    @Override
    public void setDeathwormReceded(boolean deathwormReceded) {
        this.data.isDeathwormReceded = deathwormReceded;
    }

    @Override
    public int getDeathwormLungeTicks() {
        return this.data.deathwormLungeTicks;
    }

    @Override
    public void setDeathwormLungeTicks(int deathwormLungeTicks) {
        this.data.deathwormLungeTicks = deathwormLungeTicks;
    }

    @Override
    public int getPreviousDeathwormLungeTicks() {
        return this.data.previousDeathwormLungeTicks;
    }

    public void setPreviousDeathwormLungeTicks(int previousDeathwormLungeTicks) {
        this.data.previousDeathwormLungeTicks = previousDeathwormLungeTicks;
    }

    @Override
    public boolean isDirty() {
        return !this.data.equals(this.previousData);
    }

    @Override
    public void markClean() {
        this.previousData.isDeathwormLaunched = this.data.isDeathwormLaunched;
        this.previousData.isDeathwormReceded = this.data.isDeathwormReceded;
        this.previousData.deathwormLungeTicks = this.data.deathwormLungeTicks;
        this.previousData.previousDeathwormLungeTicks = this.data.previousDeathwormLungeTicks;
    }

    private static class Data {
        private boolean isDeathwormLaunched = false;
        private boolean isDeathwormReceded = false;
        private int deathwormLungeTicks = 0;
        private int previousDeathwormLungeTicks = 0;

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj.getClass() != this.getClass()) {
                return false;
            }

            final Data other = (Data) obj;
            return this.isDeathwormLaunched == other.isDeathwormLaunched
                    && this.isDeathwormReceded == other.isDeathwormReceded
                    && this.deathwormLungeTicks == other.deathwormLungeTicks
                    && this.previousDeathwormLungeTicks == other.previousDeathwormLungeTicks;
        }
    }
}
