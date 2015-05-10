package com.ims.entity;

public class Problem {
	private String username;
	private String problemCtn;
	
	
	
	public Problem() {
	}
	public Problem(String username, String problemCtn) {
		this.username = username;
		this.problemCtn = problemCtn;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProblemCtn() {
		return problemCtn;
	}
	public void setProblemCtn(String problemCtn) {
		this.problemCtn = problemCtn;
	}
	@Override
	public String toString() {
		return "Problem [username=" + username + ", problemCtn=" + problemCtn
				+ "]";
	}
	
}
