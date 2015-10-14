package com.tieto.crterminal.model.Command;

public class JsonCommandBuilder {
	
	
	public static JsonCRTCommand buildNewRoundCommand(int roundNumber) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.FROM_SERVER_EVENT_INT_NEWROUND);
		command.setValue(roundNumber);
		return command;
	}

	public static JsonCRTCommand buildEndRoundCommand(int roundNumber) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.FROM_SERVER_EVENT_INT_ENDROUND);
		command.setValue(roundNumber);
		return command;
	}

	public static JsonCRTCommand buildJanKenPonValueCommand(int value) {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.FROM_CLIENT_EVENT_CHOICE);	
		command.setValue(value);
		return command;
	}

	
	public static JsonCRTCommand buildStartGameCommand() {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.FROM_SERVER_EVENT_NULL_STARTGAME);
		return command;
	}
	
	public static JsonCRTCommand buildEndGameCommand() {
		JsonCRTCommand command = new JsonCRTCommand(JsonCommadConstant.FROM_SERVER_EVENT_NULL_GAMERESULT);
		return command;
	}
	
	
	
	
}
