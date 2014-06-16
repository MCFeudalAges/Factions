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


public class CmdFactionsMoneyTransferFf extends FCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdFactionsMoneyTransferFf()
	{
		// Aliases
		this.addAliases("ff");

		// Args
		this.addRequiredArg("amount");
		this.addRequiredArg("faction");
		this.addRequiredArg("faction");

		// Requirements
		this.addRequirements(ReqFactionsEnabled.get());
		this.addRequirements(ReqHasPerm.get(Perm.MONEY_F2F.node));
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
		
		Faction from = this.arg(1, ARFaction.get(sender));
		if (from == null) return;
		
		Faction to = this.arg(2, ARFaction.get(sender));
		if (to == null) return;
		
		boolean success = Econ.transferMoney(usender, from, to, amount);

		if (success && MConf.get().logMoneyTransactions)
		{
			Factions.get().log(ChatColor.stripColor(Txt.parse("%s transferred %s from the faction \"%s\" to the faction \"%s\"", usender.getName(), Money.format(amount), from.describeTo(null), to.describeTo(null))));
		}
	}
	
}
