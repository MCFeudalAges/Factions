package com.github.nowireless.factions.chat.modifier;

import org.bukkit.command.CommandSender;

import com.github.nowireless.factions.chat.ChatModifierAbstract;

public class ChatModifierRp extends ChatModifierAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatModifierRp() { super("rp"); }
	private static ChatModifierRp i = new ChatModifierRp();
	public static ChatModifierRp get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getModified(String subject, CommandSender sender, CommandSender recipient)
	{
		if (subject.equals("")) return subject;
		return subject+" ";
	}

}
