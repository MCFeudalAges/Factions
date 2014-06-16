package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.FPerm;
import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.cmd.arg.ARUPlayer;
//import com.massivecraft.factions.entity.BoardColls;
import com.github.nowireless.factions.entity.UPlayer;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdFactionsAccessPlayer extends CmdFactionsAccessAbstract
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsAccessPlayer()
	{
		// Aliases
		this.addAliases("p", "player");

		// Args
		this.addRequiredArg("player");
		this.addOptionalArg("yes/no", "toggle");

		// Requirements
		this.addRequirements(ReqHasPerm.get(Perm.ACCESS_PLAYER.node));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void innerPerform()
	{
		// Args
		UPlayer uplayer = this.arg(0, ARUPlayer.getStartAny(usender));
		if (uplayer == null) return;
		
		Boolean newValue = this.arg(1, ARBoolean.get(), !ta.isPlayerIdGranted(uplayer.getId()));
		if (newValue == null) return;
		
		// FPerm
		if (!FPerm.ACCESS.has(usender, hostFaction, true)) return;
		
		// Apply
		ta = ta.withPlayerId(uplayer.getId(), newValue);
		//BoardColls.get().setTerritoryAccessAt(chunk, ta);
		
		// Inform
		this.sendAccessInfo();
	}
	
}
