package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.Rel;
import com.github.nowireless.factions.cmd.req.ReqFactionsEnabled;
import com.github.nowireless.factions.cmd.req.ReqHasFaction;
import com.github.nowireless.factions.cmd.req.ReqRoleIsAtLeast;
import com.github.nowireless.factions.event.FactionsEventOpenChange;
import com.massivecraft.massivecore.cmd.arg.ARBoolean;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdFactionsOpen extends FCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsOpen()
	{
		// Aliases
		this.addAliases("open");

		// Args
		this.addOptionalArg("yes/no", "toggle");

		// Requirements
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.OPEN.node));
		this.addRequirements(ReqHasFaction.get());
		this.addRequirements(ReqRoleIsAtLeast.get(Rel.OFFICER));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		Boolean newOpen = this.arg(0, ARBoolean.get(), !usenderFaction.isOpen());
		if (newOpen == null) return;

		// Event
		FactionsEventOpenChange event = new FactionsEventOpenChange(sender, usenderFaction, newOpen);
		event.run();
		if (event.isCancelled()) return;
		newOpen = event.isNewOpen();
		
		// Apply
		usenderFaction.setOpen(newOpen);
		
		// Inform
		String descTarget = usenderFaction.isOpen() ? "open" : "closed";
		usenderFaction.msg("%s<i> changed the faction to <h>%s<i>.", usender.describeTo(usenderFaction, true), descTarget);
	}
	
}
