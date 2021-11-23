package ru.dragonestia.expo.dialogue.instance;

import lombok.Getter;
import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.dialogue.DialogueError;
import ru.dragonestia.expo.dialogue.DialogueSender;
import ru.dragonestia.expo.player.PlayerData;

public abstract class ButtonInstance {

    @Getter private final String id;

    public ButtonInstance(String id){
        this.id = id;
    }

    public String getButtonIcon(PlayerData playerData){
        return "";
    }

    public String convertButtonText(String text){
        return text;
    }

    public abstract void handle(PlayerData playerData, DialogueSender sender, Dialogue dialogue, String text, String[] args) throws DialogueError;

}
