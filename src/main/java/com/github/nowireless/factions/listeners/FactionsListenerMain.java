package com.github.nowireless.factions.listeners;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.github.nowireless.factions.FFlag;
import com.github.nowireless.factions.FPerm;
import com.github.nowireless.factions.Factions;
import com.github.nowireless.factions.Rel;
import com.github.nowireless.factions.TerritoryAccess;
import com.github.nowireless.factions.entity.Faction;
import com.github.nowireless.factions.entity.MConf;
import com.github.nowireless.factions.entity.UConf;
import com.github.nowireless.factions.entity.UPlayer;
import com.github.nowireless.factions.entity.UPlayerColl;
import com.github.nowireless.factions.entity.UPlayerColls;
import com.github.nowireless.factions.event.FactionsEventPowerChange;
import com.github.nowireless.factions.event.FactionsEventPvpDisallowed;
import com.github.nowireless.factions.event.FactionsEventPowerChange.PowerChangeReason;
import com.github.nowireless.factions.util.VisualizeUtil;
//import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.PlayerUtil;
import com.massivecraft.massivecore.util.Txt;

public class FactionsListenerMain implements Listener
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static FactionsListenerMain i = new FactionsListenerMain();
	public static FactionsListenerMain get() { return i; }
	public FactionsListenerMain() {}
	
	// -------------------------------------------- //
	// SETUP
	// -------------------------------------------- //
	
	public void setup()
	{
		Bukkit.getPluginManager().registerEvents(this, Factions.get());
	}

	// -------------------------------------------- //
	// CHUNK CHANGE: DETECT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void chunkChangeDetect(PlayerMoveEvent event)
	{
		
	}
	
	// -------------------------------------------- //
	// CHUNK CHANGE: TERRITORY INFO
	// -------------------------------------------- //
	
	public void chunkChangeTerritoryInfo(UPlayer uplayer, Player player, PS chunkFrom, PS chunkTo, Faction factionFrom, Faction factionTo)
	{
		
	}
	
	// -------------------------------------------- //
	// CHUNK CHANGE: AUTO CLAIM
	// -------------------------------------------- //
	
	public void chunkChangeAutoClaim(UPlayer uplayer, PS chunkTo)
	{
		// If the player is auto claiming ...
		Faction autoClaimFaction = uplayer.getAutoClaimFaction();
		if (autoClaimFaction == null) return;
		
		// ... try claim.
		uplayer.tryClaim(autoClaimFaction, chunkTo, true, true);
	}
	
	// -------------------------------------------- //
	// POWER LOSS ON DEATH
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void powerLossOnDeath(PlayerDeathEvent event)
	{
		
	}
	
	// -------------------------------------------- //
	// CAN COMBAT DAMAGE HAPPEN
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void canCombatDamageHappen(EntityDamageEvent event)
	{
		// TODO: Can't we just listen to the class type the sub is of?
		if (!(event instanceof EntityDamageByEntityEvent)) return;
		EntityDamageByEntityEvent sub = (EntityDamageByEntityEvent)event;
		
		if (this.canCombatDamageHappen(sub, true)) return;
		event.setCancelled(true);
	}

	// mainly for flaming arrows; don't want allies or people in safe zones to be ignited even after damage event is cancelled
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void canCombatDamageHappen(EntityCombustByEntityEvent event)
	{
		EntityDamageByEntityEvent sub = new EntityDamageByEntityEvent(event.getCombuster(), event.getEntity(), EntityDamageEvent.DamageCause.FIRE, 0D);
		if (this.canCombatDamageHappen(sub, false)) return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void canCombatDamageHappen(PotionSplashEvent event)
	{
		// If a harmful potion is splashing ...
		if (!MUtil.isHarmfulPotion(event.getPotion())) return;
		
		ProjectileSource projectileSource = event.getPotion().getShooter();
		if (! (projectileSource instanceof Entity)) return;
		
		Entity thrower = (Entity)projectileSource;

		// ... scan through affected entities to make sure they're all valid targets.
		for (LivingEntity affectedEntity : event.getAffectedEntities())
		{
			EntityDamageByEntityEvent sub = new EntityDamageByEntityEvent(thrower, affectedEntity, EntityDamageEvent.DamageCause.CUSTOM, 0D);
			if (this.canCombatDamageHappen(sub, true)) continue;
			
			// affected entity list doesn't accept modification (iter.remove() is a no-go), but this works
			event.setIntensity(affectedEntity, 0.0);
		}
	}

	// Utility method used in "canCombatDamageHappen" below.
	public static boolean falseUnlessDisallowedPvpEventCancelled(Player attacker, Player defender, EntityDamageByEntityEvent event)
	{
		FactionsEventPvpDisallowed dpe = new FactionsEventPvpDisallowed(attacker, defender, event);
		dpe.run();
		return dpe.isCancelled();
	}
	
	public boolean canCombatDamageHappen(EntityDamageByEntityEvent event, boolean notify)
	{
		boolean ret = true;
		
		// If the defender is a player ...
		Entity edefender = event.getEntity();
		if (!(edefender instanceof Player)) return true;
		Player defender = (Player)edefender;
		UPlayer udefender = UPlayer.get(edefender);
		
		// Check Disabled
		if (UConf.isDisabled(defender)) return true;
		
		// ... and the attacker is someone else ...
		Entity eattacker = MUtil.getLiableDamager(event);
		
		// (we check null here since there may not be an attacker)
		// (lack of attacker situations can be caused by other bukkit plugins)
		if (eattacker != null && eattacker.equals(edefender)) return true;
		
		// ... gather defender PS and faction information ...
		PS defenderPs = PS.valueOf(defender);
		Faction defenderPsFaction = null;// BoardColls.get().getFactionAt(defenderPs);
		
		// ... PVP flag may cause a damage block ...
		if (defenderPsFaction.getFlag(FFlag.PVP) == false)
		{
			if (eattacker == null)
			{
				// No attacker?
				// Let's behave as if it were a player
				return falseUnlessDisallowedPvpEventCancelled(null, defender, event);
			}
			if (eattacker instanceof Player)
			{
				ret = falseUnlessDisallowedPvpEventCancelled((Player)eattacker, defender, event);
				if (!ret && notify)
				{
					UPlayer attacker = UPlayer.get(eattacker);
					attacker.msg("<i>PVP is disabled in %s.", defenderPsFaction.describeTo(attacker));
				}
				return ret;
			}
			return defenderPsFaction.getFlag(FFlag.MONSTERS);
		}

		// ... and if the attacker is a player ...
		if (!(eattacker instanceof Player)) return true;
		Player attacker = (Player)eattacker;
		UPlayer uattacker = UPlayer.get(attacker);
		
		// ... does this player bypass all protection? ...
		if (MConf.get().playersWhoBypassAllProtection.contains(attacker.getName())) return true;

		// ... gather attacker PS and faction information ...
		PS attackerPs = PS.valueOf(attacker);
		Faction attackerPsFaction = null;// BoardColls.get().getFactionAt(attackerPs);

		// ... PVP flag may cause a damage block ...
		// (just checking the defender as above isn't enough. What about the attacker? It could be in a no-pvp area)
		// NOTE: This check is probably not that important but we could keep it anyways.
		if (attackerPsFaction.getFlag(FFlag.PVP) == false)
		{
			ret = falseUnlessDisallowedPvpEventCancelled(attacker, defender, event);
			if (!ret && notify) uattacker.msg("<i>PVP is disabled in %s.", attackerPsFaction.describeTo(uattacker));
			return ret;
		}

		// ... are PVP rules completely ignored in this world? ...
		if (MConf.get().getWorldsIgnlorePvP().contains(defenderPs.getWorld())) return true;

		Faction defendFaction = udefender.getFaction();
		Faction attackFaction = uattacker.getFaction();
		UConf uconf = UConf.get(attackFaction);

		if (attackFaction.isNone() && uconf.disablePVPForFactionlessPlayers)
		{
			ret = falseUnlessDisallowedPvpEventCancelled(attacker, defender, event);
			if (!ret && notify) uattacker.msg("<i>You can't hurt other players until you join a faction.");
			return ret;
		}
		else if (defendFaction.isNone())
		{
			if (defenderPsFaction == attackFaction && uconf.enablePVPAgainstFactionlessInAttackersLand)
			{
				// Allow PVP vs. Factionless in attacker's faction territory
				return true;
			}
			else if (uconf.disablePVPForFactionlessPlayers)
			{
				ret = falseUnlessDisallowedPvpEventCancelled(attacker, defender, event);
				if (!ret && notify) uattacker.msg("<i>You can't hurt players who are not currently in a faction.");
				return ret;
			}
		}

		Rel relation = defendFaction.getRelationTo(attackFaction);

		// Check the relation
		if (udefender.hasFaction() && relation.isFriend() && defenderPsFaction.getFlag(FFlag.FRIENDLYFIRE) == false)
		{
			ret = falseUnlessDisallowedPvpEventCancelled(attacker, defender, event);
			if (!ret && notify) uattacker.msg("<i>You can't hurt %s<i>.", relation.getDescPlayerMany());
			return ret;
		}

		// You can not hurt neutrals in their own territory.
		boolean ownTerritory = udefender.isInOwnTerritory();
		
		if (udefender.hasFaction() && ownTerritory && relation == Rel.NEUTRAL)
		{
			ret = falseUnlessDisallowedPvpEventCancelled(attacker, defender, event);
			if (!ret && notify)
			{
				uattacker.msg("<i>You can't hurt %s<i> in their own territory unless you declare them as an enemy.", udefender.describeTo(uattacker));
				udefender.msg("%s<i> tried to hurt you.", uattacker.describeTo(udefender, true));
			}
			return ret;
		}

		// Damage will be dealt. However check if the damage should be reduced.
		double damage = event.getDamage();
		if (damage > 0.0 && udefender.hasFaction() && ownTerritory && uconf.territoryShieldFactor > 0)
		{
			double newDamage = damage * (1D - uconf.territoryShieldFactor);
			event.setDamage(newDamage);

			// Send message
			if (notify)
			{
				String perc = MessageFormat.format("{0,number,#%}", (uconf.territoryShieldFactor)); // TODO does this display correctly??
				udefender.msg("<i>Enemy damage reduced by <rose>%s<i>.", perc);
			}
		}

		return true;
	}
	
	// -------------------------------------------- //
	// REMOVE PLAYER DATA WHEN BANNED
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event)
	{
		// If a player was kicked from the server ...
		Player player = event.getPlayer();

		// ... and if the if player was banned (not just kicked) ...
		if (!event.getReason().equals("Banned by admin.")) return;
		
		// ... and we remove player data when banned ...
		if (!MConf.get().removePlayerDataWhenBanned) return;
		
		// ... get rid of their stored info.
		for (UPlayerColl coll : UPlayerColls.get().getColls())
		{
			UPlayer uplayer = coll.get(player, false);
			if (uplayer == null) continue;
			
			if (uplayer.getRole() == Rel.LEADER)
			{
				uplayer.getFaction().promoteNewLeader();
			}
			uplayer.leave();
			uplayer.detach();
		}
	}
	
	// -------------------------------------------- //
	// VISUALIZE UTIL
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerMoveClearVisualizations(PlayerMoveEvent event)
	{
		if (MUtil.isSameBlock(event)) return;
		
		VisualizeUtil.clear(event.getPlayer());
	}
	
	// -------------------------------------------- //
	// DENY COMMANDS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void denyCommands(PlayerCommandPreprocessEvent event)
	{
		
	}

	private static boolean containsCommand(String needle, Collection<String> haystack)
	{
		if (needle == null) return false;
		needle = Txt.removeLeadingCommandDust(needle);
		needle = needle.toLowerCase();
		
		for (String straw : haystack)
		{
			if (straw == null) continue;
			straw = Txt.removeLeadingCommandDust(straw);
			straw = straw.toLowerCase();
			
			if (needle.startsWith(straw)) return true;
		}
		
		return false;
	}
	
	// -------------------------------------------- //
	// FLAG: MONSTERS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockMonsters(CreatureSpawnEvent event)
	{
		
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockMonsters(EntityTargetEvent event)
	{
		
	}
	
	// -------------------------------------------- //
	// FLAG: EXPLOSIONS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockExplosion(HangingBreakEvent event)
	{
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockExplosion(EntityExplodeEvent event)
	{

	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockExplosion(EntityChangeBlockEvent event)
	{
		
	}
	
	// -------------------------------------------- //
	// FLAG: ENDERGRIEF
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockEndergrief(EntityChangeBlockEvent event)
	{
		
	}

	// -------------------------------------------- //
	// FLAG: FIRE SPREAD
	// -------------------------------------------- //
	
	public void blockFireSpread(Block block, Cancellable cancellable)
	{
		
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockFireSpread(BlockIgniteEvent event)
	{
		// If fire is spreading ...
		if (event.getCause() != IgniteCause.SPREAD && event.getCause() != IgniteCause.LAVA) return;
		
		// ... consider blocking it.
		blockFireSpread(event.getBlock(), event);
	}
	
	// TODO: Is use of this event deprecated?
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockFireSpread(BlockSpreadEvent event)
	{
		// If fire is spreading ...
		if (event.getNewState().getType() != Material.FIRE) return;
		
		// ... consider blocking it.
		blockFireSpread(event.getBlock(), event);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockFireSpread(BlockBurnEvent event)
	{
		// If a block is burning ...
		
		// ... consider blocking it.
		blockFireSpread(event.getBlock(), event);
	}
	
	// -------------------------------------------- //
	// FLAG: BUILD
	// -------------------------------------------- //

	public static boolean canPlayerBuildAt(Player player, PS ps, boolean verboose)
	{
		return false;
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockBuild(HangingPlaceEvent event)
	{
		if (canPlayerBuildAt(event.getPlayer(), PS.valueOf(event.getEntity()), true)) return;
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockBuild(HangingBreakEvent event)
	{
		if (! (event instanceof HangingBreakByEntityEvent)) return;
		HangingBreakByEntityEvent entityEvent = (HangingBreakByEntityEvent)event;
		
		Entity breaker = entityEvent.getRemover();
		if (! (breaker instanceof Player)) return;

		if ( ! canPlayerBuildAt((Player)breaker, PS.valueOf(event.getEntity()), true))
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void itemFrameDamage(EntityDamageByEntityEvent event)
	{
		// If the damagee is an ItemFrame ...
		Entity edamagee = event.getEntity();
		if (!(edamagee instanceof ItemFrame)) return;
		ItemFrame itemFrame = (ItemFrame)edamagee;
		
		// ... and the liable damager is a player ...
		Entity edamager = MUtil.getLiableDamager(event);
		if (!(edamager instanceof Player)) return;
		Player player = (Player)edamager;
		
		// ... and the player can't build there ...
		if (canPlayerBuildAt(player, PS.valueOf(itemFrame), true)) return;
		
		// ... then cancel the event.
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void blockBuild(BlockPlaceEvent event)
	{
		if (!event.canBuild()) return;

		if (canPlayerBuildAt(event.getPlayer(), PS.valueOf(event.getBlock()), true)) return;
		
		event.setBuild(false);
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockBuild(BlockBreakEvent event)
	{
		if (canPlayerBuildAt(event.getPlayer(), PS.valueOf(event.getBlock()), true)) return;
		
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockBuild(BlockDamageEvent event)
	{
		if (!event.getInstaBreak()) return;

		if (canPlayerBuildAt(event.getPlayer(), PS.valueOf(event.getBlock()), true)) return;
		
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockBuild(BlockPistonExtendEvent event)
	{
		
		/*
		 * note that I originally was testing the territory of each affected block, but since I found that pistons can only push
		 * up to 12 blocks and the width of any territory is 16 blocks, it should be safe (and much more lightweight) to test
		 * only the final target block as done above
		 */
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void blockBuild(BlockPistonRetractEvent event)
	{	
		
	}
	
	// -------------------------------------------- //
	// ASSORTED BUILD AND INTERACT
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		// only need to check right-clicks and physical as of MC 1.4+; good performance boost
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.PHYSICAL) return;

		Block block = event.getClickedBlock();
		Player player = event.getPlayer();

		if (block == null) return;  // clicked in air, apparently

		if ( ! canPlayerUseBlock(player, block, false))
		{
			event.setCancelled(true);
			return;
		}

		if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;  // only interested on right-clicks for below

		if ( ! playerCanUseItemHere(player, PS.valueOf(block), event.getMaterial(), false))
		{
			event.setCancelled(true);
			return;
		}
	}

	// TODO: Refactor ! justCheck    -> to informIfNot
	// TODO: Possibly incorporate pain build... 
	public static boolean playerCanUseItemHere(Player player, PS ps, Material material, boolean justCheck)
	{
		if (!MConf.get().materialsEditTools.contains(material)) return true;
		
		String name = player.getName();
		if (MConf.get().playersWhoBypassAllProtection.contains(name)) return true;

		UPlayer uplayer = UPlayer.get(player);
		if (uplayer.isUsingAdminMode()) return true;
		
		return FPerm.BUILD.has(uplayer, ps, !justCheck);
	}
	
	public static boolean canPlayerUseBlock(Player player, Block block, boolean justCheck)
	{
		String name = player.getName();
		if (MConf.get().playersWhoBypassAllProtection.contains(name)) return true;

		UPlayer me = UPlayer.get(player);
		if (me.isUsingAdminMode()) return true;
		
		PS ps = PS.valueOf(block);
		Material material = block.getType();
		
		if (MConf.get().materialsEditOnInteract.contains(material) && ! FPerm.BUILD.has(me, ps, ! justCheck)) return false;
		if (MConf.get().materialsContainer.contains(material) && ! FPerm.CONTAINER.has(me, ps, ! justCheck)) return false;
		if (MConf.get().materialsDoor.contains(material) && ! FPerm.DOOR.has(me, ps, ! justCheck)) return false;
		if (material == Material.STONE_BUTTON && ! FPerm.BUTTON.has(me, ps, ! justCheck)) return false;
		if (material == Material.LEVER && ! FPerm.LEVER.has(me, ps, ! justCheck)) return false;
		return true;
	}

	// For some reason onPlayerInteract() sometimes misses bucket events depending on distance (something like 2-3 blocks away isn't detected),
	// but these separate bucket events below always fire without fail
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event)
	{
		Block block = event.getBlockClicked();
		Player player = event.getPlayer();
		
		if (playerCanUseItemHere(player, PS.valueOf(block), event.getBucket(), false)) return;
		
		event.setCancelled(true);
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerBucketFill(PlayerBucketFillEvent event)
	{
		Block block = event.getBlockClicked();
		Player player = event.getPlayer();

		if (playerCanUseItemHere(player, PS.valueOf(block), event.getBucket(), false)) return;
		
		event.setCancelled(true);
	}
	
	// -------------------------------------------- //
	// TELEPORT TO HOME ON DEATH
	// -------------------------------------------- //
	
	public void teleportToHomeOnDeath(PlayerRespawnEvent event, EventPriority priority)
	{
		// If a player is respawning ...
		final Player player = event.getPlayer();
		final UPlayer uplayer = UPlayer.get(player);
		final UConf uconf = UConf.get(player);
		
		// ... homes are enabled, active and at this priority ...
		if (!uconf.homesEnabled) return;
		if (!uconf.homesTeleportToOnDeathActive) return;
		if (uconf.homesTeleportToOnDeathPriority != priority) return;
		
		// ... and the player has a faction ...
		final Faction faction = uplayer.getFaction();
		if (faction.isNone()) return;
		
		// ... and the faction has a home ...
		PS home = faction.getHome();
		if (home == null) return;
		
		// ... and the home is translatable ...
		Location respawnLocation = null;
		try
		{
			respawnLocation = home.asBukkitLocation(true);
		}
		catch (Exception e)
		{
			// The home location map may have been deleted
			return;
		}
		
		// ... then use it for the respawn location.
		event.setRespawnLocation(respawnLocation);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void teleportToHomeOnDeathLowest(PlayerRespawnEvent event)
	{
		this.teleportToHomeOnDeath(event, EventPriority.LOWEST);
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void teleportToHomeOnDeathLow(PlayerRespawnEvent event)
	{
		this.teleportToHomeOnDeath(event, EventPriority.LOW);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void teleportToHomeOnDeathNormal(PlayerRespawnEvent event)
	{
		this.teleportToHomeOnDeath(event, EventPriority.NORMAL);
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void teleportToHomeOnDeathHigh(PlayerRespawnEvent event)
	{
		this.teleportToHomeOnDeath(event, EventPriority.HIGH);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void teleportToHomeOnDeathHighest(PlayerRespawnEvent event)
	{
		this.teleportToHomeOnDeath(event, EventPriority.HIGHEST);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void teleportToHomeOnDeathMonitor(PlayerRespawnEvent event)
	{
		this.teleportToHomeOnDeath(event, EventPriority.MONITOR);
	}
	
}
