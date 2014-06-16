package com.github.nowireless.factions.chat.modifier;

import org.bukkit.command.CommandSender;

import com.github.nowireless.factions.chat.ChatModifierAbstract;


public class ChatModifierLp extends ChatModifierAbstract
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private ChatModifierLp() { super("lp"); }
	private static ChatModifierLp i = new ChatModifierLp();
	public static ChatModifierLp get() { return i; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //

	@Override
	public String getModified(String subject, CommandSender sender, CommandSender recipient)
	{
		if (subject.equals("")) return subject;
		return " "+subject;
	}

}
