package pl.bartixen.bxteam.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pl.bartixen.bxteam.Data.DataManager;
import pl.bartixen.bxteam.Main;
import pl.bartixen.bxteam.Utils.ItemBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TeamListener implements Listener {

    Main plugin;
    static DataManager data;

    public TeamListener(Main m) {
        plugin = m;
        data = DataManager.getInstance();
    }

    @EventHandler
    public void onOpenMenu(InventoryClickEvent e) throws IOException {
        Player p = (Player) e.getWhoClicked();
        UUID uuid = p.getUniqueId();
        if (e.getView().getTitle().equals("§9§lTEAMY")) {
            if (e.getRawSlot() == -999) p.closeInventory();
            if (e.getRawSlot() < e.getInventory().getSize()) {
                e.setCancelled(true);
                if (e.getRawSlot() == 11) {
                    p.closeInventory();
                    create_team(p);
                }
                if (e.getRawSlot() == 13) {
                    if (data.getData().getString(uuid + ".zaproszenie") != null) {
                        p.closeInventory();
                        dolaczenie_team(p);
                    }
                }
                if (e.getRawSlot() == 15) {
                    p.closeInventory();
                    lista_teamow(p);
                }
            }
        }
        if (e.getView().getTitle().equals("§9§lLISTA TEAMÓW")) {
            if (e.getRawSlot() == -999) p.closeInventory();
            if (e.getRawSlot() < e.getInventory().getSize()) {
                e.setCancelled(true);
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals("§9§lTWÓJ TEAM")) {
            if (e.getRawSlot() == -999) p.closeInventory();
            if (e.getRawSlot() < e.getInventory().getSize()) {
                e.setCancelled(true);
                p.closeInventory();
            }
        }
        if (e.getView().getTitle().equals("§9§lZARZADZAJ TEAM")) {
            if (e.getRawSlot() == -999) p.closeInventory();
            if (e.getRawSlot() < e.getInventory().getSize()) {
                e.setCancelled(true);
                p.closeInventory();
                if (e.getRawSlot() == 13) {
                    String team = data.getData().getString(uuid + ".lider_teamu");
                    e.setCancelled(true);
                }
                if (e.getRawSlot() == 15) {
                    String team = data.getData().getString(uuid + ".lider_teamu");
                    zapros_gracza(p, team);
                }
                if (e.getRawSlot() == 26) {
                    String team = data.getData().getString(uuid + ".lider_teamu");
                    usun_team(p, team);
                }
            }
        }
        if (e.getView().getTitle().equals("§9§lPOTWIERDŹ USUNIECIE TEAMU")) {
            if (e.getRawSlot() == -999) p.closeInventory();
            if (e.getRawSlot() < e.getInventory().getSize()) {
                e.setCancelled(true);
                String team = data.getData().getString(uuid + ".lider_teamu");
                if (data.getData().getString(team + ".lider").equals(p.getDisplayName())) {
                    if (e.getRawSlot() == 12) {
                        p.closeInventory();
                        data.getData().set(uuid + ".lider_teamu", null);
                        Date now = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        data.getData().set(team + ".status", "USUNIETY PRZEZ " + p.getDisplayName() + " (" + format.format(now) + ")");
                        ArrayList<String> teamy;
                        teamy = new ArrayList<>(data.getData().getStringList("teamy"));
                        teamy.remove(team);
                        data.getData().set("teamy", teamy);
                        data.saveData();
                        p.sendMessage("§fPomyślnie usunieto team");
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            players.sendMessage("§7Gracz §9" + p.getDisplayName() + " §7usunal team: §9" + team);
                        }
                    }
                    if (e.getRawSlot() == 14) {
                        p.closeInventory();
                        p.sendMessage("§7Pomyślnie anulowano akcje");
                    }
                }
            }
        }
    }

    public void zarzadanie_team(Player p, String team) {
        p.sendMessage(team);
    }

    public void dolaczenie_team(Player p) throws IOException {
        UUID uuid = p.getUniqueId();
        String team = data.getData().getString(uuid + ".zaproszenie");
        data.getData().set(uuid + ".zaproszenie", null);
        data.getData().set(uuid + ".gracz", team);
        ArrayList<String> gracze = new ArrayList<>();
        gracze = new ArrayList<>(data.getData().getStringList(team + ".gracze"));
        gracze.add(p.getDisplayName());
        data.getData().set(team + ".gracze", gracze);
        data.saveData();
        p.sendMessage("§7Pomyślnie dolaczono do teamu");
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage("§7Gracz §9" + p.getDisplayName() + " §7dolaczył do teamu: §9" + team);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws IOException {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        data.getData().set(uuid + ".zapraszanie", null);
        data.getData().set(uuid + ".create_team", null);
        data.saveData();
    }

    public void zapros_gracza(Player p, String team) {
        UUID uuid = p.getUniqueId();
        if (data.getData().getString(team + ".lider").equals(p.getDisplayName())) {
            p.sendMessage("§fNapisz nazwe gracza na czacie §7(§930s§7)");
            data.getData().set(uuid + ".zapraszanie.aktywne", true);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    if (data.getData().getString(uuid + ".zapraszanie.aktywne") != null) {
                        data.getData().set(uuid + ".zapraszanie", null);
                        try {
                            data.saveData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        p.sendMessage("§7Twój czas na napisanie nazwy minąl");
                    }
                }
            }, 30 * 20);
        }
    }

    @EventHandler
    public void OnChatZapro(AsyncPlayerChatEvent e) throws IOException {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (data.getData().getString(uuid + ".zapraszanie.aktywne") != null) {
            String messange = e.getMessage();
            Player cel = Bukkit.getPlayer(messange);
            if (cel != null) {
                UUID celuuid = cel.getUniqueId();
                if (data.getData().getString(celuuid + ".lider_teamu") == null && data.getData().getString(celuuid + ".gracz") == null) {
                    String team = data.getData().getString(uuid + ".lider_teamu");
                    data.getData().set(uuid + ".zapraszanie", null);
                    data.getData().set(uuid + ".create_team.zapraszanie", null);
                    data.getData().set(celuuid + ".zaproszenie", team);
                    data.saveData();
                    p.sendMessage("§7Pomyślnie zaproszono gracza §9" + cel.getDisplayName() + " §7do teamu");
                    cel.sendMessage("§fOtrzymaleś zaproszenie do teamu §7(aby dolaczyć użyj /team)");
                    e.setCancelled(true);
                } else {
                    p.sendMessage("§cGracz jest już w team");
                    data.getData().set(uuid + ".zapraszanie", null);
                    data.getData().set(uuid + ".create_team.zapraszanie", null);
                    data.saveData();
                    e.setCancelled(true);
                }
            } else {
                p.sendMessage("§cGracz jest offline");
                data.getData().set(uuid + ".zapraszanie", null);
                data.getData().set(uuid + ".create_team.zapraszanie", null);
                data.saveData();
                e.setCancelled(true);
            }
        }
    }

    public void usun_team(Player p, String team) {
        if (data.getData().getString(team + ".lider").equals(p.getDisplayName())) {
            Inventory inventory = Bukkit.createInventory((InventoryHolder) p, 27, ("§9§lPOTWIERDŹ USUNIECIE TEAMU"));

            ItemBuilder slot12 = (new ItemBuilder(Material.LIME_CONCRETE, 1)).setTitle("§a§lPotwierdzam").addLore("§7").addLore("§fKliknięcie spowoduje usuniecie teamu");
            inventory.setItem(12, slot12.build());

            ItemBuilder slot14 = (new ItemBuilder(Material.RED_CONCRETE, 1)).setTitle("§c§lAnuluj").addLore("§7").addLore("§fKliknięcie spowoduje anulowanie akcji");
            inventory.setItem(14, slot14.build());

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
            inventory.setItem(11, backguard.build());
            inventory.setItem(13, backguard.build());
            inventory.setItem(15, backguard.build());
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
            p.openInventory(inventory);
        }
    }

    public void create_team(Player p) {
        UUID uuid = p.getUniqueId();
        if (data.getData().getString(uuid + ".lider_teamu") == null && data.getData().getString(uuid + ".gracz") == null) {
            p.sendMessage("§fNapisz nazwe swojego teamu na czacie §7(§930s§7)");
            data.getData().set(uuid + ".create_team.aktywne_nazywanie", true);
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    if (data.getData().getString(uuid + ".create_team.aktywne_nazywanie") != null) {
                        data.getData().set(uuid + ".create_team", null);
                        try {
                            data.saveData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        p.sendMessage("§7Twój czas na napisanie nazwy minąl");
                    }
                }
            }, 30 * 20);
        } else {
            if (data.getData().getString(uuid + ".lider_teamu") != null) {
                String team = data.getData().getString(uuid + ".lider_teamu");
                lider_team(p, team);
            } else {
                if (data.getData().getString(uuid + ".gracz") != null) {
                    String team = data.getData().getString(uuid + ".gracz");
                    gracz_team(p, team);
                }
            }
        }
    }

    public void lider_team(Player p, String team) {
        if (data.getData().getString(team + ".lider").equals(p.getDisplayName())) {
            String lider = data.getData().getString(team + ".lider");
            List gracze = data.getData().getStringList(team + ".gracze");
            String datapowstania = data.getData().getString(team + ".data");

            Inventory inventory = Bukkit.createInventory((InventoryHolder) p, 27, ("§9§lZARZADZAJ TEAM"));

            ItemBuilder slot11 = (new ItemBuilder(Material.PAPER, 1)).setTitle("§e§lInformacje o team").addLore("§7").addLore("§fLider teamu: §9" + lider).addLore("§fData powstania: §9" + datapowstania).addLore("§7").addLore("§7Więcej informacji wkrótce");
            inventory.setItem(11, slot11.build());

            ItemBuilder slot13 = (new ItemBuilder(Material.CHEST, 1)).setTitle("§e§lOsoby należące do teamu").addLore("§7").addLore("§fLider: §9" + lider).addLore("§fGracze: §9" + gracze);
            inventory.setItem(13, slot13.build());

            ItemBuilder slot15 = (new ItemBuilder(Material.TRIPWIRE_HOOK, 1)).setTitle("§e§lZaproś osoby do teamu").addLore("§7").addLore("§7Kliknij, aby zaprosić osoby do teamu");
            inventory.setItem(15, slot15.build());

            ItemBuilder slot26 = (new ItemBuilder(Material.RED_CONCRETE, 1)).setTitle("§c§lUsuń team").addLore("§7").addLore("§7Kliknij, aby usunąć team");
            inventory.setItem(26, slot26.build());

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
            p.openInventory(inventory);
        } else {
            p.sendMessage("§cWystąpil blad podczas weryfikacji lidera");
        }
    }

    public void gracz_team(Player p, String team) {
        String lider = data.getData().getString(team + ".lider");
        List gracze = data.getData().getStringList(team + ".gracze");
        String datapowstania = data.getData().getString(team + ".data");

        Inventory inventory = Bukkit.createInventory((InventoryHolder) p, 27, ("§9§lTWÓJ TEAM"));

        ItemBuilder slot12 = (new ItemBuilder(Material.PAPER, 1)).setTitle("§e§lInformacje o team").addLore("§7").addLore("§fLider teamu: §9" + lider).addLore("§fData powstania: §9" + datapowstania).addLore("§7").addLore("§7Więcej informacji wkrótce");
        inventory.setItem(12, slot12.build());

        ItemBuilder slot14 = (new ItemBuilder(Material.CHEST, 1)).setTitle("§e§lOsoby należące do teamu").addLore("§7").addLore("§fLider: §9" + lider).addLore("§fGracze: §9" + gracze);
        inventory.setItem(14, slot14.build());

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
        inventory.setItem(11, backguard.build());
        inventory.setItem(13, backguard.build());
        inventory.setItem(15, backguard.build());
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
        p.openInventory(inventory);
    }

    @EventHandler
    public void OnChat(AsyncPlayerChatEvent e) throws IOException {
        Player p = e.getPlayer();
        UUID uuid = p.getUniqueId();
        if (data.getData().getString(uuid + ".create_team.aktywne_nazywanie") != null) {
            String characters = plugin.getConfig().getString("allowedteamname");
            String messange = e.getMessage();
            if (messange.matches(characters)) {
                if (messange.length() > 2 && messange.length() < 17) {
                    List<String> teamy = data.getData().getStringList("teamy");
                    Boolean check = true;
                    for (String team : teamy) {
                        if (team.toLowerCase().equals(messange.toLowerCase())) {
                            check = false;
                        }
                    }
                    if (check) {
                        Date now = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
                        data.getData().set(uuid + ".create_team", null);
                        data.getData().set(messange + ".lider", p.getDisplayName());
                        data.getData().set(messange + ".data", format.format(now));
                        ArrayList<String> gracze = new ArrayList<>();
                        gracze.add(p.getDisplayName());
                        data.getData().set(messange + ".gracze", gracze);
                        data.getData().set(uuid + ".lider_teamu", messange);
                        teamy.add(messange);
                        data.getData().set("teamy", teamy);
                        data.saveData();
                        p.sendMessage("§7Twoja nazwa teamu §7to: §9" + messange);
                        e.setCancelled(true);
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            players.sendMessage("§7Gracz §9" + p.getDisplayName() + " §7stworzyl team: §9" + messange);
                        }
                    } else {
                        p.sendMessage("§cTaka nazwa teamu już istnieje");
                        data.getData().set(uuid + ".create_team", null);
                        data.getData().set(uuid + ".create_team.aktywne_nazywanie", null);
                        data.saveData();
                        e.setCancelled(true);
                    }
                } else {
                    p.sendMessage("§cNazwa teamu musi miec minimum 3 znaki, maksymalnie 16 znaków");
                    data.getData().set(uuid + ".create_team", null);
                    data.getData().set(uuid + ".create_team.aktywne_nazywanie", null);
                    data.saveData();
                    e.setCancelled(true);
                }
            } else {
                p.sendMessage("§cNazwa teamu posiada niedozwolone znaki");
                data.getData().set(uuid + ".create_team", null);
                data.getData().set(uuid + ".create_team.aktywne_nazywanie", null);
                data.saveData();
                e.setCancelled(true);
            }
        }
    }

    public void lista_teamow(Player p) throws IOException {

        int ile_slotow = 0;
        if (data.getData().getString("teamy") == null || data.getData().getStringList("teamy").size() == 0) {
            p.sendMessage("§7Obecnie nie ma żadnych teamów");
            return;
        } else {
            int liczba = data.getData().getStringList("teamy").size();
            if (liczba < 10) {
                ile_slotow = 9;
            } else {
                if (liczba > 9 && liczba < 19) {
                    ile_slotow = 18;
                } else {
                    if (liczba > 18 && liczba < 28) {
                        ile_slotow = 27;
                    } else {
                        if (liczba > 27 && liczba < 37) {
                            ile_slotow = 36;
                        } else {
                            if (liczba > 36 && liczba < 46) {
                                ile_slotow = 45;
                            } else {
                                if (liczba > 45 && liczba < 55) {
                                    ile_slotow = 54;
                                } else {
                                    p.sendMessage("§7Jest zbyt wiele teamów, przez co nie można ich wyświetlić");
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }

        Inventory inventory = Bukkit.createInventory((InventoryHolder) p, ile_slotow, ("§9§lLISTA TEAMÓW"));
        List<String> teamy = data.getData().getStringList("teamy");
        int id = 0;
        for (String team : teamy) {
            create_slot(team, id, inventory);
            id += 1;
        }

        p.openInventory(inventory);
    }

    public static void create_slot(String team, int id, Inventory inventory) {
        String lider = data.getData().getString(team + ".lider");
        List gracze = data.getData().getStringList(team + ".gracze");
        ItemBuilder slot = (new ItemBuilder(Material.CHEST, 1)).setTitle("§b§l" + team).addLore("§7").addLore("§fLider: §9" + lider).addLore("§fGracze: §9" + gracze);
        inventory.setItem(id, slot.build());
    }

}
