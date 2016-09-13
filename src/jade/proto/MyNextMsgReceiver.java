package jade.proto;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.proto.states.MsgReceiver;
import jade.proto.states.ReplySender;

public class MyNextMsgReceiver extends MsgReceiver {

	public boolean deadlineExpired = false;
	
	public static final int INFINITE = -1;
	
	SSResponder theResponder;
	
	public MyNextMsgReceiver(Agent a, SSResponder b) {
		super(a, null, INFINITE, null, null);
		
		receivedMsgKey = "__Received_key" + b.hashCode();
		setDataStore(b.getDataStore());
		theResponder = b;
		
	}

	public int onEnd() {
		// The next reply (if any) will be a reply to the received message
		SSResponder parent = (SSResponder) getParent();
		
		ReplySender rs = (ReplySender) parent.getState(SSResponder.SEND_REPLY);
		rs.setMsgKey((String) receivedMsgKey);

		return super.onEnd();
	}
	
	@Override
	protected void handleMessage(ACLMessage msg) {
		if (msg == null){
			deadlineExpired = true;
			System.out.println("Contract Net receiver: deadline expired");
		}
	}
	
	public void registerWithSSResponder(){
		theResponder.registerDSState(this, SSResponder.RECEIVE_NEXT);
	}
} // End of inner class NextMsgReceiver
