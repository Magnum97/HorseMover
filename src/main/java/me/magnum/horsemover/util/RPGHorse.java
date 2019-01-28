package me.magnum.horsemover.util;

import com.HakYazilim.HorseRPGv3.main.Main;
import com.HakYazilim.HorseRPGv3.utils.Api;
import com.HakYazilim.HorseRPGv3.utils.HorseApi;
import me.magnum.horsemover.HorseMover;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings ("deprecation")
public class RPGHorse {

	public UUID ownerUUID, horseUUID, sParent, mParent;
	public String breed, name, sex;
	public double agility, swiftness, groundwork, traveldistance, agilityXP, swiftnessXP, groundworkXP, energy;
	public Horse.Style style;
	public Color color;
	public Variant variant;
	public ItemStack armor;
	private final HorseApi hapi = new HorseApi();
	private final Api api = new Api();
	private final String header = "&8[&eHorses&8]&e ";
	public String trait;
	public ItemStack saddle;
	public List<String> sicknesslist;
	public List<String> injurylist;
	public List<String> vaccinelist;
	public List<String> trustedPlayers;
	public Boolean dead;
	public int type, registerID, horseID;
	public Boolean pregnant;
	public long birthtime;
	public long breedingtime;
	public UUID leaseOwnerUUID;
	public long leaseStart;
	public long leasePeriod;
	public boolean onLung;
	public int step;
	public long stopTime, lastFed;
	private Location lastLocation;
	private long lastinj;
	//	private final HorseApi horseApi = new HorseApi();
//	private final Api api = new Api();
	private final String pre = HorseMover.pre;
	private final Map<String, String> traits = traitMap();


	public static Map<String, String> traitMap () {
		Map<String, String> traitMap = new HashMap<>();
		traitMap.put("energetic", "&a30% less energy usage");
		traitMap.put("quick learner", "&a20% more xp gain");
		traitMap.put("legend", "It came from the old world");
		return traitMap;
	}

	public UUID getOwnerUUID () {
		return ownerUUID;
	}

	public UUID getHorseUUID () {
		return horseUUID;
	}

	public ItemStack getSaddle () {
		return saddle;
	}

	public RPGHorse (OfflinePlayer p, String name, String color, String style, String breed, String gender) {
		this.ownerUUID = p.getUniqueId();
		this.horseUUID = UUID.randomUUID();
		this.breed = breed;
		this.name = name;
		this.sex = gender;
		this.agility = 1;
		this.groundwork = 1;
		this.sicknesslist = new ArrayList<>();
		this.injurylist = new ArrayList<>();
		this.vaccinelist = new ArrayList<>();
		this.trustedPlayers = new ArrayList<>();
		this.swiftness = 1;
		this.traveldistance = 0;
		this.agilityXP = 0.0;
		this.swiftnessXP = 0.0;
		this.groundworkXP = 0.0;
		this.type = 1;
		this.pregnant = false;
		this.birthtime = 0;
		this.breedingtime = 0;
		this.dead = false;
		this.color = Color.valueOf(color);
		this.style = Horse.Style.valueOf(style);
		this.variant = Variant.HORSE;
		this.trait = new ArrayList<>(Main.getInstance().traits.keySet()).get(ThreadLocalRandom.current().nextInt(0,
				Main.getInstance().traits.size()));
		this.armor = new ItemStack(Material.AIR);
		this.energy = 100;
		this.saddle = new ItemStack(Material.AIR);
		this.leaseOwnerUUID = null;
		this.sParent = null;
		this.mParent = null;
		this.horseID = 0;
		this.registerID = 0;
		this.leaseStart = 0L;
		this.leasePeriod = 0L;
		this.onLung = false;
		this.step = 0;
		this.stopTime = 0L;
		this.lastFed = 0L;
	}


/*
	public RPGHorse (Player player, Horse horse, double agility, double swift, double gw, double travelDist, String breed, String sex, String name, int type) {
		this.o_UUID = player.getUniqueId();
		this.h_UUID = horse.getUniqueId();
		this.breed = breed;
		this.name = name;
		this.sex = sex;
		this.style = horse.getStyle();
		this.color = horse.getColor();
		this.variant = horse.getVariant();
		this.trait = new ArrayList<>(traits.keySet()).get(ThreadLocalRandom.current().nextInt(0, traits.size()));
	}
*/

/*
	public RPGHorse (OfflinePlayer offlinePlayer, String name) {
		this.o_UUID = offlinePlayer.getUniqueId();
		this.h_UUID = UUID.randomUUID();
		this.breed = "";
		this.name = name;
		List<String> sexes = Arrays.asList("mare", "stallion");

	}
*/
}
