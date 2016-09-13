package gr.aueb.jade.test.rma;

public interface RequestOutcomeCallback {
	
	public void onInform(String content);
	
	public void onFailure(String conent);

}
