package com.github.alexthe666.iceandfire.entity.util;

import com.github.alexthe666.iceandfire.structures.WorldGenMyrmexHive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyrmexRoom {
    @Nullable private final MyrmexRoom parent;
    public final BlockPos center;
    private MyrmexHive hive;
    private final List<MyrmexRoom> connectedRooms = new ArrayList<>();
    private final WorldGenMyrmexHive.RoomType type;
    private boolean hasEntrance = false;

    public MyrmexRoom(MyrmexRoom parent, WorldGenMyrmexHive.RoomType type, BlockPos center, MyrmexHive hive) {
        this.parent = parent;
        this.type = type;
        this.center = center;
        this.hive = hive;
    }

    //from NBT
    public MyrmexRoom(MyrmexRoom parent, NBTTagCompound nbt, MyrmexHive hive){
        this(
                parent,
                WorldGenMyrmexHive.RoomType.valueOf(nbt.getString("type")),
                new BlockPos(nbt.getInteger("centerX"), nbt.getInteger("centerY"), nbt.getInteger("centerZ")),
                hive
        );
        if(nbt.hasKey("Rooms")) {
            NBTTagList list = nbt.getTagList("Rooms", 10);
            for(int i = 0; i < list.tagCount(); i++)
                connectedRooms.add(new MyrmexRoom(this, list.getCompoundTagAt(i), hive));
        }
    }

    public MyrmexRoom addConnectedRoom(MyrmexRoom room) {
        this.connectedRooms.add(room);
        return this;
    }

    public void setHasEntrance(){
        this.hasEntrance = true;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        nbt.setString("type", type.toString());
        nbt.setInteger("centerX", center.getX());
        nbt.setInteger("centerY", center.getY());
        nbt.setInteger("centerZ", center.getZ());
        if(!connectedRooms.isEmpty()) {
            NBTTagList list = new NBTTagList();
            for (MyrmexRoom connectedRoom : connectedRooms)
                list.appendTag(connectedRoom.writeToNBT(new NBTTagCompound()));
            nbt.setTag("Rooms", list);
        }
        return nbt;
    }

    public BlockPos getPos() {
        return this.center;
    }

    public MyrmexRoom getNearestRoomTowardsCenter(){
        return parent == null ? this : parent;
    }

    public BlockPos getNearestRoomPosTowardsCenter(){
        return getNearestRoomTowardsCenter().center;
    }

    @Nullable //null if there's only a hive center, no subrooms
    public MyrmexRoom getRandomConnectedRoom(Random rand){
        return connectedRooms.isEmpty() ? this.parent : connectedRooms.get(rand.nextInt(connectedRooms.size()));
    }

    public BlockPos getRandomConnectedRoomPos(Random rand){
        return getRandomConnectedRoom(rand).center;
    }

    @Nullable
    public MyrmexRoom getConnectedRoomOfType(WorldGenMyrmexHive.RoomType type){
        if(this.type.equals(type)) return this;
        for(MyrmexRoom room : connectedRooms) {
            MyrmexRoom subRoom = room.getConnectedRoomOfType(type);
            if(subRoom != null) return subRoom;
        }
        return null;
    }

    public MyrmexRoom getNearestRoomToBlockPos(BlockPos pos, double currMinDistSq) {
        double distSq = this.center.distanceSq(pos);
        MyrmexRoom nearestRoom = null;
        if(distSq < currMinDistSq){
            nearestRoom = this;
            currMinDistSq = distSq;
        }
        for(MyrmexRoom room : connectedRooms) {
            MyrmexRoom nearerRoom = room.getNearestRoomToBlockPos(pos, currMinDistSq);
            if(nearerRoom != null) { //found closer room
                nearestRoom = nearerRoom;
                currMinDistSq = nearerRoom.center.distanceSq(pos); //kinda annoying to recalc this, ill accept it for now
            }
        }
        return nearestRoom;
    }
}
