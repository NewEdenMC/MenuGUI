package co.neweden.menugui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class Util {
	
	private Util() { }

	public static String formatString(String text) {
		text = text.replaceAll("&0", "\u00A70"); // Black
		text = text.replaceAll("&1", "\u00A71"); // Dark Blue
		text = text.replaceAll("&2", "\u00A72"); // Dark Green
		text = text.replaceAll("&3", "\u00A73"); // Dark Aqua
		text = text.replaceAll("&4", "\u00A74"); // Dark Red
		text = text.replaceAll("&5", "\u00A75"); // Dark Purple
		text = text.replaceAll("&6", "\u00A76"); // Gold
		text = text.replaceAll("&7", "\u00A77"); // Gray
		text = text.replaceAll("&8", "\u00A78"); // Dark Gray
		text = text.replaceAll("&9", "\u00A79"); // Blue
		text = text.replaceAll("&a", "\u00A7a"); // Green
		text = text.replaceAll("&b", "\u00A7b"); // Aqua
		text = text.replaceAll("&c", "\u00A7c"); // Red
		text = text.replaceAll("&d", "\u00A7d"); // Light Purple
		text = text.replaceAll("&e", "\u00A7e"); // Yellow
		text = text.replaceAll("&f", "\u00A7f"); // White
		
		text = text.replaceAll("&k", "\u00A7k"); // Obfuscated
		text = text.replaceAll("&l", "\u00A7l"); // Bold
		text = text.replaceAll("&m", "\u00A7m"); // Strikethrough
		text = text.replaceAll("&o", "\u00A7o"); // Italic
		text = text.replaceAll("&r", "\u00A7r"); // Reset
		
		return text;
	}

	public static String addLineBreaks(String input, int maxLineLength) {
		String[] parts = input.split(" ");
		int lineLen = 0;
		for (int i = 0; i < parts.length; i++) {
			System.out.print(parts[i]);
			//if (i == parts.length - 1) continue; // if last word, skip it as nothing after to break
			if (i > 0) lineLen++; // Account for the missing space in the line character count

			int newLength = lineLen + parts[i].length();
			if (newLength < maxLineLength) { // If line + word length doesn't exceed max line length keep going
				lineLen = newLength;
				System.out.print(parts[i] + "(" + lineLen + ")");
				continue;
			}

			// We now know we need to break the line
			if (lineLen > 0)
				parts[i - 1] += "\n"; // Add linebreak before word so this word will start the next line
			else if (lineLen == 0 && parts.length > 1)
				parts[i] += "\n"; // Add linebreak after word if it's only word on line and the paragraph has more than one word (to avoid a random linebreak after word)
			System.out.print(parts[i] + "(" + lineLen + ")");

			lineLen = 0;
		}
		return String.join(" ", parts);
	}
	
}
