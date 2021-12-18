package ru.dragonestia.expo.entity.npc;

import cn.nukkit.scheduler.AsyncTask;
import com.google.gson.JsonSyntaxException;
import lombok.Getter;
import lombok.SneakyThrows;
import ru.dragonestia.expo.Expo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Random;

public class ProfileManager {

    @Getter private static final Random random = new Random();

    @Getter private final Expo main;
    private final HashMap<String, NpcProfile> profiles = new HashMap<>();

    public ProfileManager(Expo main){
        this.main = main;

        initProfiles();
    }

    @SneakyThrows
    private void initProfiles(){
        File dir = new File("plugins/Expo/npc");
        if(!dir.exists()) dir.mkdir();

        for(File file: dir.listFiles()){
            if(!file.isFile()) continue;

            try(FileReader reader = new FileReader(file)){
                NpcProfile profile = Expo.getGson().fromJson(reader, NpcProfile.class);
                profile.init();
                profiles.put(profile.getId(), profile);
            } catch (JsonSyntaxException ex) {
                main.getLogger().error("Произошла ошибка при загрузке профия NPC в файле " + file.getName());
            }
        }
    }

    public void createProfile(NpcProfile profile){
        profiles.put(profile.getId(), profile);
        saveProfile(profile);
    }

    @SneakyThrows
    public synchronized void saveProfile(NpcProfile profile){
        main.getServer().getScheduler().scheduleAsyncTask(main, new SaveProfileAsyncTask(profile));
    }

    public NpcProfile get(String profileId){
        return profiles.getOrDefault(profileId, null);
    }

    public static class SaveProfileAsyncTask extends AsyncTask {

        private final NpcProfile profile;

        private SaveProfileAsyncTask(NpcProfile profile){
            this.profile = profile;
        }

        @SneakyThrows
        @Override
        public void onRun() {
            File file = new File("plugins/Expo/npc/"+profile.getId());

            if(!file.exists()) file.createNewFile();

            try(FileWriter writer = new FileWriter(file)){
                writer.write(Expo.getGson().toJson(profile));
            }
        }
    }

}
