package ru.dragonestia.expo.entity.npc;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.PlayerSkinPacket;
import cn.nukkit.utils.Utils;
import lombok.Getter;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.dialogue.DialogueSender;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class NpcEntity extends Entity implements DialogueSender {

    public final static String ENTITY_ID = "expo_npc";
    @Getter private NpcProfile profile = null;
    protected UUID uuid;

    public NpcEntity(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);

        if(namedTag.exist("expo")){
            profile = Expo.getInstance().getProfileManager().get(getProfileId());
            if(getProfile() == null){
                close();
                return;
            }

            init();
        }
    }

    @Override
    protected void initEntity() {
        super.initEntity();

        if(namedTag.exist("expo")) init();
    }

    @Override
    public String getName(){
        return "NPC";
    }

    @Override
    public void saveNBT() {
        super.saveNBT();

        namedTag.putString("expo", profile.getId());
    }

    public void firstSpawn(Player spawner, NpcProfile profile) {
        yaw = (float) spawner.getYaw();
        pitch = (float) spawner.getPitch();
        namedTag.putString("expo", profile.getId());
        this.profile = profile;

        initSettings();
    }


    public String getProfileId(){
        return namedTag.getString("expo");
    }

    public void init(){
        despawnFromAll();

        setImmobile(true);
        profile = Expo.getInstance().getProfileManager().get(getProfileId());
        if(profile == null) return;

        initSettings();
        spawnToAll();
    }

    public void initSettings(){
        setNameTag(profile.getName().generateName());
        setNameTagAlwaysVisible(profile.isNameVisible());

        uuid = Utils.dataToUUID(String.valueOf(this.getId()).getBytes(StandardCharsets.UTF_8), this.getSkin().getSkinData().data, this.getNameTag().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void spawnTo(Player player) {
        if (!this.hasSpawned.containsKey(player.getLoaderId())) {
            this.hasSpawned.put(player.getLoaderId(), player);

            server.updatePlayerListData(getUniqueId(), getId(), getName(), profile.getSkin(), new Player[]{player});

            AddPlayerPacket pk = new AddPlayerPacket();
            pk.uuid = uuid;
            pk.username = getNpcId() + id;
            pk.entityUniqueId = id;
            pk.entityRuntimeId = id;
            pk.x = (float) x;
            pk.y = (float) y;
            pk.z = (float) z;
            pk.speedX = (float) motionX;
            pk.speedY = (float) motionY;
            pk.speedZ = (float) motionZ;
            pk.yaw = (float) yaw;
            pk.pitch = (float) pitch;
            pk.item = Item.get(0);
            pk.metadata = dataProperties;
            player.dataPacket(pk);

            this.server.removePlayerListData(getUniqueId(), new Player[]{player});

            PlayerSkinPacket packet = new PlayerSkinPacket();
            packet.skin = profile.getSkin();
            packet.newSkinName = "NPC";
            packet.oldSkinName = "steve";
            packet.uuid = getUniqueId();
            player.dataPacket(packet);

            super.spawnTo(player);
        }
    }

    @Override
    public int getNetworkId() {
        return EntityHuman.NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 0.6F;
    }

    @Override
    public float getLength() {
        return 0.6F;
    }

    @Override
    public float getHeight() {
        return 1.8F;
    }

    @Override
    public float getEyeHeight() {
        return 1.7F;
    }

    @Override
    protected float getBaseOffset() {
        return 1.6F;
    }

    public Skin getSkin() {
        return profile.getSkin();
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public String getNpcName() {
        return profile.getName().getName();
    }

    @Override
    public String getNpcId() {
        return getProfileId();
    }

}
