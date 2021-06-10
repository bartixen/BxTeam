package pl.bartixen.bxteam;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.bartixen.bxteam.Commands.BxTeamCommand;
import pl.bartixen.bxteam.Commands.TeamCommand;
import pl.bartixen.bxteam.Data.DataManager;
import pl.bartixen.bxteam.Listeners.PlayerJoin;
import pl.bartixen.bxteam.Listeners.TeamListener;

import java.io.IOException;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    public static Main main;
    public String version = getDescription().getVersion();

    DataManager data;

    public Main() {
        data = DataManager.getInstance();
    }

    @Override
    public void onEnable() {
        if ((!getDescription().getName().contains("BxTeam")) || (!getDescription().getAuthors().contains("Bartixen"))) {
            getLogger().log(Level.WARNING, "§8[========== §9BxTeam §8==========]");
            getLogger().log(Level.WARNING, "§cPlugin zostal wylaczony z powodu edytowania pliku §eplugin.yml");
            getLogger().log(Level.WARNING, "§8[========== §9BxTeam §8==========]");
            Bukkit.getServer().getPluginManager().disablePlugin(this);
        } else {
            getLogger().log(Level.INFO, "§8[========== §9BxTeam §8==========]");
            getLogger().log(Level.INFO, "§fVersion: §b{0}", getDescription().getVersion());
            getLogger().log(Level.INFO, "§fAuthor: §bBartixen");
            getLogger().log(Level.INFO, "§fWebsite: §bhttps://bartixen.pl");
            getLogger().log(Level.INFO, "§8[========== §9BxTeam §8==========]");

            getConfig().options().copyDefaults(true);
            saveConfig();

            try {
                data.setup(this);
            } catch (IOException e) {
                e.printStackTrace();
            }

            new TeamCommand(this);
            new BxTeamCommand(this);

            getServer().getPluginManager().registerEvents(new TeamListener(this), this);
            getServer().getPluginManager().registerEvents(new PlayerJoin(this), this);

        }
    }

    public static Main getMain() {
        return main;
    }

}
