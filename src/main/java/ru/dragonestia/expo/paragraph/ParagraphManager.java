package ru.dragonestia.expo.paragraph;

import lombok.Getter;
import lombok.SneakyThrows;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.workbench.Workbench;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class ParagraphManager {

    @Getter private final Expo main;
    private HashMap<String, Paragraph> paragraphs;

    public ParagraphManager(Expo main){
        this.main = main;

        createExampleParagraph();
        init();
    }

    public void init(){
        paragraphs = new HashMap<>();
        initParagraphs();
    }

    @SneakyThrows
    private void initParagraphs(){
        File dir = new File("plugins/Expo/paragraphs");
        if(!dir.exists()) dir.mkdir();

        for(File file: dir.listFiles()){
            if(!file.isFile()) continue;

            try(FileReader reader = new FileReader(file)){
                Paragraph paragraph = Expo.getGson().fromJson(reader, Paragraph.class);
                paragraphs.put(paragraph.getId(), paragraph);
            }
        }
    }

    public void initWorkbench(Workbench workbench){
        for(Paragraph paragraph: paragraphs.values()){
            if(!workbench.getId().equals(paragraph.getWorkbench())) continue;

            workbench.addParagraph(paragraph);
        }
    }

    public Paragraph get(String id){
        return paragraphs.getOrDefault(id, null);
    }

    @SneakyThrows
    private void createExampleParagraph(){
        File file = new File("plugins/Expo/example_paragraph.json");
        if(!file.exists()) file.createNewFile();

        Paragraph paragraph = new Paragraph("example_paragraph");
        paragraph.setName("Название главы");
        paragraph.setDev(false);
        paragraph.setText("Здесь должен быть написан какой-то текст главы");
        paragraph.setIcon("textures/items/diamond");
        paragraph.setWorkbench("example_workshop");
        paragraph.setDepend("other_paragraph");
        paragraph.setConditions(new String[]{
                "test 1 2 3"
        });

        try(FileWriter writer = new FileWriter(file)){
            writer.write(Expo.getGson().toJson(paragraph));
        }
    }

}
