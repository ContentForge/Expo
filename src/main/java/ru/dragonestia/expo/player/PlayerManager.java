package ru.dragonestia.expo.player;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import lombok.Getter;
import lombok.SneakyThrows;
import ru.dragonestia.expo.Expo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

public class PlayerManager implements Listener {

    @Getter private final Expo main;
    private final HashMap<Long, PlayerData> players = new HashMap<>();

    public PlayerManager(Expo main){
        this.main = main;

        File dir = new File("plugins/Expo/players");
        if(!dir.exists()) dir.mkdir();
    }

    @SneakyThrows
    private PlayerData load(Player player){
        File file = getPlayerFile(player);
        PlayerData data;

        if(file.exists()){
            try(FileReader reader = new FileReader(file)) {
                data = Expo.getGson().fromJson(reader, PlayerData.class);
            }
        } else data = new PlayerData();

        data.init(main, player);
        return data;
    }

    public PlayerData get(Player player){
        return players.getOrDefault(player.getId(), null);
    }

    @SneakyThrows
    private void save(PlayerData playerData){
        File file = getPlayerFile(playerData.getPlayer());

        if(!file.exists()) file.createNewFile();
        try(FileWriter writer = new FileWriter(file)){
            writer.write(Expo.getGson().toJson(playerData));
        }
    }

    private File getPlayerFile(Player player){
        return new File("plugins/Expo/players/"+player.getLoginChainData().getXUID()+".json");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onLogin(PlayerLoginEvent event){
        Player player = event.getPlayer();

        players.put(player.getId(), load(player));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(players.containsKey(player.getId())) save(players.get(player.getId()));
        players.remove(player.getId());
    }

}
