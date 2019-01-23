package me.magnum.horsemover.util;

import com.HakYazilim.HorseRPGv3.utils.Api;
import com.HakYazilim.HorseRPGv3.utils.HorseApi;
import me.magnum.horsemover.HorseMover;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings ("deprecation")
public class RPGHorse {

	public UUID o_UUID, h_UUID;
	public String breed, name, sex;
	public Horse.Style style;
	public Color color;
	public Variant variant;
	private final HorseApi horseApi = new HorseApi();
	private final Api api = new Api();
	private final String pre = HorseMover.pre;
	private final Map<String, String> traits = traitMap();
	public String trait;
	public int type, horseID;


	public static Map<String, String> traitMap () {
		Map<String, String> traitMap = new HashMap<>();
		traitMap.put("energetic", "&a30% less energy usage");
		traitMap.put("quick learner", "&a20% more xp gain");
		traitMap.put("legend", "It came from the old world");
		return traitMap;
	}

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

	public RPGHorse (OfflinePlayer offlinePlayer, String name) {
		this.o_UUID = offlinePlayer.getUniqueId();
		this.h_UUID = UUID.randomUUID();
		this.breed = "";
		this.name = name;
		List<String> sexes = Arrays.asList("mare", "stallion");

	}
}
