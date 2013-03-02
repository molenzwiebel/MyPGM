package nl.thijsmolendijk.MyPGM.MapData;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import nl.thijsmolendijk.MyPGM.Cores.Core;
import nl.thijsmolendijk.MyPGM.Cores.CoreManager;
import nl.thijsmolendijk.MyPGM.Teams.TeamData;
import nl.thijsmolendijk.MyPGM.Teams.TeamManager;

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
	static String[] colors = {"aqua", "black", "blue", "darkaqua", "darkblue", "darkgray", "darkgreen", "darkpurple",
            "darkred", "gold", "gray", "green", "lightpurple", "red", "white", "yellow"};
    static ChatColor[] colorsC = {ChatColor.AQUA, ChatColor.BLACK, ChatColor.BLUE, ChatColor.DARK_AQUA,
            ChatColor.DARK_BLUE, ChatColor.DARK_GRAY, ChatColor.DARK_GREEN, ChatColor.DARK_PURPLE,
            ChatColor.DARK_RED, ChatColor.GOLD, ChatColor.GRAY, ChatColor.GREEN,
            ChatColor.LIGHT_PURPLE, ChatColor.RED, ChatColor.WHITE, ChatColor.YELLOW};
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
	 
		MapData data = new MapData("", "", "", 0);
		if (notExists(ed, "author")) throw new Exception("Tag \"author\" doesn't exist");
		data.author = doc.getElementsByTagName("author").item(0).getTextContent();
		
		if (notExists(ed, "objective")) throw new Exception("Tag \"objective\" doesn't exist");
		data.objective = doc.getElementsByTagName("objective").item(0).getTextContent();
		
		if (notExists(ed, "type")) throw new Exception("Tag \"type\" doesn't exist");
		data.gameType = doc.getElementsByTagName("type").item(0).getTextContent();
		
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
		data.cores = new CoreManager();
		
        
		if (notExists(ed, "changeBowProjectile")) { data.changeBowProjectile = false; } else {
			Node bowNode = doc.getElementsByTagName("changeBowProjectile").item(0);
			if (bowNode.getNodeType() == Node.ELEMENT_NODE) {
				Element bowElement = (Element) bowNode;
				data.changeBowProjectile = true;
				data.newBowEntity = bowElement.getElementsByTagName("entity").item(0).getTextContent();
				data.newBowVelocity = Float.parseFloat(bowElement.getElementsByTagName("velocity").item(0).getTextContent());
			}
		}
		data.teams = new TeamManager();
		data = addCores(doc, world, data);
		data = addTeams(doc, world, data);
		return data;
		
	}
	 
	public static boolean notExists(Element node, String str) {
		if (node.getElementsByTagName(str).getLength() < 1) {
			return true;
		}
		return false;
	}
	public static MapData addCores(Document doc, World world, MapData map) throws Exception {
		Element ed = doc.getDocumentElement();
		if (notExists(ed, "cores")) throw new Exception("No cores found");
		Node coreNode = doc.getElementsByTagName("cores").item(0);
		if (coreNode.getNodeType() == Node.ELEMENT_NODE) {
			Element coreElement = (Element) coreNode;
			NodeList cores = coreElement.getChildNodes();
			for (int i = 0; i < cores.getLength(); i++) {
				Node node = cores.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) node;
					String id = e.getAttribute("id");
					if (map.cores.coreByID(id) != null) throw new Exception("Each core should have a individual id");
					String[] spawnData = e.getElementsByTagName("centerLocation").item(0).getTextContent().split(":");
					Location center = new Location(world,Integer.parseInt(spawnData[0]),Integer.parseInt(spawnData[1]),Integer.parseInt(spawnData[2]));
					int radius = Integer.parseInt(e.getElementsByTagName("radius").item(0).getTextContent());
					boolean red = Boolean.parseBoolean(e.getElementsByTagName("redTeam").item(0).getTextContent());
					Core c = new Core(id, null, center, radius, red);
					map.cores.addCore(c);
					System.out.println(map.cores);
				}
			}
		}
		return map;
	}
	public static MapData addTeams(Document doc, World world, MapData map) throws Exception {
		Element ed = doc.getDocumentElement();
		if (notExists(ed, "teams")) throw new Exception("No teams found");
		Node coreNode = doc.getElementsByTagName("teams").item(0);
		if (coreNode.getNodeType() == Node.ELEMENT_NODE) {
			Element coreElement = (Element) coreNode;
			NodeList cores = coreElement.getChildNodes();
			for (int i = 0; i < cores.getLength(); i++) {
				Node node = cores.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) node;
					String id = e.getAttribute("id");
					if (map.teams.teamForID(id) != null) throw new Exception("Each team should have a individual id");
					String name = e.getElementsByTagName("name").item(0).getTextContent();
					String rawColor = e.getElementsByTagName("color").item(0).getTextContent();
					String joinArg = e.getElementsByTagName("joinArg").item(0).getTextContent();
					int size = Integer.parseInt(e.getElementsByTagName("size").item(0).getTextContent());
					String[] spawnData = e.getElementsByTagName("spawn").item(0).getTextContent().split(":");
					Location center = new Location(world,Integer.parseInt(spawnData[0]),Integer.parseInt(spawnData[1]),Integer.parseInt(spawnData[2]));
					ChatColor color = ChatColor.RED;
					for (int i1 = 0; i1 < colors.length; i1++) {
						
						if (colors[i1].equals(rawColor)) {
							
							color = colorsC[i1];
						}
					}
					TeamData teamData = new TeamData(name, size, color);
					teamData.id = id;
					teamData.spawn = center;
					teamData.joinArg = joinArg;
					teamData.spawnInventory = addInv(((Element) e.getElementsByTagName("inventory").item(0)).getChildNodes());
					map.teams.addTeam(teamData);
				}
			}
		}
		map.teams.logTeams();
		return map;
	}
	public static HashMap<String, ItemStack> addInv(NodeList redInv) {
		HashMap<String, ItemStack> data = new HashMap<String, ItemStack>();
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
				data.put(itemData[0], item);
			}
		}
		return data;
	}
}
