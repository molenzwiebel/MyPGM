package nl.thijsmolendijk.MyPGM;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLHandler {
	public static MapData getMapData(String mapName, World world) throws Exception {

	 	//Try to load the map .xml
		File fXmlFile = new File("Maps/", mapName+".xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
	 
		//optional, but recommended
		//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		doc.getDocumentElement().normalize();
		Element ed = doc.getDocumentElement();
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	 
		NodeList nListRedInv = doc.getElementsByTagName("redInv");
		Node redInvItems = nListRedInv.item(0);
		NodeList redInv = redInvItems.getChildNodes();
		NodeList nListBlueInv = doc.getElementsByTagName("blueInv");
		Node blueInvItems = nListBlueInv.item(0);
		NodeList blueInv = blueInvItems.getChildNodes();
		MapData data = new MapData("", "", "", 0);
		if (notExists(ed, "author")) throw new Exception("Tag \"author\" doesn't exist");
		data.author = doc.getElementsByTagName("author").item(0).getTextContent();
		
		if (notExists(ed, "objective")) throw new Exception("Tag \"objective\" doesn't exist");
		data.objective = doc.getElementsByTagName("objective").item(0).getTextContent();
		
		if (notExists(ed, "type")) throw new Exception("Tag \"type\" doesn't exist");
		data.gameType = doc.getElementsByTagName("type").item(0).getTextContent();
		
		if (notExists(ed, "redCorePos")) throw new Exception("Tag \"redCorePos\" doesn't exist");
		String[] redCoreData = doc.getElementsByTagName("redCorePos").item(0).getTextContent().split(":");
		data.redCoreLocation = new Location(world, Integer.parseInt(redCoreData[0]), Integer.parseInt(redCoreData[1]), Integer.parseInt(redCoreData[2]));
		
		if (notExists(ed, "blueCorePos")) throw new Exception("Tag \"blueCorePos\" doesn't exist");
		String[] blueCoreData = doc.getElementsByTagName("blueCorePos").item(0).getTextContent().split(":");
		data.blueCoreLocation = new Location(world, Integer.parseInt(blueCoreData[0]), Integer.parseInt(blueCoreData[1]), Integer.parseInt(blueCoreData[2]));
		
		if (notExists(ed, "coreRadius")) throw new Exception("Tag \"coreRadius\" doesn't exist");
		data.coreRadius = Integer.parseInt(doc.getElementsByTagName("coreRadius").item(0).getTextContent());
		
		if (notExists(ed, "dropItemsOnDeath")) throw new Exception("Tag \"dropItemsOnDeath\" doesn't exist");
		data.dropItemsOnDeath = Boolean.parseBoolean(doc.getElementsByTagName("dropItemsOnDeath").item(0).getTextContent());
		
		if (notExists(ed, "entitySpawning")) throw new Exception("Tag \"entitySpawning\" doesn't exist");
		data.spawnMonsters = Boolean.parseBoolean(doc.getElementsByTagName("entitySpawning").item(0).getTextContent());
		
		if (notExists(ed, "blockBreaking")) throw new Exception("Tag \"blockBreaking\" doesn't exist");
		data.blockBreaking = Boolean.parseBoolean(doc.getElementsByTagName("blockBreaking").item(0).getTextContent());
		
		if (notExists(ed, "forceTime")) throw new Exception("Tag \"forceTime\" doesn't exist");
		data.forceTime = Boolean.parseBoolean(doc.getElementsByTagName("forceTime").item(0).getTextContent());
		
		if (notExists(ed, "forcedTime")) throw new Exception("Tag \"forcedTime\" doesn't exist");
		data.timeToForce = Integer.parseInt(doc.getElementsByTagName("forcedTime").item(0).getTextContent());
		
		if (notExists(ed, "name")) throw new Exception("Tag \"name\" doesn't exist");
		data.name = doc.getElementsByTagName("name").item(0).getTextContent();
		
		data.realName = mapName;
		data.world = world;
		data.matchLenght = Integer.parseInt(doc.getElementsByTagName("lenght").item(0).getTextContent());
		data.spawnProtectionRadius = Integer.parseInt(doc.getElementsByTagName("spawnProtectionRadius").item(0).getTextContent());
		
		if (notExists(ed, "redSpawn")) throw new Exception("Tag \"redSpawn\" doesn't exist");
		String[] redSpawnData = doc.getElementsByTagName("redSpawn").item(0).getTextContent().split(":");
		
		if (notExists(ed, "blueSpawn")) throw new Exception("Tag \"blueSpawn\" doesn't exist");
		String[] blueSpawnData = doc.getElementsByTagName("blueSpawn").item(0).getTextContent().split(":");
		
		for (int temp = 0; temp < redInv.getLength(); temp++) {
			Node nNode = redInv.item(temp);
			if (nNode.getNodeName().equals("entry")) {
				String[] itemData = nNode.getTextContent().split(":");
				itemData[0] = itemData[0].replace("\n", "").replace(" ", "");
				ItemStack item = new ItemStack(Integer.parseInt(itemData[1]), Integer.parseInt(itemData[2]), (short) Integer.parseInt(itemData[3]));
				Element element = (Element) nNode;
				if (element.hasAttribute("enchantment")) {
					
					for (int i = 0; i < element.getAttribute("enchantment").split(",").length; i++) {
						String rawEnch = element.getAttribute("enchantment").split(",")[i];
						String enchantment = rawEnch.split(":")[0];
						String level = rawEnch.split(":")[1];
						Enchantment e = Enchantment.getByName(enchantment);
						item.addUnsafeEnchantment(e, Integer.parseInt(level));
						System.out.println("Enchantment: "+enchantment+", level: "+level);
					}
				}
				if (element.hasAttribute("displayName")) {
					ItemMeta itemMeta = item.getItemMeta();
					itemMeta.setDisplayName(element.getAttribute("displayName"));
					item.setItemMeta(itemMeta);
				}
				data.redInv.put(itemData[0], item);
			}
		}
		for (int temp = 0; temp < blueInv.getLength(); temp++) {
			Node nNode = blueInv.item(temp);
			if (nNode.getNodeName().equals("entry")) {
				String[] itemData = nNode.getTextContent().split(":");
				itemData[0] = itemData[0].replace("\n", "").replace(" ", "");
				ItemStack item = new ItemStack(Integer.parseInt(itemData[1]), Integer.parseInt(itemData[2]), (short) Integer.parseInt(itemData[3]));
				Element element = (Element) nNode;
				if (element.hasAttribute("enchantment")) {
					
					for (int i = 0; i < element.getAttribute("enchantment").split(",").length; i++) {
						String rawEnch = element.getAttribute("enchantment").split(",")[i];
						String enchantment = rawEnch.split(":")[0];
						String level = rawEnch.split(":")[1];
						Enchantment e = Enchantment.getByName(enchantment);
						item.addUnsafeEnchantment(e, Integer.parseInt(level));
						System.out.println("Enchantment: "+enchantment+", level: "+level);
					}
				}
				if (element.hasAttribute("displayName")) {
					ItemMeta itemMeta = item.getItemMeta();
					itemMeta.setDisplayName(element.getAttribute("displayName"));
					item.setItemMeta(itemMeta);
				}
				data.blueInv.put(itemData[0], item);
			}
		}
		data.redSpawn = new Location(world,Integer.parseInt(redSpawnData[0]),Integer.parseInt(redSpawnData[1]),Integer.parseInt(redSpawnData[2]));
		data.blueSpawn = new Location(world,Integer.parseInt(blueSpawnData[0]),Integer.parseInt(blueSpawnData[1]),Integer.parseInt(blueSpawnData[2]));
		//Teams
		String[] colors = {"aqua", "black", "blue", "darkaqua", "darkblue", "darkgray", "darkgreen", "darkpurple",
                "darkred", "gold", "gray", "green", "lightpurple", "red", "white", "yellow"};
        	ChatColor[] colorsC = {ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA,
                ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE,
                ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN,
                ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.WHITE, ChatColor.YELLOW};
        if (notExists(ed, "teamOne")) throw new Exception("Tag \"teamOne\" doesn't exist");
        Node teamOneNode = doc.getElementsByTagName("teamOne").item(0);
		if (teamOneNode.getNodeType() == Node.ELEMENT_NODE) {
			Element teamOneElement = (Element) teamOneNode;
			String teamOneName = teamOneElement.getElementsByTagName("name").item(0).getTextContent();
			String teamOneMaxSize = teamOneElement.getElementsByTagName("maxSize").item(0).getTextContent();
			String teamOneColorRAW = teamOneElement.getElementsByTagName("color").item(0).getTextContent();
			String joinArgOne = teamOneElement.getElementsByTagName("joinArg").item(0).getTextContent();
			ChatColor teamOneColor = ChatColor.RED;
			for (int i = 0; i < colors.length; i++) {
				
				if (colors[i].equals(teamOneColorRAW)) {
					
					teamOneColor = colorsC[i];
				}
			}
			data.teamOne = new TeamData(teamOneName, Integer.parseInt(teamOneMaxSize), teamOneColor);
			data.teamOne.joinArg = joinArgOne;
		}
		if (notExists(ed, "teamTwo")) throw new Exception("Tag \"teamTwo\" doesn't exist");
		Node teamTwoNode = doc.getElementsByTagName("teamTwo").item(0);
		if (teamTwoNode.getNodeType() == Node.ELEMENT_NODE) {
			Element teamTwoElement = (Element) teamTwoNode;
			String teamTwoName = teamTwoElement.getElementsByTagName("name").item(0).getTextContent();
			String teamTwoMaxSize = teamTwoElement.getElementsByTagName("maxSize").item(0).getTextContent();
			String teamTwoColorRAW = teamTwoElement.getElementsByTagName("color").item(0).getTextContent();
			String joinArgTwo = teamTwoElement.getElementsByTagName("joinArg").item(0).getTextContent();
			ChatColor teamTwoColor = ChatColor.BLUE;
			for (int i = 0; i < colors.length; i++) {
				if (colors[i].equals(teamTwoColorRAW)) {
					teamTwoColor = colorsC[i];
				}
			}
			data.teamTwo = new TeamData(teamTwoName, Integer.parseInt(teamTwoMaxSize), teamTwoColor);
			data.teamTwo.joinArg = joinArgTwo;
		}
		return data;
		
	}
	 
	public static boolean notExists(Element node, String str) {
		if (node.getElementsByTagName(str).getLength() < 1) {
			return true;
		}
		return false;
	}
}
