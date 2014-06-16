package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.Rel;
import com.github.nowireless.factions.cmd.req.ReqFactionsEnabled;
import com.github.nowireless.factions.cmd.req.ReqHasFaction;
import com.github.nowireless.factions.cmd.req.ReqRoleIsAtLeast;
import com.github.nowireless.factions.event.FactionsEventDescriptionChange;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.mixin.Mixin;

public class CmdFactionsDescription extends FCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDescription()
	{
		// Aliases
		this.addAliases("desc");

		// Args
		this.addRequiredArg("desc");
		this.setErrorOnToManyArgs(false);

		// Requirements
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.DESCRIPTION.node));
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
		String newDescription = this.argConcatFrom(0);
		
		// Event
		FactionsEventDescriptionChange event = new FactionsEventDescriptionChange(sender, usenderFaction, newDescription);
		event.run();
		if (event.isCancelled()) return;
		newDescription = event.getNewDescription();

		// Apply
		usenderFaction.setDescription(newDescription);
		
		// Inform
		usenderFaction.msg("<i>%s <i>set your faction description to:\n%s", Mixin.getDisplayName(sender), usenderFaction.getDescription());
	}
	
}
