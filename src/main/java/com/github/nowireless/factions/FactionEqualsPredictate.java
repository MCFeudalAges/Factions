package com.github.nowireless.factions;

import java.io.Serializable;

import org.bukkit.command.CommandSender;

import com.github.nowireless.factions.entity.Faction;
import com.github.nowireless.factions.entity.UPlayer;
import com.massivecraft.massivecore.Predictate;

public class FactionEqualsPredictate implements Predictate<CommandSender>, Serializable
{
	private static final long serialVersionUID = 1L;
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final String factionId;
	public String getFactionId() { return this.factionId; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public FactionEqualsPredictate(Faction faction)
	{
		this.factionId = faction.getId();
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public boolean apply(CommandSender sender)
	{
		UPlayer uplayer = UPlayer.get(sender);
		return this.factionId.equals(uplayer.getFactionId());
	}

}