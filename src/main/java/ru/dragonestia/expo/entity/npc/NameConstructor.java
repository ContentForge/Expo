package ru.dragonestia.expo.entity.npc;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NameConstructor {

    private String name = "unnamed";
    private String color = "2";
    private String title = null;

    public String generateName(){
        String tag = "§l§" + color + name + "§r";
        if(title != null && title.length() != 0) tag += "\n§7<"+title+">";
        return tag;
    }

}
