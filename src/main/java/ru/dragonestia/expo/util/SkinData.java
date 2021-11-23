package ru.dragonestia.expo.util;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.SerializedImage;
import lombok.SneakyThrows;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SkinData {

    private final Skin skin;

    public SkinData(Skin skin){
        this.skin = skin;
    }

    @SneakyThrows
    public SkinData(String data){
        CompoundTag skinTag = NBTIO.read(Base64.getDecoder().decode(data));
        skin = new Skin();

        skin.setSkinData(new SerializedImage(
                skinTag.getInt("SkinImageWidth"),
                skinTag.getInt("SkinImageHeight"),
                skinTag.getByteArray("Data")
        ));
        skin.setCapeId(skinTag.getString("CapeId"));
        skin.setCapeData(new SerializedImage(
                skinTag.getInt("CapeImageWidth"),
                skinTag.getInt("CapeImageHeight"),
                skinTag.getByteArray("CapeData")
        ));
        skin.setCapeOnClassic(skinTag.getBoolean("CapeOnClassicSkin"));
        skin.setPersona(skinTag.getBoolean("PersonaSkin"));
        skin.setPremium(skinTag.getBoolean("PremiumSkin"));
        skin.setAnimationData(new String(skinTag.getByteArray("AnimationData"), StandardCharsets.UTF_8));
        skin.setGeometryData(new String(skinTag.getByteArray("GeometryData"), StandardCharsets.UTF_8));
        skin.setSkinResourcePatch(new String(skinTag.getByteArray("SkinResourcePatch"), StandardCharsets.UTF_8));
        skin.setSkinId(skinTag.getString("ModelId"));
    }

    public Skin getSkin() {
        return skin;
    }

    @SneakyThrows
    public String getData(){
        CompoundTag skinTag = new CompoundTag()
                .putByteArray("Data", skin.getSkinData().data)
                .putInt("SkinImageWidth", skin.getSkinData().width)
                .putInt("SkinImageHeight", skin.getSkinData().height)
                .putString("ModelId", skin.getSkinId())
                .putString("CapeId", skin.getCapeId())
                .putByteArray("CapeData", skin.getCapeData().data)
                .putInt("CapeImageWidth", skin.getCapeData().width)
                .putInt("CapeImageHeight", skin.getCapeData().height)

                .putByteArray("SkinResourcePatch", skin.getSkinResourcePatch().getBytes(StandardCharsets.UTF_8))
                .putByteArray("GeometryData", skin.getGeometryData().getBytes(StandardCharsets.UTF_8))
                .putByteArray("AnimationData", skin.getAnimationData().getBytes(StandardCharsets.UTF_8))
                .putBoolean("PremiumSkin", skin.isPremium())
                .putBoolean("PersonaSkin", skin.isPersona())
                .putBoolean("CapeOnClassicSkin", skin.isCapeOnClassic());

        return new String(Base64.getEncoder().encode(NBTIO.write(skinTag)));
    }

    public String getId(){
        return skin.getSkinId();
    }

}
