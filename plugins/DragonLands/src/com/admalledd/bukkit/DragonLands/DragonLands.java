
package com.admalledd.bukkit.DragonLands;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
/**
 * Sample plugin for Bukkit
 *
 * @author admalledd
 */
public class DragonLands extends JavaPlugin implements Listener {
	private double state_locx;
	private double state_locz;
	
	//strings from config, comment format is "key<type>" (and thus why sub is <Object>)
	private List<HashMap<String, Object>> directions; //"string<String>", "min<int>","max<int>"
	private List<HashMap<String, Object>> distances;  //"string<String>", "min<int>","max<int>"
	private List<HashMap<String, Object>> lore;       //"string<String>"
	private List<HashMap<String, Object>> teleport;   //"string<String>"
	private List<HashMap<String, Object>> toofar;     //"string<String>"
	
    public void onDisable() {
    	PluginDescriptionFile pdfFile = this.getDescription();
        getLogger().info(pdfFile.getName() + " version " + pdfFile.getVersion() + " is disabled.");
    }

    public void onEnable() {

    	//first thing first, if our folder does not exist create it!
    	
    	if (!getDataFolder().exists()){
    		getDataFolder().mkdir();
    	}
    	
    	loadConfig();
    	try {
			loadState();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	getServer().getPluginManager().registerEvents(this, this);
        
        PluginDescriptionFile pdfFile = this.getDescription();
        getLogger().info( pdfFile.getName() + " version " + pdfFile.getVersion() + " is enabled" );
        
    }
    
    /*
     * Dragon commands:
     *  "/dragon": teleport (if in rage) to dragon_lands
     *  "/dragon regen": OP/admin only, regenerate where the hole in the world is (assumed that new world also made)
     * 
     * */
	@SuppressWarnings("unchecked")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("dragon")){
    		if (args.length == 0 && sender instanceof Player){
    			//default command and we are a player (console has no use for this command)
    			Location ploc = ((Player) sender).getLocation();
    			getLogger().info("player ran dragon command");
    			double dist =Math.hypot((ploc.getX()-state_locx), (ploc.getZ()-state_locz)); 
    			if (dist< getConfig().getInt("settings.zone.size")){
    				sender.sendMessage(getRandMsg(teleport));
    				((Player)sender).teleport(getServer().getWorld("dragon_lands").getSpawnLocation());
    			}else{
    				sender.sendMessage(getRandMsg(toofar));
    			}
    			
    		}
    		else if (args.length == 1 && args[0].equalsIgnoreCase("regen")) {
    			generateZone();
    			sender.sendMessage(String.format("Generated new dragon zone at: (%f,%f)",state_locx,state_locz));
    		}
    		return true;
    	}
    	return false;
    }
    @SuppressWarnings("unchecked")
	private void loadConfig(){
		getLogger().info(String.format("itemID: %s", getConfig().getString("settings.guide.itemMaterial")));
		getLogger().info(String.format("Zone size: %f", getConfig().getDouble("settings.zone.size")));
		//System.out.println(getConfig().getStringList("strings.teleport"));
		directions = (List<HashMap<String, Object>>) getConfig().getList("strings.directions");
		if (directions ==  null){getLogger().warning("unable to load directions! might crash later!"); }
		distances  = (List<HashMap<String, Object>>) getConfig().getList("strings.distances");
		if (distances  ==  null){getLogger().warning("unable to load distances! might crash later!"); }
		teleport   = (List<HashMap<String, Object>>) getConfig().getList("strings.teleport");
		if (teleport   ==  null){getLogger().warning("unable to load teleports! might crash later!"); }
		toofar     = (List<HashMap<String, Object>>) getConfig().getList("strings.toofar");
		if (toofar     ==  null){getLogger().warning("unable to load toofars! might crash later!"); }
		lore       = (List<HashMap<String, Object>>) getConfig().getList("strings.lore");
		if (lore       ==  null){getLogger().warning("unable to load lores! might crash later!"); }
		
    }
    
    /*
     * State format: 
     * 
     * 1. x loc of zone
     * 2. z loc of zone
     * 
     * */
    private void loadState() throws NumberFormatException, IOException {
		File stateFile = new File(this.getDataFolder(), "state.drg");

		if (!stateFile.exists() || !stateFile.canRead()) {
			getLogger().log(Level.WARNING, "{0} does not exist or is not accessible, assuming bad generation and thus recreating", stateFile.getPath());
			generateZone();
		}
		else{
	
			Reader reader = new FileReader(stateFile);
			BufferedReader bufReader = new BufferedReader(reader);
			
			state_locx=Double.parseDouble(bufReader.readLine().trim());
			state_locz=Double.parseDouble(bufReader.readLine().trim());
			getLogger().info(String.format("Loaded zone at (x:%f,z:%f)", state_locx,state_locz));
			bufReader.close();
			reader.close();
		}
    }
    private void saveState() throws IOException{
    	File stateFile = new File(this.getDataFolder(),"state.drg");
    	//TODO:: assume we can write whatever we want, just error otherwise, its up to the sysadmin to fix that.
    	BufferedWriter wr = new BufferedWriter(new FileWriter(stateFile));
    	wr.write(Double.toString(state_locx));
    	wr.write("\n");
    	wr.write(Double.toString(state_locz));
    	wr.write("\n");
    	wr.close();
    }
	/*
	 * create zone given params in config.yml
	 * (radius min, radius max)
	 * 
	 * */
    private void generateZone() {
		double theta= Math.PI * RandomUtils.nextDouble(); //angle from spawn
		int max_dist=getConfig().getInt("settings.zone.spawn_radius_max");
		int min_dist=getConfig().getInt("settings.zone.spawn_radius_min");
		int radii = RandomUtils.nextInt((max_dist-min_dist)+1)+min_dist; //distance from spawn
		state_locx= radii*Math.cos(theta);
		state_locz= radii*Math.sin(theta);
		getLogger().info(String.format("Created zone at (x:%d,z:%d)", state_locx,state_locz));
		try {
			saveState();//write out to file, in a diff func in case state expands later
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
	//---------------EVENT HANDLING
    
    /*
     * 1. handle players rejoining
     * 2. handle players using the guide
     * 
     * */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
    	Player p = event.getPlayer();
    	if (p.getWorld().getName().equalsIgnoreCase("dragon_lands") || 
    		p.getWorld().getName().equalsIgnoreCase("dragon_lands_nether")||
    		p.getWorld().getName().equalsIgnoreCase("dragon_lands_the_end")){
    		//in worlds, not allowed teleport them back!
    		Location wloc = getServer().getWorld(getConfig().getString("settings.respawn.world_name")).getSpawnLocation();
    		p.teleport(wloc);
    	}
    }
    
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		 Player p = event.getPlayer();
		 if (!p.getWorld().getName().equalsIgnoreCase(getConfig().getString("settings.guide.active_world_name"))){
			 //not the same world as the one the "zone" is in
			 return;
		 }
		 if(p.getItemInHand().getType() == Material.EYE_OF_ENDER) {
			 
			 if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
				 //p.sendMessage("EYE_OF_ENDER right_click"); //DEBUG
				 event.setCancelled(true);
				 Location ploc = p.getLocation();
				 double dist = Math.hypot((ploc.getX()-state_locx), (ploc.getZ()-state_locz));
				 double theta = Math.toDegrees(Math.atan2(ploc.getZ()- state_locz,ploc.getX()-state_locx));
				 float yaw = ploc.getYaw()+90;
				 while (yaw >180 || yaw < -180){
					 if (yaw >180){
						 yaw-=360;
					 }
					 else if (yaw < -180){
						 yaw+=360;
					 }
				 }
				 theta = yaw-theta+180;
				 while (theta >180 || theta < -180){
					 if (theta >180){
						 theta -=360;
					 }
					 else if (theta < -180){
						 theta+=360;
					 }
				 }
				 String msg = getMsg(dist, theta, p);
				 p.sendMessage(String.format("%s%s%s", ChatColor.GRAY,ChatColor.ITALIC,msg));
			 }
		 }
	}

	//-------HELPERS
	/*
	 * get a random message string from a list of possibles
	 * 
	 * */
	private String getRandMsg(List<HashMap<String,Object>> li){
		return (String) li.get(RandomUtils.nextInt(li.size())).get("string");
	}
	
	@SuppressWarnings("unchecked")
	private String getMsg(double dist,final double theta,Player player){
		String msg="This is a test string!";
		
		int choice = RandomUtils.nextInt(2); //choose between distances, directions and lores
		
		ArrayList<HashMap<String,Object>> good_msgs = new ArrayList<HashMap<String,Object>>();
		if (choice == 0){
			//choose Directions
			for (HashMap<String, Object> tmsg : directions){
				if ( (Integer)tmsg.get("min") <theta && (Integer)tmsg.get("max")>theta){
					good_msgs.add(tmsg);
				}
			}
			//select best angle candidate
			Comparator comp = new Comparator(){
				public int compare(Object o1, Object o2) {
					HashMap<String,Integer> m1 = ((HashMap<String,Integer>) o1);
					HashMap<String,Integer> m2 = ((HashMap<String,Integer>) o2);
					
					//min diff of m1 from theta
					double m1_delta = Math.min(Math.abs(m1.get("min")-theta),Math.abs(m1.get("max")-theta));
					
					double m2_delta = Math.min(Math.abs(m2.get("min")-theta),Math.abs(m2.get("max")-theta));
					
					if (m1_delta < m2_delta){
						return -1;
					}
					else if (m1_delta == m2_delta){
						return 0;
					}
					else if (m1_delta > m2_delta){
						return 1;
					}
					getLogger().warning("compare failed for some reason!");
					return 5; //something bad happened here!
				}
				
			};
			msg = (String) Collections.min(good_msgs, comp).get("string");
			msg = String.format("%s%s:%s%d",msg,ChatColor.RESET,ChatColor.LIGHT_PURPLE,Double.valueOf(theta).intValue());
		}
		else if (choice == 1){
			//choose Distances
			for (HashMap<String, Object> tmsg : distances){
				if ( (Integer)tmsg.get("min") <dist && (Integer)tmsg.get("max")>dist){
					good_msgs.add(tmsg);
				}
			}
			msg = getRandMsg(good_msgs);
			msg = String.format("%s%s:%s%d",msg,ChatColor.RESET,ChatColor.LIGHT_PURPLE,Double.valueOf(dist).intValue());
		}
		else if (choice == 2){
			//choose Lore
			msg = getRandMsg(lore);
		}
		else{
			//should never happen right?
			getLogger().warning("Choicer failure! wtf just happened?");
			msg="msg get fail D:";
		}
		
		return msg;
	}
}
