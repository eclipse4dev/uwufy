package dev.remy.uwufy;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class Uwufy extends JavaPlugin implements Listener {

    private final String[] textEndings = {"~", " uwu", " owo", " >u<", " :3", " hehe~", " *blushes*"};

    private boolean isUwufyEnabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        isUwufyEnabled = getConfig().getBoolean("uwufy-enabled", true);
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("uwutoggle").setExecutor(this);
        getLogger().info("uwufy loaded!");
    }

    @Override
    public void onDisable() {}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("uwutoggle")) {

            if (!sender.isOp()) {
                sender.sendMessage(Component.text("you have no perms for this :<").color(NamedTextColor.RED));
                return true;
            }

            isUwufyEnabled = !isUwufyEnabled;

            getConfig().set("uwufy-enabled", isUwufyEnabled);
            saveConfig();
            if (isUwufyEnabled) {
                sender.sendMessage(Component.text("uwu-mode is on >:3").color(NamedTextColor.GREEN));
            } else {
                sender.sendMessage(Component.text("uwu-mode is off <:(").color(NamedTextColor.AQUA));
            }

            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (!isUwufyEnabled) return;

        // normal message :(
        String originalText = PlainTextComponentSerializer.plainText().serialize(event.message());

        String uwuText = originalText.replaceAll("(?i)you", "chu")
                .replaceAll("(?i)ove", "uv")
                .replaceAll("(?i)th", "d");

        uwuText = uwuText
                .replaceAll("n([aeiou])", "ny$1")
                .replaceAll("N([aeiou])", "Ny$1")
                .replaceAll("N([AEIOU])", "Ny$1");

        uwuText = uwuText
                .replace("r", "w").replace("R", "W")
                .replace("l", "w").replace("L", "W");

        String[] words = uwuText.split(" ");
        StringBuilder stutteredText = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0 && Character.isLetter(word.charAt(0))) {
                if (ThreadLocalRandom.current().nextInt(100) < 15) {
                    word = word.charAt(0) + "-" + word;
                }
            }
            stutteredText.append(word).append(" ");
        }

        uwuText = stutteredText.toString().trim();

        // adding random endings >:}
        if (ThreadLocalRandom.current().nextBoolean()) {
            int randomIndex = ThreadLocalRandom.current().nextInt(textEndings.length);
            String randomEnding = textEndings[randomIndex];
            uwuText = uwuText + randomEnding;
        }

        // uwufy'd :3
        event.message(Component.text(uwuText));
    }
}