package pl.bartixen.bxteam.Data;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class DataManager {

    static DataManager instance;
    Plugin p;
    FileConfiguration data;
    public static File file;

    static {
        instance = new DataManager();
    }

    public static DataManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) throws IOException {
        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdirs();
        }
        File path = new File(p.getDataFolder() + File.separator + "/data");
        file = new File(path, File.separator + "team.yml");
        if (!file.exists()) {
            try {
                path.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getServer().getLogger().log(Level.WARNING, "Failed to create file team.yml");
            }
        }
        data = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getData() {
        return data;
    }

    public void saveData() throws IOException {
        try {
            data.save(file);
        } catch (IIOException e) {
            Bukkit.getServer().getLogger().log(Level.WARNING, "Failed to save the file team.yml");
        }
    }

    public void reloadData() {
        data = YamlConfiguration.loadConfiguration(file);
    }
}
