# Paper API — Padrões e Referência

## Stack moderno (Paper 1.21+)

| Componente | Recomendação |
|------------|--------------|
| **API** | `io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT` |
| **Mensagens** | Adventure API + MiniMessage (preferencial) |
| **Comandos** | `CommandExecutor` ou Cloud Command Framework |
| **Logger** | SLF4J (`org.slf4j.Logger`) |
| **Java** | 21+ |
| **Testes** | JUnit 5 + MockBukkit |

## Adventure API / MiniMessage (recomendado)

```java
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.format.NamedTextColor;

// MiniMessage (mais legível)
var mm = MiniMessage.miniMessage();
Component msg = mm.deserialize("<gradient:gold:yellow>Bem-vindo!</gradient>");
player.sendMessage(msg);

// Component builder
Component texto = Component.text("Jogador: ", NamedTextColor.GRAY)
    .append(Component.text(player.getName(), NamedTextColor.GREEN));
player.sendMessage(texto);

// Ações
player.sendActionBar(Component.text("Ação rápida!", NamedTextColor.AQUA));
player.sendMessage(Component.text("§cAinda funciona mas prefira Adventure"));
```

## Estrutura da Main class

```java
package com.meuplugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeuPlugin extends JavaPlugin {

    private static MeuPlugin instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(MeuPlugin.class);

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        registerCommands();
        registerListeners();
        LOGGER.info("MeuPlugin v{} ativado!", getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        LOGGER.info("MeuPlugin desativado!");
    }

    public static MeuPlugin getInstance() {
        return instance;
    }

    private void registerCommands() {
        var cmd = getCommand("meucomando");
        if (cmd != null) cmd.setExecutor(new MeuComando());
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new MeuListener(), this);
    }
}
```

## Eventos

### Listeners

```java
package com.meuplugin.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MeuListener implements Listener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeuListener.class);

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("§aBem-vindo!");
        LOGGER.debug("Player {} entrou no servidor", event.getPlayer().getName());
    }
}
```

### Eventos comuns

```java
PlayerJoinEvent, PlayerQuitEvent, PlayerInteractEvent, PlayerMoveEvent,
BlockPlaceEvent, BlockBreakEvent, EntityDeathEvent, PlayerDeathEvent,
InventoryClickEvent, InventoryOpenEvent
```

## Comandos

```java
package com.meuplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MeuComando implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cApenas jogadores.");
            return true;
        }
        if (args.length == 0) {
            p.sendMessage("§eUse /meucomando help");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "help" -> p.sendMessage("§eComandos disponiveis...");
            default -> p.sendMessage("§cComando desconhecido.");
        }
        return true;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MeuComando.class);
}
```

### TabCompleter

```java
package com.meuplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import java.util.List;

public class MeuTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 1) return List.of("help", "reload");
        return List.of();
    }
}
```

### Registrar no onEnable

```java
var cmd = getCommand("meucomando");
if (cmd != null) {
    cmd.setExecutor(new MeuComando());
    cmd.setTabCompleter(new MeuTabCompleter());
}
```

## Config.yml

```java
// Salvar padrão
saveDefaultConfig();

// Ler
String valor = getConfig().getString("caminho.chave", "valorPadrao");
int numero = getConfig().getInt("caminho.numero", 10);
boolean bool = getConfig().getBoolean("caminho.flag", false);

// Escrever
getConfig().set("caminho.chave", "novoValor");
saveConfig();

// Recarregar
reloadConfig();
```

## Logger (SLF4J - recomendado sobre getLogger())

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger LOGGER = LoggerFactory.getLogger(MinhaClasse.class);

// Com placeholders (evita concatenação)
LOGGER.info("Player {} executou comando {}", player.getName(), label);
LOGGER.warn("Configuração ausente: {}", path);
LOGGER.error("Erro ao processar {}", data, exception); // stack trace incluso
```

## Ponteiros úteis

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

// Som
p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

// Partículas
p.spawnParticle(Particle.FLAME, loc, 10, 0.5, 0.5, 0.5, 0.01);

// Task (1 tick = 1/20 seg)
new BukkitRunnable() {
    @Override
    public void run() {
        p.sendMessage("§aPassou 1 segundo!");
    }
}.runTaskLater(plugin, 20L);

// Task repetitiva
new BukkitRunnable() {
    @Override
    public void run() { }
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

## PersistentDataContainer

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

## Boas práticas

- Prefira Adventure API sobre `§`/`ChatColor`
- Use SLF4J em vez de `getLogger()`/`System.out`/`e.printStackTrace()`
- Valide configuração no `onEnable()`, desabilite o plugin se inválido
- Use try-with-resources para conexões e streams
- Nunca chame Bukkit API de threads assíncronas
- UUIDs em vez de referências `Player` em coleções
- Prepared statements — nunca concatenação de SQL
- Adicione `api-version` no plugin.yml
