package com.github.alexthe666.iceandfire.enums;

import com.github.alexthe666.iceandfire.item.ItemBestiary;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import scala.Int;

import java.util.*;

public enum EnumBestiaryPages {

	INTRODUCTION(2),
	FIREDRAGON(4),
	FIREDRAGONEGG(1),
	ICEDRAGON(4),
	ICEDRAGONEGG(1),
	LIGHTNINGDRAGON(5),
	LIGHTNINGDRAGONEGG(1),
	TAMEDDRAGONS(3),
	MATERIALS(2),
	ALCHEMY(1),
	VILLAGERS(0),
	HIPPOGRYPH(1),
	GORGON(1),
	PIXIE(1),
	CYCLOPS(2),
	SIREN(2),
	HIPPOCAMPUS(2),
	DEATHWORM(3),
	COCKATRICE(2),
	STYMPHALIANBIRD(1),
	TROLL(2),
	MYRMEX(4),
	AMPHITHERE(2),
	SEASERPENT(2),
	HYDRA(2),
	DREAD_MOBS(1);

	public final int pages;

	EnumBestiaryPages(int pages) {
		this.pages = pages;
	}

	public static List<Integer> toList(int[] containedPages) {
		List<Integer> intList = new ArrayList<>();
		if (containedPages == null) {
			return intList;
		}
		for (int containedPage : containedPages) {
			intList.add(containedPage);
		}
		return intList;
	}

	public static int[] fromList(List<Integer> containedPages) {
		int[] pages = new int[containedPages.size()];
		for (int i = 0; i < pages.length; i++)
			pages[i] = containedPages.get(i);
		return pages;
	}

	public static List<EnumBestiaryPages> containedPages(List<Integer> pages) {
		Iterator<Integer> itr = pages.iterator();
		List<EnumBestiaryPages> list = new ArrayList<>();
		while (itr.hasNext()) {
			list.add(EnumBestiaryPages.values()[itr.next()]);
		}
		return list;
	}

	public static List<Integer> enumToInt(List<EnumBestiaryPages> pages) {
		Iterator<com.github.alexthe666.iceandfire.enums.EnumBestiaryPages> itr = pages.iterator();
		List<Integer> list = new ArrayList<>();
		while (itr.hasNext()) {
			list.add(EnumBestiaryPages.values()[(itr.next()).ordinal()].ordinal());
		}
		return list;
	}

	public static EnumBestiaryPages getRand() {
		return EnumBestiaryPages.values()[new Random().nextInt(EnumBestiaryPages.values().length)];

	}

	public static void addRandomPage(ItemStack book) {
		if (book.getItem() instanceof ItemBestiary) {
			List<EnumBestiaryPages> list = EnumBestiaryPages.possiblePages(book);
			if (list != null && !list.isEmpty()) {
				addPage(list.get(new Random().nextInt(list.size())), book);
			}
		}
	}

	public static List<EnumBestiaryPages> possiblePages(ItemStack book) {
		if (book.getItem() instanceof ItemBestiary) {
			NBTTagCompound tag = book.getTagCompound();
			List<EnumBestiaryPages> allPages = new ArrayList<>();
			Collections.addAll(allPages, EnumBestiaryPages.values());
			List<EnumBestiaryPages> containedPages = getContainedPages(tag);
			List<EnumBestiaryPages> possiblePages = new ArrayList<>();
			for (EnumBestiaryPages page : allPages) {
				if (!containedPages.contains(page)) {
					possiblePages.add(page);
				}
			}
			return possiblePages;
		}
		return null;
	}


	public static void addPage(EnumBestiaryPages page, ItemStack book) {
		if (book.getItem() instanceof ItemBestiary) {
			NBTTagCompound tag = book.getTagCompound();
			if (tag == null) {
				return;
			}
			List<EnumBestiaryPages> enumlist = getContainedPages(tag);
			if (!enumlist.contains(page)) {
				enumlist.add(page);
			}
			tag.setIntArray("Pages", fromList(enumToInt(enumlist)));
		}
	}

	private static List<EnumBestiaryPages> getContainedPages(NBTTagCompound tag) {
		if (tag == null) {
			return new ArrayList<>();
		}
		List<Integer> pages = toList(tag.getIntArray("Pages"));
		return containedPages(pages);
	}
}
