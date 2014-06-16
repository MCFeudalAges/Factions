package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.cmd.arg.ARFaction;
import com.github.nowireless.factions.cmd.req.ReqBankCommandsEnabled;
import com.github.nowireless.factions.cmd.req.ReqFactionsEnabled;
import com.github.nowireless.factions.entity.Faction;
import com.github.nowireless.factions.integration.Econ;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;

public class CmdFactionsMoneyBalance extends FCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMoneyBalance()
	{
		// Aliases
		this.addAliases("b", "balance");

		// Args
		this.addOptionalArg("faction", "you");

		// Requirements
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.MONEY_BALANCE.node));
		this.addRequirements(ReqBankCommandsEnabled.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Faction faction = this.arg(0, ARFaction.get(sender), usenderFaction);
		if (faction == null) return;
			
		if (faction != usenderFaction && ! Perm.MONEY_BALANCE_ANY.has(sender, true)) return;
		
		Econ.sendBalanceInfo(usender, faction);
	}
	
}
