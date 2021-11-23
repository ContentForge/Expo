package ru.dragonestia.expo.dialogue;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import lombok.Getter;
import lombok.SneakyThrows;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.dialogue.instance.*;
import ru.dragonestia.expo.event.DialogueOpenEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class DialogueManager implements Listener {

    @Getter private final Expo main;
    private HashMap<String, Dialogue> dialogues;
    private final HashMap<String, ButtonInstance> buttonInstances = new HashMap<>();

    public static final String DIALOGUES_DIR = "plugins/Expo/dialogues";

    public DialogueManager(Expo main){
        this.main = main;

        initDefaultButtonInstances();
        createExampleDialogue();
        init();
    }

    public void init(){
        File dialoguesDir = new File(DIALOGUES_DIR);
        if(!dialoguesDir.exists()) dialoguesDir.mkdir();

        dialogues = new HashMap<>();
        loadDialogues();
    }

    private void initDefaultButtonInstances(){
        registerButtonInstance(new NoneButtonInstance());
        registerButtonInstance(new DialogueButtonInstance(this));
        registerButtonInstance(new QuestionButtonInstance());
        registerButtonInstance(new DispatchCommandButtonInstance());
    }

    @SneakyThrows
    private void loadDialogues(){
        File dir = new File("plugins/Expo/dialogues");
        if(!dir.exists()) dir.mkdir();

        for(File file: dir.listFiles()){
            if(!file.isFile()) continue;

            try(FileReader reader = new FileReader(file)){
                Dialogue dialogue = Expo.getGson().fromJson(reader, Dialogue.class);
                dialogues.put(dialogue.getId(), dialogue);
            }
        }
    }

    public Dialogue getDialogue(String dialogueId){
        return dialogues.getOrDefault(dialogueId, null);
    }

    public void registerButtonInstance(ButtonInstance buttonInstance){
        buttonInstances.put(buttonInstance.getId(), buttonInstance);
    }

    public ButtonInstance getButtonInstance(String buttonInstanceId){
        return buttonInstances.getOrDefault(buttonInstanceId, null);
    }

    @SneakyThrows
    private void createExampleDialogue(){
        File file = new File("plugins/Expo/example_dialogue.json");
        if(!file.exists()) file.createNewFile();

        Dialogue dialogue = new Dialogue( "example_dialogue");
        dialogue.setText("Какой-то текст");
        dialogue.setButtons(new DialogueButton[]{new DialogueButton()});
        dialogue.setConditions(new String[]{"test 1 2 3"});

        try(FileWriter writer = new FileWriter(file)){
            writer.write(Expo.getGson().toJson(dialogue));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDialogOpened(DialogueOpenEvent event){
        event.getPlayerData().readDialogue(event.getDialogue());
    }

}
