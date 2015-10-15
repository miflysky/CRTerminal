package com.tieto.crterminal.model.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tieto.crterminal.model.player.GamePlayer;

public class JsonCommandBuilder {
	
	
	public static JsonCRTCommand buildNewRoundCommand(int roundNumber) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_INT_NEWROUND);
		command.setValue(String.valueOf(roundNumber));
		return command;
	}

	public static JsonCRTCommand buildEndRoundCommand(int roundNumber, ArrayList<GamePlayer> winers, ArrayList<GamePlayer> loser) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_INT_ENDROUND);
		try{
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("numbers", roundNumber);
			command.setValue(jsonObject.toString());
			
			JSONArray winArray = new JSONArray();
			for (GamePlayer gamePlayer : winers) {
				winArray.put(gamePlayer.mName);
			}
			
			jsonObject.put("winers", winArray.toString());
			
			JSONArray loserArray = new JSONArray();
			for (GamePlayer gamePlayer : loser) {
				loserArray.put(gamePlayer.mName);
			}
			
			jsonObject.put("losers", winArray.toString());
			
			command.setValue(String.valueOf(roundNumber));
			return command;

		}catch(Exception e){
			
		}
		return null;
	}

	public static void getResutlt(String value, HashMap<String, GamePlayer> playersMap
			, ArrayList<GamePlayer> winArrayList, ArrayList<GamePlayer> lostArrayList) {
		try {
			JSONObject jsonObject = new JSONObject(value);
			//get winer
			String winString = jsonObject.getString("winers");
			JSONArray winArray = new JSONArray(winString);
			for(int i = 0; i < winArray.length() ; i++ ){
				String userName = (String) winArray.get(0);
				if(playersMap.containsKey(userName));{
					winArrayList.add(playersMap.get(userName));
				}
			}
			
			//get loster
			String lostString = jsonObject.getString("losers");
			JSONArray lostJsonArray = new JSONArray(lostString);
			for(int i = 0; i < lostJsonArray.length() ; i++ ){
				String userName = (String) lostJsonArray.get(0);
				if(playersMap.containsKey(userName));{
					lostArrayList.add(playersMap.get(userName));
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	public static JsonCRTCommand buildJanKenPonValueCommand(String name, int value) {
		try {
			JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_STR_CHOOSE);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("username", name);
			jsonObject.put("value", value);
			command.setValue(jsonObject.toString());
			return command;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static JsonCRTCommand buildStartGameCommand() {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_NULL_STARTGAME);
		return command;
	}
	
	public static JsonCRTCommand buildEndGameCommand() {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_NULL_ENDGAME);
		return command;
	}
	
	
	
	public static JsonCRTCommand buildJoinGameCommand(String name) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_STR_JOIN);
		command.setValue(name);
		return command;
	}
	
	public static JsonCRTCommand buildLeaveGameCommand(String name) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_STR_LEAVE);
		command.setValue(name);
		return command;
	}

	public static JsonCRTCommand buildPlayerListCommandplayersMap(
			HashMap<String, GamePlayer> playersMap) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_STR_PLAYER_LIST);
		JSONArray jsonArray = new JSONArray();

		Iterator<Entry<String, GamePlayer>> iterator = playersMap.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String,GamePlayer> entry = iterator.next();
			jsonArray.put(buildJoinGameCommand(entry.getKey()).toString());
			if(entry.getValue().status == GamePlayer.READY){
				jsonArray.put(buildJanKenPonValueCommand(entry.getKey(),entry.getValue().status));
			}
		}
		command.setValue(jsonArray.toString());
		return command;
	}

	public static ArrayList<JsonCRTCommand> getPalyerList(String value) {
		try {
			ArrayList<JsonCRTCommand> commands = new ArrayList<JsonCRTCommand>();
			JSONArray jsonArray = new JSONArray(value);
			for(int i = 0 ; i < jsonArray.length(); i++){
				JsonCRTCommand command = new JsonCRTCommand(jsonArray.getInt(i));
				commands.add(command);
			}
			return commands;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getChooseValue(String value) {
		try {
			JSONObject jsonObject = new JSONObject(value);
			return jsonObject.getString("username");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String getChooseName(String value) {
		try {
			JSONObject jsonObject = new JSONObject(value);
			return jsonObject.getString("value");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";	}

}
