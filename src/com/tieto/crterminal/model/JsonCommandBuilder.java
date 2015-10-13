package com.tieto.crterminal.model;

public class JsonCommandBuilder {
	public static final String NEW_ROUND_COMMAND = "newRound";
	public static final String END_ROUND_COMMAND = "endRound";

	
	public static JsonCRTCommand buildNewRoundCommand(int roundNumber) {
		JsonCRTCommand command = new JsonCRTCommand();
		command.commandName = NEW_ROUND_COMMAND;
		command.value = String.valueOf(roundNumber);
		return command;
	}

	public static JsonCRTCommand buildEndRoundCommand(int roundNumber) {
		JsonCRTCommand command = new JsonCRTCommand();
		command.commandName = END_ROUND_COMMAND;
		command.value = String.valueOf(roundNumber);
		return command;
	}

	public static JsonCRTCommand buildSetFingerValueCommand(JanKenPonValue paper) {
		// TODO Auto-generated method stub
		return null;
	}

}
