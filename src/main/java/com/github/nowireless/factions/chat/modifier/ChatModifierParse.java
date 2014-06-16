package com.github.nowireless.factions.chat.modifier;

import org.bukkit.command.CommandSender;

import com.github.nowireless.factions.chat.ChatModifierAbstract;
import com.massivecraft.massivecore.util.Txt;

public class ChatModifierParse extends ChatModifierAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatModifierParse() { super("parse"); }
	private static ChatModifierParse i = new ChatModifierParse();
	public static ChatModifierParse get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getModified(String subject, CommandSender sender, CommandSender recipient)
	{
		return Txt.parse(subject);
	}

}
