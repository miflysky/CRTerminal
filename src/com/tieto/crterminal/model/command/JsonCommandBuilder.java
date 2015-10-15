package com.tieto.crterminal.model.command;

public class JsonCommandBuilder {
	
	
	public static JsonCRTCommand buildNewRoundCommand(int roundNumber) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_INT_NEWROUND);
		command.setValue(roundNumber);
		return command;
	}

	public static JsonCRTCommand buildEndRoundCommand(int roundNumber) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_INT_ENDROUND);
		command.setValue(roundNumber);
		return command;
	}

	public static JsonCRTCommand buildJanKenPonValueCommand(int value) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.EVENT_STR_CHOOSE);	
		command.setValue(value);
		return command;
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

	
	
	
	
	
}
