package pl.bartixen.bxteam.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.bartixen.bxteam.Data.DataManager;
import pl.bartixen.bxteam.Main;

import java.io.IOException;

public class PlayerJoin implements Listener {

    Main plugin;
    DataManager data;

    public PlayerJoin(Main m) {
        plugin = m;
        data = DataManager.getInstance();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws IOException {
        Player p = e.getPlayer();
        String uuid = p.getUniqueId().toString();
        data.getData().set(p.getDisplayName() + ".uuid", uuid);
        data.saveData();
    }

}
