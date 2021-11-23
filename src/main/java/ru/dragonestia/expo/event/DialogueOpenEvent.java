package ru.dragonestia.expo.event;

import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import lombok.Getter;
import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.dialogue.DialogueSender;
import ru.dragonestia.expo.player.PlayerData;

public class DialogueOpenEvent extends PlayerEvent {

    @Getter private static final HandlerList handlers = new HandlerList();

    @Getter private final Dialogue dialogue;
    @Getter private final DialogueSender sender;
    @Getter private final PlayerData playerData;

    public DialogueOpenEvent(PlayerData playerData, Dialogue dialogue, DialogueSender sender){
        this.playerData = playerData;
        this.player = playerData.getPlayer();
        this.dialogue = dialogue;
        this.sender = sender;
    }

}
