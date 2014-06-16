package com.github.nowireless.factions.cmd;

import com.github.nowireless.factions.Factions;
import com.github.nowireless.factions.Perm;
import com.github.nowireless.factions.cmd.arg.ARFaction;
import com.github.nowireless.factions.cmd.req.ReqBankCommandsEnabled;
import com.github.nowireless.factions.cmd.req.ReqFactionsEnabled;
import com.github.nowireless.factions.entity.Faction;
import com.github.nowireless.factions.entity.MConf;
import com.github.nowireless.factions.integration.Econ;
import com.massivecraft.massivecore.cmd.arg.ARDouble;
import com.massivecraft.massivecore.cmd.req.ReqHasPerm;
import com.massivecraft.massivecore.money.Money;
import com.massivecraft.massivecore.util.Txt;

import org.bukkit.ChatColor;

public class CmdFactionsMoneyDeposit extends FCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMoneyDeposit()
	{
		// Aliases
		this.addAliases("d", "deposit");

		// Args
		this.addRequiredArg("amount");
		this.addOptionalArg("faction", "you");

		// Requirements
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.MONEY_DEPOSIT.node));
		this.addRequirements(ReqBankCommandsEnabled.get());
	}

	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform()
	{
		Double amount = this.arg(0, ARDouble.get());
		if (amount == null) return;
		
		Faction faction = this.arg(1, ARFaction.get(sender), usenderFaction);
		if (faction == null) return;
		
		boolean success = Econ.transferMoney(usender, usender, faction, amount);
		
		if (success && MConf.get().logMoneyTransactions)
		{
			Factions.get().log(ChatColor.stripColor(Txt.parse("%s deposited %s in the faction bank: %s", usender.getName(), Money.format(amount), faction.describeTo(null))));
		}
	}
	
}
