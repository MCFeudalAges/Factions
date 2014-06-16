package com.github.nowireless.factions.chat.tag;

import org.bukkit.command.CommandSender;

import com.github.nowireless.factions.chat.ChatTagAbstract;
import com.github.nowireless.factions.entity.UConf;
import com.github.nowireless.factions.entity.UPlayer;
import com.massivecraft.massivecore.util.Txt;

public class ChatTagRole extends ChatTagAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatTagRole() { super("factions_role"); }
	private static ChatTagRole i = new ChatTagRole();
	public static ChatTagRole get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getReplacement(CommandSender sender, CommandSender recipient)
	{
		// Check disabled
		if (UConf.isDisabled(sender)) return "";
		
		// Get entities
		UPlayer usender = UPlayer.get(sender);
		
		return Txt.upperCaseFirst(usender.getRole().toString().toLowerCase());
	}

}
