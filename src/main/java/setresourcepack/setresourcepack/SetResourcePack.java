package setresourcepack.setresourcepack;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.*;

public final class SetResourcePack extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this,this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) throws IOException {
        event.getPlayer().setResourcePack(getLink());
        event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 10 * 20, 20));

    }

    public String getLink() throws IOException {
        String currentDirectory;
        File file = new File("ResourcePackPath.txt");
        currentDirectory = file.getAbsolutePath();
        File resourcePack = new File(currentDirectory);
        BufferedReader br = new BufferedReader(new FileReader(resourcePack));
        String string = br.readLine();
        br.close();
        return string;
    }

    public File getFile() {
        String currentDirectory;
        File file = new File("ResourcePackPath.txt");
        currentDirectory = file.getAbsolutePath();
        return new File(currentDirectory);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("rp")){
            if(args.length == 1 && args[0].equalsIgnoreCase("reload") && sender instanceof Player){
                Player player = (Player) sender;
                try {
                    player.setResourcePack(getLink());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5 * 20, 20));
            }
            if(args.length == 2 && args[0].equalsIgnoreCase("set")){
                try {
                    File tempFile = new File("tempfile.txt");
                    File inputFile = getFile();
                    BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
                    writer.write(args[1]);
                    tempFile.renameTo(inputFile);
                    writer.close();
                    sender.sendMessage("Resource Pack set!");
                } catch (IOException e) {
                    sender.sendMessage("Error: Resource Pack Link is invalid");
                    e.printStackTrace();
                }
            }
            if(args.length == 1 && args[0].equalsIgnoreCase("help")){
                sender.sendMessage("Mit /rp set 'link' kannst du den Link anpassen");
                sender.sendMessage("Mit /rp reload wird das rp angewendet");
            }
        }
        return true;
    }

    @EventHandler
    public void resourcePackInstall(PlayerResourcePackStatusEvent event){
        if(event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED){
            event.getPlayer().kickPlayer("You must install the resource pack!");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
