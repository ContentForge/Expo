package ru.dragonestia.expo.event;

import cn.nukkit.Player;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import lombok.Getter;
import ru.dragonestia.expo.player.PlayerData;
import ru.dragonestia.expo.paragraph.Paragraph;

public class PlayerLearnedParagraphEvent extends PlayerEvent {

    @Getter private static final HandlerList handlers = new HandlerList();

    @Getter private final PlayerData playerData;
    @Getter private final Paragraph paragraph;

    public PlayerLearnedParagraphEvent(Player player, PlayerData playerData, Paragraph paragraph){
        this.player = player;
        this.playerData = playerData;
        this.paragraph = paragraph;
    }

}