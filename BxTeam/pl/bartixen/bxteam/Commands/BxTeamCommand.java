package pl.bartixen.bxteam.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pl.bartixen.bxteam.Data.DataManager;
import pl.bartixen.bxteam.Main;

public class BxTeamCommand implements CommandExecutor {

    Main plugin;
    DataManager data;

    public BxTeamCommand(Main m) {
        plugin = m;
        data = DataManager.getInstance();
        m.getCommand("bxteam").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String version = plugin.version;
        if (args.length == 1) {
            if (sender.hasPermission("bxteam.commands.reload") || sender.isOp()) {
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage("§8 • — • — • — • ");
                    plugin.reloadConfig();
                    sender.sendMessage("§7Pomyślnie przeladowano §9CONFIG");
                    data.reloadData();
                    sender.sendMessage("§7Pomyślnie przeladowano §9DATA");
                    sender.sendMessage("§8 • — • — • — • ");
                } else {
                    sender.sendMessage("§7");
                    sender.sendMessage("§7Plugin §eBxTeam");
                    sender.sendMessage("§7Version: §e" + version);
                    sender.sendMessage("§7Author: §eBartixen");
                    sender.sendMessage("§7Website: §ehttps://bartixen.pl");
                    sender.sendMessage("§7");
                }
            } else {
                sender.sendMessage("§7");
                sender.sendMessage("§7Plugin §eBxTeam");
                sender.sendMessage("§7Version: §e" + version);
                sender.sendMessage("§7Author: §eBartixen");
                sender.sendMessage("§7Website: §ehttps://bartixen.pl");
                sender.sendMessage("§7");
            }
        } else {
            sender.sendMessage("§7");
            sender.sendMessage("§7Plugin §eBxTeam");
            sender.sendMessage("§7Version: §e" + version);
            sender.sendMessage("§7Author: §eBartixen");
            sender.sendMessage("§7Website: §ehttps://bartixen.pl");
            sender.sendMessage("§7");
        }
        return false;
    }

}
