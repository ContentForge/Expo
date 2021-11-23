package ru.dragonestia.expo.entity.npc;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.PlayerSkinPacket;
import ru.contentforge.formconstructor.form.SimpleForm;
import ru.contentforge.formconstructor.form.element.ImageType;
import ru.dragonestia.expo.Expo;
import ru.dragonestia.expo.dialogue.Dialogue;
import ru.dragonestia.expo.player.PlayerData;

public class NpcListener implements Listener {

    private final Expo main;

    public NpcListener(Expo main){
        this.main = main;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof NpcEntity)) return;

        event.setCancelled(true);

        if(!(event instanceof EntityDamageByEntityEvent) || event instanceof EntityDamageByChildEntityEvent) return;
        if(!(((EntityDamageByEntityEvent) event).getDamager() instanceof Player)) return;

        NpcEntity npc = (NpcEntity) event.getEntity();
        Player player = (Player) ((EntityDamageByEntityEvent) event).getDamager();
        PlayerData playerData = main.getPlayerManager().get(player);

        if(player.isOp() && player.getInventory().getItemInHand().getId() == Item.CARROT_ON_A_STICK){
            sendEditForm(player, npc);
            return;
        }

        Dialogue dialogue = npc.getProfile().chooseDialogue(playerData);
        if(dialogue != null){
            dialogue.sendToPlayer(playerData, npc);
            return;
        }

        String replica = npc.getProfile().chooseReplica();
        if(replica != null){
            NameConstructor name = npc.getProfile().getName();
            player.sendMessage("§"+name.getColor()+name.getName()+"§f: §7" + Dialogue.format(replica, player, npc));
        }
    }

    private void sendEditForm(Player player, NpcEntity entity){
        new SimpleForm("Управление NPC", "id профиля NPC: §2"+entity.getProfileId())
                .addButton("Пересоздать NPC", ImageType.PATH, "textures/ui/dressing_room_animation", (p, b) -> {
                    PlayerSkinPacket packet = new PlayerSkinPacket();
                    packet.skin = entity.getSkin();
                    packet.newSkinName = "NPC";
                    packet.oldSkinName = "steve";
                    packet.uuid = entity.getUniqueId();
                    for(Player target: player.getLevel().getPlayers().values()) target.dataPacket(packet);

                    entity.setNameTag(entity.getProfile().getName().generateName());
                    entity.setNameTagAlwaysVisible(entity.getProfile().isNameVisible());

                    p.sendMessage("§eВы успешно пересоздали NPC!");
                }).addButton("Удалить NPC", ImageType.PATH, "textures/ui/realms_red_x", (p, b) -> {
                    entity.close();

                    p.sendMessage("§eNPC был успешно удален!");
                }).send(player);
    }

}
