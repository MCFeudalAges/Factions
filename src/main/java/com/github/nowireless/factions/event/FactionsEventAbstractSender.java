package com.github.nowireless.factions.event;

import org.bukkit.command.CommandSender;

import com.github.nowireless.factions.entity.UPlayer;
import com.massivecraft.massivecore.event.EventMassiveCore;

public abstract class FactionsEventAbstractSender extends EventMassiveCore
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final CommandSender sender;
	public CommandSender getSender() { return this.sender; }
	public UPlayer getUSender() { return this.sender == null ? null : UPlayer.get(this.sender); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FactionsEventAbstractSender(CommandSender sender)
	{
		this.sender = sender;
	}
}
