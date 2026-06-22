package com.{{groupId}}.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class {{mainCommandClass}} implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cApenas jogadores podem usar este comando.");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage("§eUse /{{commandName}} help");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "help" -> p.sendMessage("§eComandos disponiveis...");
            case "reload" -> {
                if (p.hasPermission("{{permissionBase}}.reload")) {
                    reloadConfig();
                    p.sendMessage("§aConfig recarregada!");
                } else {
                    p.sendMessage("§cSem permissao.");
                }
            }
            default -> p.sendMessage("§cComando desconhecido.");
        }

        return true;
    }
}
