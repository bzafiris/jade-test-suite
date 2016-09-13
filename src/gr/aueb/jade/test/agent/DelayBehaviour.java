package gr.aueb.jade.test.agent;

import jade.core.behaviours.OneShotBehaviour;

public class DelayBehaviour extends OneShotBehaviour {

	long delay;

	public DelayBehaviour(long delay) {

		this.delay = delay;
	}

	@Override
	public void action() {

		System.out.println("Waiting for " + delay/1000 + "s");
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Delayed for " + delay);

	}
}