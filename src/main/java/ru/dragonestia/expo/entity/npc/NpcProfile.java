package ru.dragonestia.expo.entity.npc;

import cn.nukkit.entity.data.Skin;
import lombok.Getter;
import lombok.Setter;
import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.dialogue.DialogueManager;
import ru.dragonestia.expo.player.PlayerData;
import ru.dragonestia.expo.util.SkinData;

import java.util.ArrayList;

public class NpcProfile {

    @Getter private String id;
    @Getter @Setter private NameConstructor name = new NameConstructor();
    @Getter @Setter private boolean nameVisible = false;
    @Getter @Setter private ArrayList<String> dialoguesQueue = new ArrayList<>();
    @Getter @Setter private ArrayList<String> replicas = new ArrayList<>();
    private String skinData = null;
    @Getter private transient Skin skin = null;

    public NpcProfile(String id, Skin skin){
        this.id = id;

        setSkin(skin);
    }

    public void init(){
        if(skinData != null){
            skin = new SkinData(skinData).getSkin();
        }
    }

    public void setSkin(Skin newSkin){
        SkinData data = new SkinData(newSkin);

        skin = data.getSkin();
        skinData = data.getData();
    }

    public String chooseReplica(){
        if(replicas.size() == 0) return null;

        return replicas.get(ProfileManager.getRandom().nextInt(replicas.size()));
    }

    public Dialogue chooseDialogue(PlayerData playerData){
        if(dialoguesQueue.size() == 0) return null;

        DialogueManager dialogueManager = playerData.getMain().getDialogueManager();
        for(String dialogueId: dialoguesQueue){
            Dialogue dialogue = dialogueManager.getDialogue(dialogueId);
            if(dialogue == null){
                if(playerData.getPlayer().isOp()) playerData.getPlayer().sendMessage("§c[ERROR] Диалог "+dialogueId+" не найден!");
                continue;
            }

            if(dialogue.canOpen(playerData)) return dialogue;
        }
        return null;
    }

}
