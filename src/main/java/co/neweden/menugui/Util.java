package co.neweden.menugui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	public static String formatString(String text, int maxLineLength) {
		char[] input = text.toCharArray();
		StringBuilder output = new StringBuilder();
		int wordStartAt = 0;
		String lastColourCode = "";
		int currLineLength = 1;

		for (int i = 0; i < input.length; i++) { // 7
			// convert colour codes
			if (i < input.length - 1 && (input[i] == '&' || input[i] == '\u00A7') && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(input[i + 1]) >= 0) {
				if (i >= 2 && input[i - 2] == '\u00A7')
					lastColourCode = "\u00A7" + input[i - 1] + "\u00A7" + input[i + 1];
				else
					lastColourCode = "\u00A7" + input[i + 1];
				output.append('\u00A7').append(input[i + 1]);
				i++;
				continue;
			}

			if (input[i] == ' ' || input[i] == '\n' || input[i] == '\r')
				wordStartAt = output.length(); // will be index after space
			if (input[i] == '\n' || input[i] == '\r')
				currLineLength = 0;

			if (currLineLength > maxLineLength) {
				output.insert(wordStartAt, "\n" + lastColourCode);
				output.append(input[i]);
				currLineLength = output.length() - 1 - wordStartAt;
				continue;
			}

			currLineLength++;
			output.append(input[i]);
		}

		return output.toString();
	}

}
