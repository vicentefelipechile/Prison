package tech.mcprison.prison.spigot.commands;

import org.bukkit.entity.Player;

import tech.mcprison.prison.Prison;
import tech.mcprison.prison.commands.Arg;
import tech.mcprison.prison.commands.Command;
import tech.mcprison.prison.internal.CommandSender;
import tech.mcprison.prison.output.Output;
import tech.mcprison.prison.spigot.SpigotPrison;
import tech.mcprison.prison.spigot.configs.MessagesConfig;
import tech.mcprison.prison.spigot.gui.SpigotPrisonGUI;
import tech.mcprison.prison.spigot.gui.guiutility.SpigotGUIComponents;
import tech.mcprison.prison.spigot.gui.mine.SpigotPlayerMinesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerPrestigesGUI;
import tech.mcprison.prison.spigot.gui.rank.SpigotPlayerRanksGUI;

/**
 * @author GABRYCA
 * @author RoyalBlueRanger (rBluer)
 */
public class PrisonSpigotGUICommands extends PrisonSpigotBaseCommands {

    private final MessagesConfig messages = getMessages();

    /**
     * NOTE: onlyPlayers needs to be false so players can use /gui help on the command, even from console.
     *
     * @param sender
     */
    @Command( identifier = "gui", description = "The Prison's GUI",
            aliases = {"gui admin"},
            altPermissions = {"prison.admin", "prison.prisonmanagergui"},
            onlyPlayers = false
    )
    private void prisonManagerGUI(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        if (player.hasPermission("prison.admin") || player.hasPermission("prison.prisonmanagergui")) {
            SpigotPrisonGUI gui = new SpigotPrisonGUI(player);
            gui.open();
            return;
        }

        sender.dispatchCommand("gui help");
    }

    @Command( identifier = "gui prestiges", description = "GUI Prestiges",
            onlyPlayers = true )
    private void prisonManagerPrestiges( CommandSender sender ) {

        if ( !isPrisonConfig("prestiges") && !isPrisonConfig( "prestige.enabled" ) ) {
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_prestiges_disabled)));
            return;
        }


        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Prestiges.GUI_Enabled")){
            Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_prestiges_or_gui_disabled)));
            return;
        }

        if ( isConfig("Options.Prestiges.Permission_GUI_Enabled") ){
            String perm = getConfig( "Options.Prestiges.Permission_GUI");

            if ( !sender.hasPermission( perm ) ){
                Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" +
                        perm + "]"));
                return;
            }
        }

        Player player = getSpigotPlayer( sender );

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        SpigotPlayerPrestigesGUI gui = new SpigotPlayerPrestigesGUI( player );
        gui.open();
    }

    @Command( identifier = "gui mines", description = "GUI Mines",
            onlyPlayers = true )
    private void prisonManagerMines(CommandSender sender) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        if ( !isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Mines.GUI_Enabled") ){
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_mines_or_gui_disabled)));
            return;
        }


        if ( isConfig("Options.Mines.Permission_GUI_Enabled") ){
            String perm = getConfig( "Options.Mines.Permission_GUI");

            if ( !sender.hasPermission( perm ) ){
                Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" +
                        perm + "]"));
                return;
            }
        }

        SpigotPlayerMinesGUI gui = new SpigotPlayerMinesGUI( player );
        gui.open();
    }

    @Command( identifier = "gui ranks", description = "GUI Ranks",
            onlyPlayers = true )
    private void prisonManagerRanks(CommandSender sender,
    		@Arg(name = "page", description = "If there are more than 45 ranks, then the " +
    				"ranks are shown on multiple pages.  The page parameter starts with " +
    				"page 1.", def = "1" ) int page
    		) {

        Player player = getSpigotPlayer(sender);

        if (player == null) {
            Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_console_error)));
            return;
        }

        if (!isPrisonConfig("prison-gui-enabled") || !isConfig("Options.Ranks.GUI_Enabled")) {
            Output.get().sendInfo(sender, SpigotPrison.format(String.format(String.format(
                    getMessages().getString(MessagesConfig.StringID.spigot_message_ranks_or_gui_disabled),
                    getPrisonConfig("prison-gui-enabled"), getConfig("Options.Ranks.GUI_Enabled") ))));
            return;
        }

        if (isConfig("Options.Ranks.Permission_GUI_Enabled")) {
            String perm = getConfig( "Options.Ranks.Permission_GUI");
            if (!sender.hasPermission(perm)) {

                Output.get().sendInfo(sender, SpigotPrison.format( getMessages().getString(MessagesConfig.StringID.spigot_message_missing_permission) + " [" +
                        perm + "]"));
                return;
            }
        }

        SpigotPlayerRanksGUI gui = new SpigotPlayerRanksGUI( player, page );
        gui.open();
    }

    @Command(identifier = "gui sellall", description = "SellAll GUI command", onlyPlayers = true)
    private void sellAllGuiCommandNew(CommandSender sender){

    	String registeredCmd = Prison.get().getCommandHandler().findRegisteredCommand( "sellall gui" );
        sender.dispatchCommand(registeredCmd);
    }

    // Backpack GUI commands got moved to the Backpacks class so they won't be loaded if backpacks are disabled.

    @Command(identifier = "gui reload", description = "Reload GUIs", permissions = "prison.admin",onlyPlayers = false)
    public void reloadGUICommand(CommandSender sender){
        SpigotGUIComponents.updateMessages();
        SpigotGUIComponents.updateSellAllConfig();
        SpigotGUIComponents.updateGUIConfig();
        Output.get().sendInfo(sender, SpigotPrison.format(messages.getString(MessagesConfig.StringID.spigot_message_gui_reload_success)));
    }
}
