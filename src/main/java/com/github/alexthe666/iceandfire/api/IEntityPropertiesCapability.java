package com.github.alexthe666.iceandfire.api;

public interface IEntityPropertiesCapability {
    boolean isDeathwormLaunched();
    void setDeathwormLaunched(boolean deathwormLaunched);
    boolean isDeathwormReceded();
    void setDeathwormReceded(boolean deathwormReceded);
    int getDeathwormLungeTicks();
    void setDeathwormLungeTicks(int deathwormLungeTicks);
    int getPreviousDeathwormLungeTicks();
    void setPreviousDeathwormLungeTicks(int previousDeathwormLungeTicks);
    boolean isDirty();
    void markClean();
}