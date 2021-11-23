package ru.dragonestia.expo.player;

import cn.nukkit.Player;
import lombok.Getter;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.paragraph.Paragraph;

import java.util.HashSet;

public final class PlayerData {

    private HashSet<String> completedParagraphs = new HashSet<>();
    private HashSet<String> readDialogues = new HashSet<>();

    @Getter private transient Expo main;
    @Getter private transient Player player;

    public void init(Expo main, Player player){
        this.main = main;
        this.player = player;
    }

    public void completeParagraph(Paragraph paragraph){
        completedParagraphs.add(paragraph.getId());
    }

    public boolean isCompletedParagraph(Paragraph paragraph){
        return isCompletedParagraph(paragraph.getId());
    }

    public boolean isCompletedParagraph(String paragraphId){
        return completedParagraphs.contains(paragraphId);
    }

    public void readDialogue(Dialogue dialogue){
        readDialogues.add(dialogue.getId());
    }

    public boolean isReadDialogue(String dialogueId){
        return readDialogues.contains(dialogueId);
    }

}
