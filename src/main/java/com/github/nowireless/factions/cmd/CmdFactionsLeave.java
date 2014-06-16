package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.cmd.req.ReqFactionsEnabled;
import com.github.nowireless.factions.cmd.req.ReqHasFaction;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdFactionsLeave extends FCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsLeave()
	{
		// Aliases
		this.addAliases("leave");

		// Requirements
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.LEAVE.node));
		this.addRequirements(ReqHasFaction.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		usender.leave();
	}
	
}
