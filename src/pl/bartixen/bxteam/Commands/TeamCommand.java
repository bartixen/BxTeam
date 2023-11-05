package pl.bartixen.bxteam.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import pl.bartixen.bxteam.Data.DataManager;
import pl.bartixen.bxteam.Main;
import pl.bartixen.bxteam.Utils.ItemBuilder;

import java.io.IOException;
import java.util.UUID;

public class TeamCommand implements CommandExecutor {

    Main plugin;
    static DataManager data;

    public TeamCommand(Main m) {
        plugin = m;
        data = DataManager.getInstance();
        m.getCommand("team").setExecutor(this);
        m.getCommand("teamy").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cTa komenda jest przeznaczona tylko dla graczy");
            return false;
        }
        Player p = (Player) sender;
        try {
            team(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static InventoryView team(Player p) throws IOException {
        UUID uuid = p.getUniqueId();

        Inventory inventory = Bukkit.createInventory((InventoryHolder) p, 27, ("§9§lTEAMY"));
        if (data.getData().getString(uuid + ".team_leader") == null && data.getData().getString(uuid + ".player") == null) {
            ItemBuilder slot11 = (new ItemBuilder(Material.CRAFTING_TABLE, 1)).setTitle("§e§lStwórz team").addLore("§7").addLore("§fKliknij, aby go stworzyć i potem nim zarzadzać");
            inventory.setItem(11, slot11.build());
        } else {
            ItemBuilder slot11 = (new ItemBuilder(Material.CHEST, 1)).setTitle("§e§lTwój team").addLore("§7").addLore("§fKliknij, aby zobaczyć swój team");
            inventory.setItem(11, slot11.build());
        }
        if (data.getData().getString(uuid + ".invitation") == null) {
            ItemBuilder slot13 = (new ItemBuilder(Material.LIGHT_GRAY_CONCRETE, 1)).setTitle("§7§lBrak zaproszeń do teamu").addLore("§7").addLore("§fObecnie nie posiadasz żadnych zaproszeń,").addLore("§fjeżeli chcesz dostać zaproszenie do danego teamu").addLore("§fto poproś o to lidera teamu");
            inventory.setItem(13, slot13.build());
        } else {
            String team = data.getData().getString(uuid + ".invitation");
            ItemBuilder slot13 = (new ItemBuilder(Material.LIME_CONCRETE, 1)).setTitle("§a§lPosiadasz zaproszenie do teamu: " + team).addLore("§7").addLore("§fKliknij, aby dołączyć do teamu");
            inventory.setItem(13, slot13.build());
        }
        ItemBuilder slot15 = (new ItemBuilder(Material.PAPER, 1)).setTitle("§e§lWyświetl teamy").addLore("§7").addLore("§fKliknij, aby otworzyć listę teamów");
        inventory.setItem(15, slot15.build());

        ItemBuilder backguard = (new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 1)).setTitle("§7");
        inventory.setItem(0, backguard.build());
        inventory.setItem(1, backguard.build());
        inventory.setItem(2, backguard.build());
        inventory.setItem(3, backguard.build());
        inventory.setItem(4, backguard.build());
        inventory.setItem(5, backguard.build());
        inventory.setItem(6, backguard.build());
        inventory.setItem(7, backguard.build());
        inventory.setItem(8, backguard.build());
        inventory.setItem(9, backguard.build());
        inventory.setItem(10, backguard.build());
        inventory.setItem(12, backguard.build());
        inventory.setItem(14, backguard.build());
        inventory.setItem(16, backguard.build());
        inventory.setItem(17, backguard.build());
        inventory.setItem(18, backguard.build());
        inventory.setItem(19, backguard.build());
        inventory.setItem(20, backguard.build());
        inventory.setItem(21, backguard.build());
        inventory.setItem(22, backguard.build());
        inventory.setItem(23, backguard.build());
        inventory.setItem(24, backguard.build());
        inventory.setItem(25, backguard.build());
        inventory.setItem(26, backguard.build());
        return p.openInventory(inventory);
    }

}
