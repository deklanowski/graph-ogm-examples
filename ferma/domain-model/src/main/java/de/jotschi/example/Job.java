package de.jotschi.example;

public class Job extends BaseVertex implements IJob {

	public static final String HAS_JOB = "HAS_JOB";

	@Override
	public String getJobName() {
		return getProperty("name");
	}

}
