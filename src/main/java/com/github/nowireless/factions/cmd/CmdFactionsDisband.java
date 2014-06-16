package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.FFlag;
import com.github.nowireless.factions.FPerm;
import com.github.nowireless.factions.Factions;
import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.cmd.arg.ARFaction;
import com.github.nowireless.factions.cmd.req.ReqFactionsEnabled;
import com.github.nowireless.factions.entity.Faction;
import com.github.nowireless.factions.entity.FactionColls;
import com.github.nowireless.factions.entity.MConf;
import com.github.nowireless.factions.entity.UPlayer;
import com.github.nowireless.factions.entity.UPlayerColls;
import com.github.nowireless.factions.event.FactionsEventDisband;
import com.github.nowireless.factions.event.FactionsEventMembershipChange;
import com.github.nowireless.factions.event.FactionsEventMembershipChange.MembershipChangeReason;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.util.Txt;

public class CmdFactionsDisband extends FCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsDisband()
	{
		// Aliases
		this.addAliases("disband");

		// Args
		this.addOptionalArg("faction", "you");

		// Requirements
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.DISBAND.node));
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{	
		// Args
		Faction faction = this.arg(0, ARFaction.get(usender), usenderFaction);
		if (faction == null) return;
		
		// FPerm
		if ( ! FPerm.DISBAND.has(usender, faction, true)) return;

		// Verify
		if (faction.getFlag(FFlag.PERMANENT))
		{
			msg("<i>This faction is designated as permanent, so you cannot disband it.");
			return;
		}

		// Event
		FactionsEventDisband event = new FactionsEventDisband(me, faction);
		event.run();
		if (event.isCancelled()) return;

		// Merged Apply and Inform
		
		// Run event for each player in the faction
		for (UPlayer uplayer : faction.getUPlayers())
		{
			FactionsEventMembershipChange membershipChangeEvent = new FactionsEventMembershipChange(sender, uplayer, FactionColls.get().get(faction).getNone(), MembershipChangeReason.DISBAND);
			membershipChangeEvent.run();
		}

		// Inform all players
		for (UPlayer uplayer : UPlayerColls.get().get(usender).getAllOnline())
		{
			String who = usender.describeTo(uplayer);
			if (uplayer.getFaction() == faction)
			{
				uplayer.msg("<h>%s<i> disbanded your faction.", who);
			}
			else
			{
				uplayer.msg("<h>%s<i> disbanded the faction %s.", who, faction.getName(uplayer));
			}
		}
		
		if (MConf.get().logFactionDisband)
		{
			Factions.get().log(Txt.parse("<i>The faction <h>%s <i>(<h>%s<i>) was disbanded by <h>%s<i>.", faction.getName(), faction.getId(), usender.getDisplayName()));
		}		
		
		faction.detach();
	}
	
}
