package ru.dragonestia.expo.dialogue;

import lombok.Getter;

public class DialogueError extends RuntimeException {

    @Getter private final String message;

    public DialogueError(String message){
        this.message = message;
    }

}
