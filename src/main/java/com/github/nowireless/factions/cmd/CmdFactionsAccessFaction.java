package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.FPerm;
import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.cmd.arg.ARFaction;
//import com.massivecraft.factions.entity.BoardColls;
import com.github.nowireless.factions.entity.Faction;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdFactionsAccessFaction extends CmdFactionsAccessAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsAccessFaction()
	{
		// Aliases
		this.addAliases("f", "faction");
		
		// Args
		this.addRequiredArg("faction");
		this.addOptionalArg("yes/no", "toggle");

		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.ACCESS_FACTION.node));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void innerPerform()
	{
		// Args
		Faction faction = this.arg(0, ARFaction.get(usender));
		if (faction == null) return;
		
		Boolean newValue = this.arg(1, ARBoolean.get(), !ta.isFactionIdGranted(faction.getId()));
		if (newValue == null) return;
		
		// FPerm
		if (!FPerm.ACCESS.has(usender, hostFaction, true)) return;
		
		// Apply
		ta = ta.withFactionId(faction.getId(), newValue);
		//BoardColls.get().setTerritoryAccessAt(chunk, ta);
		
		// Inform
		this.sendAccessInfo();
	}
	
}
