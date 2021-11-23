package ru.dragonestia.expo.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import ru.contentforge.formconstructor.form.CustomForm;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.contentforge.formconstructor.form.element.Input;
import ru.contentforge.formconstructor.form.element.Toggle;
import ru.contentforge.formconstructor.form.element.validator.RegexValidator;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.entity.npc.NameConstructor;
import ru.dragonestia.expo.entity.npc.NpcEntity;
import ru.dragonestia.expo.entity.npc.NpcProfile;

import java.util.ArrayList;

public class NpcCommand extends Command {

    private final Expo main;

    public NpcCommand(Expo main) {
        super("npc", "Управление NPC");

        this.main = main;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] args) {
        if(commandSender instanceof Player && commandSender.isOp()){
            sendMainForm((Player) commandSender);
            return true;
        }

        commandSender.sendMessage("Данную команду можно использовать только в игре.");
        return false;
    }

    private void sendMainForm(Player player){
        new SimpleForm("Создание NPC", "Для создания NPC для него потребуется задать профиль.")
                .addButton("Создать NPC", ImageType.PATH, "textures/ui/dressing_room_capes", (p, b) -> sendNpcCreateForm(p))
                .addButton("Создать профиль NPC", ImageType.PATH, "textures/ui/color_plus", (p, b) -> sendCreateProfileForm(p))
                .addButton("Редактировать профиль NPC", ImageType.PATH, "textures/ui/recap_glyph_color_2x", (p, b) -> sendFindProfileForm(p))
                .send(player);
    }

    private void sendNpcCreateForm(Player player) {
        new CustomForm("Создание NPC")
                .addElement("Для создания NPC вам потребуется задать для него профиль. На один и тот же профиль можно задавать нескольким NPC и они будут одинаковыми.")
                .addElement("profile", Input.builder().setName("id профиля").setTrim(true).build())
                .send(player, (p, response) -> {
                    String profileId = response.getInput("profile").getValue();
                    NpcProfile profile = main.getProfileManager().get(profileId);

                    if (profile == null) {
                        p.sendMessage("§cВведен неверный id профиля NPC.");
                        return;
                    }

                    NpcEntity npc = (NpcEntity) NpcEntity.createEntity(NpcEntity.ENTITY_ID, p);
                    npc.firstSpawn(p, profile);
                    npc.spawnToAll();

                    p.sendMessage("§eNPC был успешно создан!");
                });
    }

    private void sendCreateProfileForm(Player player){
        new CustomForm("Создание профиля NPC")
                .addElement("Профиль NPC - это набор настроек NPC. Один профиль вы можете использовать на нескольких NPC и они будут иметь одинаковые параметры.")
                .addElement("id профиля NPC может состоять только из §3английских букв§f нижнего регистра(строчные буквы), §3цифр§f и §3знаков подчеркивания§f.")
                .addElement("profile", Input.builder().setName("id профиля NPC").setTrim(true).addValidator(new RegexValidator("", "^[a-z\\d_]+$")).build())
                .addElement("При создании профиля будет использоваться ваш скин.")
                .send(player, (p, response) -> {
                    if(!response.isValidated()){
                        p.sendMessage("§cВведен неверный id профиля NPC.");
                        return;
                    }

                    String profileId = response.getInput("profile").getValue();

                    if(main.getProfileManager().get(profileId) != null){
                        p.sendMessage("§cДанный id профиля NPC уже существует!");
                        return;
                    }

                    NpcProfile profile = new NpcProfile(profileId, p.getSkin());
                    main.getProfileManager().createProfile(profile);

                    sendEditProfileForm(p, profile);
                });
    }

    private void sendFindProfileForm(Player player){
        new CustomForm("Поиск профиля NPC")
                .addElement("Введите id профиля NPC для его редактирования.")
                .addElement("profile", Input.builder().setName("id профиля NPC").setTrim(true).build())
                .send(player, (p, response) -> {
                    String profileId = response.getInput("profile").getValue();
                    NpcProfile profile = main.getProfileManager().get(profileId);

                    if(profile == null){
                        p.sendMessage("§cПрофиль NPC с таким id не существует.");
                        return;
                    }

                    sendEditProfileForm(p, profile);
                });
    }

    private void sendEditProfileForm(Player player, NpcProfile profile){
        new SimpleForm("Настройка профиля NPC")
                .addButton("Изменить имя", ImageType.PATH, "textures/items/name_tag", (p, b) -> sendNameForm(p, profile))
                .addButton("Изменить скин", ImageType.PATH, "textures/ui/dressing_room_skins", (p, b) -> {
                    profile.setSkin(p.getSkin());
                    p.sendMessage("§eСкин был успешно изменен!");
                }).addButton("Управление диалогами", ImageType.PATH, "textures/ui/chat_send", (p, b) -> sendDialoguesForm(p, profile))
                .addButton("Сохранить", (p, b) -> {
                    main.getProfileManager().saveProfile(profile);

                    p.sendMessage("§eВсе изменения были успешно сохранены!");
                })
                .send(player);
    }

    private void sendNameForm(Player player, NpcProfile profile){
        NameConstructor name = profile.getName();

        new CustomForm("Смена имени")
                .addElement("name", Input.builder().setName("Имя NPC").setDefaultValue(name.getName()).setTrim(true).build())
                .addElement("Доступные цвета:§2§l 0-9a-g")
                .addElement("color", Input.builder().setName("Цвет имени").setDefaultValue(name.getColor()).setTrim(true).build())
                .addElement("Ели вам не нужен титул, то просто оставьте поле пустым")
                .addElement("title", Input.builder().setName("Титул").setDefaultValue(name.getTitle() == null? "" : name.getTitle()).setTrim(true).build())
                .addElement("visible", new Toggle("Постоянная видимость имени", profile.isNameVisible()))
                .send(player, (p, response) -> {
                    name.setName(response.getInput("name").getValue());
                    name.setColor(response.getInput("color").getValue());
                    name.setTitle(response.getInput("title").getValue());
                    profile.setNameVisible(response.getToggle("visible").getValue());

                    sendEditProfileForm(p, profile);
                });
    }

    private void sendDialoguesForm(Player player, NpcProfile profile){
        CustomForm form = new CustomForm("Управление диалогами")
                .addElement("Укажите id диалогов в полях ниже. Это очередь диалогов. Если у диалога не выполняются все условия, то по очереди идет следующий и вызван будет тот, у которого выполняются все условия.");

        for(int i = 0, n = profile.getDialoguesQueue().size(), r = n + 3; i < r; i++){
            form.addElement(
                    Input.builder()
                    .setTrim(true)
                    .setName("id диалога "+i)
                    .setDefaultValue(i < n? profile.getDialoguesQueue().get(i) : "")
                    .build()
            );
        }

        form.send(player, (p, response) -> {
            ArrayList<String> dialogueQueue = new ArrayList<>();
            for(Input input: response.getInputs()){
                if(input.getValue().length() == 0) continue;

                dialogueQueue.add(input.getValue());
            }
            profile.setDialoguesQueue(dialogueQueue);

            sendEditProfileForm(p, profile);
        });
    }

}
