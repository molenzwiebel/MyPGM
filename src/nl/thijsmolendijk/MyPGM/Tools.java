package nl.thijsmolendijk.MyPGM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Tools {
	//Hide a player and chenge his gamemode to creative
	public static void hidePlayer(Player p) {
		p.setGameMode(GameMode.CREATIVE);
		for (Player op : Bukkit.getOnlinePlayers()) {
			op.hidePlayer(p);
		}
	}
	//Show a player and change his gamemode to survival
	public static void showPlayer(Player p) {
		p.setGameMode(GameMode.SURVIVAL);
		for (Player op : Bukkit.getOnlinePlayers()) {
			op.showPlayer(p);
		}
	}
	//Use java reflection methods to get a value of a class
	public static Object getValueOf(Object clazz, String lookingForValue)
		       throws Exception {
		    Field field = clazz.getClass().getField(lookingForValue);
		    Class<?> clazzType = field.getType();
		    if (clazzType.toString().equals("double"))
		      return field.getDouble(clazz);
		    else if (clazzType.toString().equals("int"))
		      return field.getInt(clazz);
		    
		    // else other type ...
		    // and finally
		    return field.get(clazz);
	}
	//Use java reflection methods to set a named variable
	public static void setValueOf(Object clazz, String lookingForValue, Object value)
		       throws Exception {
		    Field field = clazz.getClass().getField(lookingForValue);
		    @SuppressWarnings("rawtypes")
			Class clazzType = field.getType();
		    
		    if (clazzType.toString().equals("double")) {
		      field.setDouble(clazz, Double.parseDouble((String) value));
		      return;
		    }
		    else if (clazzType.toString().equals("int")) {
		    	field.setInt(clazz, Integer.parseInt((String) value));
		    	return;
		    }
		    else if (clazzType.toString().equals("boolean")) {
		    	field.setBoolean(clazz, Boolean.parseBoolean((String) value));
		    	return;
		    }
		    else if (clazzType.toString().equals("class org.bukkit.Location")) {
		    	System.out.println("Setting location field!");
		    	String[] info = ((String) value).split(":");
		    	if (info.length != 3) {
		    		throw new Exception("Location format should be x:y:z");
		    	}
		    	World world = (World) Tools.getValueOf(clazz, "world");
		    	int x = Integer.parseInt(info[0]);
		    	int y = Integer.parseInt(info[1]);
		    	int z = Integer.parseInt(info[2]);
		    	field.set(clazz, new Location(world, x, y, z));
		    	System.out.println("Set location field to: "+ new Location(world, x, y, z));
		    	return;
		    }
		     field.set(clazz, value); 
		    // else other type ...
		    // and finally
	}
	//Show a player
	static void showPlayerNC(Player p) {
		
		for (Player op : Bukkit.getOnlinePlayers()) {
			op.showPlayer(p);
		}
	}
	//Copy a file from src to dest
	public static void copyFolder(File src, File dest)
	    	throws IOException{

	    		if(src.isDirectory()){

	    			//if directory not exists, create it
	    			if(!dest.exists()){
	    				dest.mkdir();
	    			}

	    			//list all the directory contents
	    			String files[] = src.list();

	    			for (String file : files) {
	    				//construct the src and dest file structure
	    				File srcFile = new File(src, file);
	    				File destFile = new File(dest, file);
	    				//recursive copy
	    				copyFolder(srcFile,destFile);
	    			}

	    		} else {
	    			//if file, then copy it
	    			//Use bytes stream to support all file types
	    			InputStream in = new FileInputStream(src);
	    	        OutputStream out = new FileOutputStream(dest); 

	    	        byte[] buffer = new byte[1024];

	    	        int length;
	    	        //copy the file content in bytes 
	    	        while ((length = in.read(buffer)) > 0){
	    	    	   out.write(buffer, 0, length);
	    	        }

	    	        in.close();
	    	        out.close();
	    	}
	}
	//Delete folder file
	public static void deleteFolder(File file)
	    	throws IOException{
	 
	    	if(file.isDirectory()){
	 
	    		//directory is empty, then delete it
	    		if(file.list().length==0){
	 
	    		   file.delete();
	    		   System.out.println("Directory is deleted : " 
	                                                 + file.getAbsolutePath());
	 
	    		}else{
	 
	    		   //list all the directory contents
	        	   String files[] = file.list();
	 
	        	   for (String temp : files) {
	        	      //construct the file structure
	        	      File fileDelete = new File(file, temp);
	 
	        	      //recursive delete
	        	     deleteFolder(fileDelete);
	        	   }
	 
	        	   //check the directory again, if empty then delete it
	        	   if(file.list().length==0){
	           	     file.delete();
	        	     System.out.println("Directory is deleted : " 
	                                                  + file.getAbsolutePath());
	        	   }
	    		}
	 
	    	}else{
	    		//if file, then delete it
	    		file.delete();
	    		System.out.println("File is deleted : " + file.getAbsolutePath());
	    	}
	}
	//Function to set a player inventory to a hashmap
	public static void addItemsToPlayerInv(Player p, HashMap<String, ItemStack> data) {
		for (Entry<String, ItemStack> entry : data.entrySet()) {
		    String key = entry.getKey();
		    System.out.println("Key: "+key);
		    ItemStack value = entry.getValue();
		    p.getInventory().setItem(Integer.parseInt(key), value);
		}
	}
	//Function to turn a hashmap into a xml
	public static String itemsToXML(HashMap<String, ItemStack> items) {
		ArrayList<String> str = new ArrayList<String>();
		str.add("The slots are as following: \n \n Hotbar: 0-8 \n Row 1: 9-17 \n Row 2: 18-26 \n Row 3: 27 - 35 \n Armor (bottom to top) 36-39 \n\n\n");
		for (Entry<String, ItemStack> entry2 : items.entrySet()) {
		    ItemStack value = entry2.getValue();
			if (value != null && value.getType() != Material.AIR) {
			String key = entry2.getKey();
		    int id = value.getTypeId();
		    int amount = value.getAmount();
		    int damage = value.getDurability();
		    String entry = "<entry";
		    boolean containsEnch = false;
		    for (Enchantment e : Enchantment.values()) {
		    	if (value.containsEnchantment(e)) {
		    		containsEnch = true;
		    	}
		    }
		    if (containsEnch) {
		    	entry = entry + " enchantment=\"";
		    	for (Entry<Enchantment, Integer> ench : value.getEnchantments().entrySet()) {
		    		entry = entry+ench.getKey().getName()+":"+ench.getValue()+",";
		    	}
		    	entry = entry + "\"";
		    }
		    entry = entry.replace(",\"", "\"");
		    if (value.getItemMeta().hasDisplayName()) {
		    	entry = entry + " displayName=\""+value.getItemMeta().getDisplayName()+"\"";
		    }
		    str.add(entry+">"+key+":"+id+":"+amount+":"+damage+"</entry>\n");
		    }
		}
		return str.toString().replace("[", "").replace("]", "").replace(", ", "");
	}
	//Function to upload to dpaste
	public static String uploadToPasteBin(HashMap<String, ItemStack> items) throws Exception {
		String text = Tools.itemsToXML(items);
		String url2 = "content="+text;
		URL url = new URL("http://dpaste.com/api/v1/");
		URLConnection conn = url.openConnection();

		conn.setDoOutput(true);
		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

		writer.write(url2);
		writer.flush();

		String line;
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		System.out.println(conn.getHeaderFields());
		while ((line = reader.readLine()) != null) {
		    if (line.contains("<title>")) {
		    	System.out.println("http://dpaste.com/"+line.replace("<title>", "").replace("</title>", "").replace("dpaste: #", ""));
		    	return "http://dpaste.com/"+line.replace("<title>", "").replace("</title>", "").replace("dpaste: #", "").replace("	","");
		    }
		}
		writer.close();
		reader.close(); 
		return "";
	}
	//Message to display when team one won
	static public String redWonMessage(Main plugin) {
		String redTeamName = plugin.currentMap.teamOne.preColor + plugin.currentMap.teamOne.name;
		String lenghtToBe = Tools.stringWithTokens("#", 5+redTeamName.length());
		String message = ChatColor.LIGHT_PURPLE+ Tools.stringWithTokens("#", 5+redTeamName.length()) +" \n" +
				         ChatColor.LIGHT_PURPLE+ " Game ended!"+Tools.stringWithTokens(" ", lenghtToBe.length()+19)+" \n" +
				         ChatColor.LIGHT_PURPLE+ " "+redTeamName+" wins!     \n" +
				         ChatColor.LIGHT_PURPLE+ Tools.stringWithTokens("#", 5+redTeamName.length());
		message = message.replace("Game", ChatColor.GOLD+"Game");
		message = message.replace("!", "!"+ChatColor.LIGHT_PURPLE);
		return message;
	}
	//Message to display when blue team won
	static public String blueWonMessage(Main plugin) {
		String redTeamName = plugin.currentMap.teamTwo.preColor + plugin.currentMap.teamTwo.name;
		String message = ChatColor.LIGHT_PURPLE+ Tools.stringWithTokens("#", 5+redTeamName.length()) +" \n" +
				         ChatColor.LIGHT_PURPLE+ " Game ended!"+Tools.stringWithTokens(" ", redTeamName.length()-3)+"  \n" +
				         ChatColor.LIGHT_PURPLE+ " "+redTeamName+" wins!    \n" +
				         ChatColor.LIGHT_PURPLE+ Tools.stringWithTokens("#", 5+redTeamName.length());
		message = message.replace("Game", ChatColor.GOLD+"Game");
		message = message.replace("!", "!"+ChatColor.LIGHT_PURPLE);
		return message;
	}
	//Message to display when it is a tie
	static public String tieMessage(Main plugin) {
		String message = ChatColor.LIGHT_PURPLE+ "############## \n" +
				         ChatColor.LIGHT_PURPLE+ "# Game ended!   # \n" +
				         ChatColor.LIGHT_PURPLE+ "# It's a tie!        # \n" +
				         ChatColor.LIGHT_PURPLE+ "##############";
		message = message.replace("Game", ChatColor.GOLD+"Game");
		message = message.replace("It's", ChatColor.AQUA+"It's");
		message = message.replace("!", "!"+ChatColor.LIGHT_PURPLE);
		return message;
	}
	//Message to display when a game starts
	static public String startMessage() {
		String message = ChatColor.LIGHT_PURPLE+ "###################### \n" +
						 ChatColor.LIGHT_PURPLE+ "# The game has started!  # \n" +
						 ChatColor.LIGHT_PURPLE+ "######################";
		message = message.replace("The", ChatColor.GOLD+"The");
		message = message.replace("!", "!"+ChatColor.LIGHT_PURPLE);
		return message;
	}
	//Funtion to turn seconds into HH:MM:SS
	public static String formatIntoHHMMSS(int secsIn)
	{

		int hours = secsIn / 3600,
		remainder = secsIn % 3600,
		minutes = remainder / 60,
		seconds = remainder % 60;

		return ( (hours < 10 ? "0" : "") + hours
		+ ":" + (minutes < 10 ? "0" : "") + minutes
		+ ":" + (seconds< 10 ? "0" : "") + seconds );

	}
	//Function to return a random boolean
	public static boolean getRandomBoolean() {
	    Random random = new Random();
	    return random.nextBoolean();
	}
	//Function to return a string with token repeated lenght times
	public static String stringWithTokens(String token, int lenght) {
		String str = "";
		for (int i = 0; i < lenght; i++) {
			str = str + token;
		}
		return str;
	}
	//Unused function to determine if a block is next to a lava block
	public static boolean hasLava(Block block ){
	    if( block.getRelative(BlockFace.NORTH).getType().equals(Material.LAVA)) return true;
	    if( block.getRelative(BlockFace.SOUTH).getType().equals(Material.LAVA)) return true;
	    if( block.getRelative(BlockFace.EAST).getType().equals(Material.LAVA)) return true;
	    if( block.getRelative(BlockFace.WEST).getType().equals(Material.LAVA)) return true;
	    return false;
	}
	public static int showRandomInteger(int aStart, int aEnd, Random aRandom){
	    if ( aStart > aEnd ) {
	      throw new IllegalArgumentException("Start cannot exceed End.");
	    }
	    //get the range, casting to long to avoid overflow problems
	    long range = (long)aEnd - (long)aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    int randomNumber =  (int)(fraction + aStart);    
	    return randomNumber;
	  }
}
