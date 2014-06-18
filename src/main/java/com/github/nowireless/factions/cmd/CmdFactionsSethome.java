package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.FPerm;
import com.github.nowireless.factions.Factions;
import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.cmd.arg.ARFaction;
import com.github.nowireless.factions.cmd.req.ReqFactionsEnabled;
import com.github.nowireless.factions.entity.Faction;
import com.github.nowireless.factions.entity.UConf;
import com.github.nowireless.factions.event.FactionsEventHomeChange;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.cmd.req.ReqIsPlayer;
import com.massivecraft.massivecore.ps.PS;

public class CmdFactionsSethome extends FCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsSethome()
	{
		// Aliases
		this.addAliases("sethome");

		// Args
		this.addOptionalArg("faction", "you");

		// Requirements
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqIsPlayer.get());
		this.addRequirements(ReqHasPerm.get(Perm.SETHOME.node));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		// Args
		Faction faction = this.arg(0, ARFaction.get(usenderFaction), usenderFaction);
		if (faction == null) return;
		
		PS newHome = PS.valueOf(me.getLocation());
		
		// Validate
		if ( ! UConf.get(faction).homesEnabled)
		{
			usender.msg("<b>Sorry, Faction homes are disabled on this server.");
			return;
		}
		
		// FPerm
		if ( ! FPerm.SETHOME.has(usender, faction, true)) return;
		
		// Verify
		if (!usender.isUsingAdminMode() && !faction.isValidHome(newHome))
		{
			usender.msg("<b>Sorry, your faction home can only be set inside your own claimed territory.");
			return;
		}
		
		// Event
		FactionsEventHomeChange event = new FactionsEventHomeChange(sender, faction, newHome);
		event.run();
		if (event.isCancelled()) return;
		newHome = event.getNewHome();

		// Apply
		faction.setHome(newHome);
		
		// Inform
		faction.msg("%s<i> set the home for your faction. You can now use:", usender.describeTo(usenderFaction, true));
		faction.sendMessage(Factions.get().getOuterCmdFactions().cmdFactionsHome.getUseageTemplate());
		if (faction != usenderFaction)
		{
			usender.msg("<b>You have set the home for the "+faction.getName(usender)+"<i> faction.");
		}
	}
	
}