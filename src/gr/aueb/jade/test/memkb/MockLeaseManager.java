package gr.aueb.jade.test.memkb;

import java.util.Date;

import jade.domain.KBManagement.LeaseManager;

public class MockLeaseManager implements LeaseManager {

	@Override
	public Date getLeaseTime(Object item) {
		return null;
	}

	@Override
	public void setLeaseTime(Object item, Date lease) {

	}

	@Override
	public Object grantLeaseTime(Object item) {
		return null;
	}

	@Override
	public boolean isExpired(Date lease) {
		return false;
	}

}
