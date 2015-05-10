package com.ims.entity;

public class MeetingData {
	private String title;
	private String meetingData;
	
	
	public MeetingData(String title, String meetingData) {
		this.title = title;
		this.meetingData = meetingData;
	}
	
	public MeetingData() {
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMeetingData() {
		return meetingData;
	}
	public void setMeetingData(String meetingData) {
		this.meetingData = meetingData;
	}
	@Override
	public String toString() {
		return "MeetingData [title=" + title + ", meetingData=" + meetingData
				+ "]";
	}
	
}
