package com.tieto.crterminal.model.command;

public class JsonCommandParserHost {

	
	private JsonCRTCommand mJsonCRTCommand;
	
	private int mEvent;
	
	
	public JsonCommandParserHost(String msg){
		mJsonCRTCommand = new JsonCRTCommand(msg);
	}
	
	public void parse(){
		
		mEvent = mJsonCRTCommand.getEvent();
		
		/*
		switch(mEvent):
			case JsonCommadConstant.FROM_CLIENT_EVENT_STR_JOIN:
			break;
		
		if()
		*/
		
		
		
	}
	
	
	
}
