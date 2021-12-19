package ru.dragonestia.expo.dialogue;

import cn.nukkit.Player;
import lombok.Getter;
import ru.contentforge.formconstructor.form.element.Button;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.condition.ConditionManager;
import ru.dragonestia.expo.dialogue.instance.ButtonInstance;
import ru.dragonestia.expo.player.PlayerData;

import java.util.ArrayList;

public class DialogueButton {

    @Getter protected String text = "Какой-то текст у кнопки";
    @Getter protected String action = "none";
    @Getter protected final ArrayList<String> conditions = new ArrayList<>();

    public Button generateButton(PlayerData playerData, DialogueSender sender, Dialogue dialogue){
        DialogueManager dialogueManager = Expo.getInstance().getDialogueManager();
        String[] args = action.split(" ", 2);
        ButtonInstance buttonInstance = dialogueManager.getButtonInstance(args[0]);

        if(buttonInstance == null) return null;

        Player player = playerData.getPlayer();
        try {
            Button override = buttonInstance.getOverrideButton(playerData, sender, dialogue, Dialogue.format(text, playerData.getPlayer(), sender), args.length > 1? args[1].split(" ") : new String[0]);
            if(override != null) return override;
        } catch (DialogueError ex){
            player.sendMessage("§c[ERROR] " + ex.getMessage());
        } catch (NullPointerException ex){
            return null;
        }

        return new Button(
                buttonInstance.convertButtonText(Dialogue.format(text, playerData.getPlayer(), sender)),
                ImageType.PATH,
                buttonInstance.getButtonIcon(playerData),
                (p, b) -> {
                    try {
                        buttonInstance.handle(playerData, sender, dialogue, Dialogue.format(text, playerData.getPlayer(), sender), args.length > 1? args[1].split(" ") : new String[0]);
                    } catch (DialogueError ex){
                        p.sendMessage("§c[ERROR] " + ex.getMessage());
                    }
                }
        );
    }

    public boolean handleConditions(PlayerData playerData){
        ConditionManager conditionManager = Expo.getInstance().getConditionManager();

        for(String condition: conditions){
            if(!conditionManager.handleCondition(playerData, condition).isCompleted()) return false;
        }
        return true;
    }

}
