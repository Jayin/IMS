package com.ims.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.ims.entity.Problem;

/**
 * 处理问题文件，完成对文件的增 和 遍历,一行保存一个问题记录，每一个字段用@@@隔开
 * 对文件的处理不应该同时写，考虑到异步什么的，用单例，写的方法全部上锁。
 * 
 * @author Administrator
 * 
 */
public class ProblemFile {
	File file = null;

	public ProblemFile() {
		file = new File(getTmpProblemFile());
	}

	/**
	 * 根据操作系统获取problem.xml路径
	 * @return
	 */
	public static String getTmpProblemFile(){
		String os_name = System.getProperty("os.name").toLowerCase();
		if(os_name.contains("windows")){
			return "c:/problem.xml";
		}
		return "/tmp/problem.xml";
	}

	// 增加一个问题
	public void create(Problem problem) {
		try {
			FileOutputStream out = new FileOutputStream(file, true);
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					out));
			writer.write(problem.getUsername() + "@@@" + problem.getProblemCtn()
					+ "\r\n");
			writer.close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// 拿到全部问题
	public Set<Problem> getAll() {
		
		Set<Problem> set = null;
		
		try {
			set = new HashSet<Problem>();
			FileInputStream in = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			String line;
			while((line=reader.readLine())!=null){
				String[] strs = line.split("@@@");
				if(strs.length<2){
					continue;
				}
				Problem p = new Problem(strs[0], strs[1]);
				set.add(p);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return set;
	}

	public static void main(String[] args) {
		Set<Problem> set = new ProblemFile().getAll();
		Iterator<Problem> i = set.iterator();
		while(i.hasNext()){
			System.out.println(i.next());
		}
	}

}
