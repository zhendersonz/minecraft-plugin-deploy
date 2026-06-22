# Paper API — Padrões e Referência

## Estrutura da Main class

```java
package com.meuplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class MeuPlugin extends JavaPlugin {

    private static MeuPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        registerCommands();
        registerListeners();
        getLogger().info("MeuPlugin ativado!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MeuPlugin desativado!");
    }

    public static MeuPlugin getInstance() {
        return instance;
    }

    private void registerCommands() {
        // getCommand("meucomando").setExecutor(new MeuComando());
    }

    private void registerListeners() {
        // getServer().getPluginManager().registerEvents(new MeuListener(), this);
    }
}
```

## Eventos comuns

```java
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
```

### Listener padrão

```java
package com.meuplugin;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MeuListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("§aBem-vindo!");
    }
}
```

## Comandos

### Classe separada (recomendado)

```java
package com.meuplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeuComando implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            p.sendMessage("§eUse: /meucomando <arg>");
            return true;
        }

        p.sendMessage("§aVocê digitou: " + args[0]);
        return true;
    }
}
```

### TabCompleter

```java
package com.meuplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.List;

public class MeuTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("opcao1", "opcao2", "opcao3");
        }
        return List.of();
    }
}
```

### Registrar ambos no onEnable

```java
getCommand("meucomando").setExecutor(new MeuComando());
getCommand("meucomando").setTabCompleter(new MeuTabCompleter());
```

## Config.yml

```java
// salvar padrão
saveDefaultConfig();

// ler
String valor = getConfig().getString("caminho.chave", "valorPadrao");
int numero = getConfig().getInt("caminho.numero", 10);
boolean bool = getConfig().getBoolean("caminho.flag", false);

// escrever
getConfig().set("caminho.chave", "novoValor");
saveConfig();

// recarregar
reloadConfig();
```

## Mensagens coloridas

```java
// § = & no config.yml
p.sendMessage("§cVermelho §aVerde §eAmarelo");
p.sendMessage(ChatColor.RED + "Vermelho" + ChatColor.GREEN + " Verde");

// Component (moderno)
p.sendActionBar("§aMensagem na barra de ação");
p.sendTitle("§cTítulo", "§7Subtítulo", 10, 70, 20);
```

## Ponteiros uteis do Paper

```java
// Verificar se é jogador
if (sender instanceof Player p) { }

// Localização
Location loc = p.getLocation();
World w = loc.getWorld();
double x = loc.getX(), y = loc.getY(), z = loc.getZ();
float yaw = loc.getYaw(), pitch = loc.getPitch();

// Itens
ItemStack item = p.getInventory().getItemInMainHand();
Material type = item.getType();
if (type == Material.DIAMOND) { }

// Som
p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

// Partículas
p.spawnParticle(Particle.FLAME, loc, 10, 0.5, 0.5, 0.5, 0.01);

// Schedule task (1 tick = 1/20 seg)
new BukkitRunnable() {
    @Override
    public void run() {
        p.sendMessage("§aPassou 1 segundo!");
    }
}.runTaskLater(plugin, 20L); // 20 ticks = 1 segundo

// Task repetitiva
new BukkitRunnable() {
    @Override
    public void run() {
        // executa a cada X ticks
    }
}.runTaskTimer(plugin, 0L, 20L);
```

## Databases

### SQLite

```java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.File;

public class Database {
    private Connection conn;

    public void connect(File dataFolder) {
        try {
            conn = DriverManager.getConnection(
                "jdbc:sqlite:" + new File(dataFolder, "database.db").getAbsolutePath()
            );
            conn.createStatement().execute(
                "CREATE TABLE IF NOT EXISTS dados (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "uuid VARCHAR(36) NOT NULL, " +
                "valor TEXT)"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String uuid, String valor) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO dados (uuid, valor) VALUES (?, ?)")) {
            ps.setString(1, uuid);
            ps.setString(2, valor);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### MySQL

```java
conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/minecraft?useSSL=false",
    "user", "password"
);
```

## PersistentDataContainer (Paper moderno)

```java
// Salvar dados no item (sem lore)
ItemStack item = p.getInventory().getItemInMainHand();
ItemMeta meta = item.getItemMeta();
NamespacedKey key = new NamespacedKey(plugin, "meuplugin_dono");
meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, p.getUniqueId().toString());
item.setItemMeta(meta);

// Ler
String dono = item.getItemMeta()
    .getPersistentDataContainer()
    .get(key, PersistentDataType.STRING);
```
