package com.github.nowireless.factions.chat.tag;

import org.bukkit.command.CommandSender;

import com.github.nowireless.factions.chat.ChatTagAbstract;
import com.github.nowireless.factions.entity.UConf;
import com.github.nowireless.factions.entity.UPlayer;

public class ChatTagRelcolor extends ChatTagAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatTagRelcolor() { super("factions_relcolor"); }
	private static ChatTagRelcolor i = new ChatTagRelcolor();
	public static ChatTagRelcolor get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getReplacement(CommandSender sender, CommandSender recipient)
	{
		// Check disabled
		if (UConf.isDisabled(sender)) return "";
		
		// Opt out if no recipient
		if (recipient == null) return null;

		// Get entities
		UPlayer usender = UPlayer.get(sender);
		UPlayer urecipient = UPlayer.get(recipient);
		
		return urecipient.getRelationTo(usender).getColor().toString();
	}

}
