package ru.dragonestia.expo.dialogue;

import cn.nukkit.Player;
import lombok.Getter;
import lombok.Setter;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.Button;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.condition.ConditionManager;
import ru.dragonestia.expo.event.DialogueOpenEvent;
import ru.dragonestia.expo.player.PlayerData;

@Getter
public class Dialogue {

    private String id;
    @Setter private String[] text = null;
    @Setter private DialogueButton[] buttons = new DialogueButton[0];
    @Setter private String[] conditions = new String[0];

    public Dialogue(String id){
        this.id = id;
    }

    public static String format(String text, Player player, DialogueSender sender){
        return text.replace("{player}", player.getName())
                .replace("{sender}", sender.getNpcName());
    }

    public boolean canOpen(PlayerData playerData){
        ConditionManager conditionManager = Expo.getInstance().getConditionManager();
        for(String condition: conditions){
            if(!conditionManager.handleCondition(playerData, condition).isCompleted()) return false;
        }
        return true;
    }

    public void sendToPlayer(PlayerData playerData, DialogueSender sender){
        sendToPlayer(playerData, sender, null, false, text);
    }

    public void sendToPlayer(PlayerData playerData, DialogueSender sender, String quote, boolean replied, String[] text){
        Player player = playerData.getPlayer();

        SimpleForm form = new SimpleForm(sender.getNpcName());

        if(quote != null){
            form.addContent("§g§l"+player.getName()+"§f >> §r"+format(quote, player, sender)+"§r\n");
        }

        if(text != null){
            if(quote != null) form.addContent("\n");
            form.addContent("§2§l"+sender.getNpcName()+"§f >> §r"+format(String.join("\n ", text), player, sender)+"§r\n");
        }

        form.addContent("\n\n\n\n");

        if(player.isOp()){
            form.addContent("§l§d[id: "+id+"]");
        }

        for(DialogueButton button: buttons){
            if(!button.handleConditions(playerData)) continue;

            Button b = button.generateButton(playerData, sender, this);
            if(b == null) continue;
            form.addButton(b);
        }

        if(!replied){
            player.getServer().getPluginManager().callEvent(new DialogueOpenEvent(playerData, this, sender));
        }

        form.send(player);
    }

}
