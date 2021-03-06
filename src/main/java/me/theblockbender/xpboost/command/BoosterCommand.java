package me.theblockbender.xpboost.command;

import me.theblockbender.xpboost.Main;
import me.theblockbender.xpboost.util.BoosterType;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BoosterCommand implements CommandExecutor {

    private Main main;

    public BoosterCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean isPlayer = false;
        if (sender instanceof Player) {
            isPlayer = true;
        }

        if (args.length == 0) {
            if (isPlayer) {
                if (!sender.hasPermission("xpboost.player")) {
                    sender.sendMessage(main.getMessage("command-no-permission"));
                    return true;
                }
                main.boostergui.open((Player) sender);
                return true;
            } else {
                sendHelp(sender, label);
                return true;
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("boost")) {
                if (!isPlayer) {
                    sender.sendMessage(main.getMessage("command-from-console"));
                    return true;
                }
                if (!sender.hasPermission("xpboost.admin")) {
                    sender.sendMessage(main.getMessage("command-no-permission"));
                    return true;
                }
                BoosterType type = null;
                if (args[1].equalsIgnoreCase("minecraft")) {
                    type = BoosterType.MINECRAFT;
                    if (!main.isBoosterEnabled(BoosterType.MINECRAFT)) {
                        sender.sendMessage(main.getMessage("event-booster-disabled"));
                        return true;
                    }
                }
                if (args[1].equalsIgnoreCase("skillapi")) {
                    type = BoosterType.SKILLAPI;
                    if (!main.isBoosterEnabled(BoosterType.SKILLAPI)) {
                        sender.sendMessage(main.getMessage("event-booster-disabled"));
                        return true;
                    }
                }
                if (args[1].equalsIgnoreCase("mcmmo")) {
                    type = BoosterType.MCMMO;
                    if (!main.isBoosterEnabled(BoosterType.MCMMO)) {
                        sender.sendMessage(main.getMessage("event-booster-disabled"));
                        return true;
                    }
                }
                // Future add argument-type here
                if (args[1].equalsIgnoreCase("jobs")) {
                    type = BoosterType.JOBS;
                    if (!main.isBoosterEnabled(BoosterType.JOBS)) {
                        sender.sendMessage(main.getMessage("event-booster-disabled"));
                        return true;
                    }
                }
                if (type == null) {
                    sender.sendMessage(main.getMessage("command-invalid-type"));
                    // Future add type here
                    sender.sendMessage(main.getMessage("command-available-types"));
                    return true;
                }
                main.cmdActivate((Player) sender, type);
                return true;
            }
        }
        if (args.length < 3 || args.length > 4) {
            sendHelp(sender, label);
            return true;
        }
        String subcommand = args[0].toLowerCase();
        Player player = null;
        if (subcommand.equalsIgnoreCase("give") || subcommand.equalsIgnoreCase("take") || subcommand.equalsIgnoreCase("reset")) {
            player = Bukkit.getPlayer(args[1]);
            if (player == null) {
                sender.sendMessage(main.getMessage("command-player-not-online").replace("{player}", args[1]));
                return true;
            }
        }
        BoosterType type = null;
        if (args[2].equalsIgnoreCase("minecraft")) {
            type = BoosterType.MINECRAFT;
            if (!main.isBoosterEnabled(BoosterType.MINECRAFT)) {
                sender.sendMessage(main.getMessage("event-booster-disabled"));
                return true;
            }
        }
        if (args[2].equalsIgnoreCase("skillapi")) {
            type = BoosterType.SKILLAPI;
            if (!main.isBoosterEnabled(BoosterType.SKILLAPI)) {
                sender.sendMessage(main.getMessage("event-booster-disabled"));
                return true;
            }
        }
        if (args[2].equalsIgnoreCase("mcmmo")) {
            type = BoosterType.MCMMO;
            if (!main.isBoosterEnabled(BoosterType.MCMMO)) {
                sender.sendMessage(main.getMessage("event-booster-disabled"));
                return true;
            }
        }
        // Future add argument-type here
        if (args[2].equalsIgnoreCase("jobs")) {
            type = BoosterType.JOBS;
            if (!main.isBoosterEnabled(BoosterType.JOBS)) {
                sender.sendMessage(main.getMessage("event-booster-disabled"));
                return true;
            }
        }
        if (type == null) {
            sender.sendMessage(main.getMessage("command-invalid-type"));
            // Future add type here
            sender.sendMessage(main.getMessage("command-available-types"));
            return true;
        }

        if (args.length == 3) {
            if (subcommand.equalsIgnoreCase("give")) {
                if (!sender.hasPermission("xpboost.admin")) {
                    sender.sendMessage(main.getMessage("command-no-permission"));
                    return true;
                }
                main.addBooster(player, 1, sender, type);
                return true;
            }
            if (subcommand.equalsIgnoreCase("take")) {
                if (!sender.hasPermission("xpboost.admin")) {
                    sender.sendMessage(main.getMessage("command-no-permission"));
                    return true;
                }
                main.takeBooster(player, 1, sender, type);
                return true;
            }
            if (subcommand.equalsIgnoreCase("reset")) {
                if (!sender.hasPermission("xpboost.admin")) {
                    sender.sendMessage(main.getMessage("command-no-permission"));
                    return true;
                }
                main.resetBooster(player, sender, type);
                return true;
            }
            sendHelp(sender, label);
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[3]);
        } catch (NumberFormatException ex) {
            sender.sendMessage(main.getMessage("command-invalid-amount").replace("{amount}", args[3]));
            return true;
        }
        if (amount < 1 || amount > 100) {
            sender.sendMessage(main.getMessage("command-number-to-weird"));
            return true;
        }
        if (subcommand.equalsIgnoreCase("give")) {
            if (!sender.hasPermission("xpboost.admin")) {
                sender.sendMessage(main.getMessage("command-no-permission"));
                return true;
            }
            main.addBooster(player, amount, sender, type);
            return true;
        }
        if (subcommand.equalsIgnoreCase("take")) {
            if (!sender.hasPermission("xpboost.admin")) {
                sender.sendMessage(main.getMessage("command-no-permission"));
                return true;
            }
            main.takeBooster(player, amount, sender, type);
            return true;
        }
        sendHelp(sender, label);
        return true;
    }

    private void sendHelp(CommandSender sender, String label) {
        sender.sendMessage(main.getMessage("help-divider"));
        sender.sendMessage(main.getMessage("help-header"));
        sender.sendMessage(main.getMessage("help-gui").replace("{cmd}", label));
        sender.sendMessage(main.getMessage("help-give").replace("{cmd}", label));
        sender.sendMessage(main.getMessage("help-take").replace("{cmd}", label));
        sender.sendMessage(main.getMessage("help-reset").replace("{cmd}", label));
        sender.sendMessage(main.getMessage("help-start-booster").replace("{cmd}", label));
        sender.sendMessage(main.getMessage("help-divider"));
    }

}
