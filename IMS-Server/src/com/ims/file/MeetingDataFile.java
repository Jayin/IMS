package com.ims.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.ims.common.FilePath;
import com.ims.entity.MeetingData;

public class MeetingDataFile {
	File file = null;

	public MeetingDataFile() {
		file = new File(FilePath.getMeetingDataFile());
	}

	// 增加一个问题
	public void create(MeetingData meetingData) {
		try {
			FileOutputStream out = new FileOutputStream(file, false);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					out));
			writer.write(meetingData.getTitle() + "\r\n");
			writer.write(meetingData.getMeetingData());
			writer.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public MeetingData getMeetingData() {
		System.out.println("下载会议资料1");
		MeetingData meetingData;

		try {
			meetingData = new MeetingData();
			FileInputStream in = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line;
			if ((line = reader.readLine()) != null) {
				meetingData.setTitle(line);
			}

			String meetingDataCtn = "";
			while ((line = reader.readLine()) != null) {
				meetingDataCtn += line + "\r\n";
			}

			meetingData.setMeetingData(meetingDataCtn);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return meetingData;
	}

	public static void main(String[] args) {

		System.out.println(new MeetingDataFile().getMeetingData());
	}
}
