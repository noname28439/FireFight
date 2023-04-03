package util;


import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.lang.Nullable;
import main.Main;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;

import java.util.HashMap;

import static com.mongodb.client.model.Filters.eq;

public class DBHandler {

    protected static MongoCollection<Document> accCol, teamCol;

    protected static MongoClient mongoClient;

    public static void load(){
        try {
            String connectURL = Main.plugin.getConfig().getString("dbUrl");
            if(connectURL==null){
                Main.plugin.getConfig().set("dbUrl", "");
                Main.plugin.saveConfig();
                throw new Exception("No dbUrl specified in Config");
            }
            mongoClient = new MongoClient(new MongoClientURI(connectURL));

            MongoDatabase db = mongoClient.getDatabase("mgt_prod");
            accCol = db.getCollection("account");
            teamCol = db.getCollection("team");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unload(){
        mongoClient.close();
    }


    public static String teamNameByUserName(String username) {
        if(!userTeams.containsKey(username)) loadUserTeam(username);

        return userTeams.get(username);
    }

    static HashMap<String, String> userTeams = new HashMap<>();

    public static void loadUserTeam(String username){
        String teamName = fetchTeamName(username);
        if(teamName == null) teamName = "...";
        userTeams.put(username, teamName);
    }

    @Nullable
    protected static String fetchTeamName(String username){
        Document accDoc = accCol.find(eq("username", username)).first();
        if (accDoc != null) {
            Object teamIDObj = accDoc.get("team");
            if(teamIDObj==null) return null;
            String teamID = teamIDObj.toString();
            return teamCol.find(eq("_id", new ObjectId(teamID))).first().get("short_name").toString();
        } else {}
        return null;
    }


}
