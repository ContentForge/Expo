package ru.dragonestia.expo.workbench;

import lombok.Getter;
import ru.dragonestia.expo.Expo;
import java.util.HashMap;

public class WorkbenchManager {

    @Getter private final Expo main;
    private final HashMap<String, Workbench> workbenches = new HashMap<>();

    public WorkbenchManager(Expo main){
        this.main = main;
    }

    public void registerWorkbench(Workbench workbench){
        workbenches.put(workbench.getId(), workbench);
    }

    public Workbench get(String id){
        return workbenches.getOrDefault(id, null);
    }

    public void reload(){
        for(Workbench workbench: workbenches.values()) workbench.init();
    }

}
