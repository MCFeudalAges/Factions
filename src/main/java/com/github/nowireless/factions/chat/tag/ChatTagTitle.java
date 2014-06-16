package com.github.nowireless.factions.chat.tag;

import org.bukkit.command.CommandSender;

import com.github.nowireless.factions.chat.ChatTagAbstract;
import com.github.nowireless.factions.entity.UConf;
import com.github.nowireless.factions.entity.UPlayer;

public class ChatTagTitle extends ChatTagAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatTagTitle() { super("factions_title"); }
	private static ChatTagTitle i = new ChatTagTitle();
	public static ChatTagTitle get() { return i; }
	
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
		
		if (!usender.hasTitle()) return "";
		return usender.getTitle();
	}

}
