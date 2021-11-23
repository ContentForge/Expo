package ru.dragonestia.expo.condition;

import lombok.Getter;
import ru.dragonestia.expo.player.PlayerData;

public abstract class Condition {

    @Getter private final String name;

    public Condition(String name){
        this.name = name;
    }

    public abstract Result handle(PlayerData playerData, String[] args) throws IndexOutOfBoundsException;

    public static class Result {

        @Getter private final boolean completed;
        @Getter private final String text;

        public Result(String text, boolean completed){
            this.completed = completed;
            this.text = text;
        }

        public String getResultText(){
            return (completed? "§2" : "§4") + text;
        }
    }

}
