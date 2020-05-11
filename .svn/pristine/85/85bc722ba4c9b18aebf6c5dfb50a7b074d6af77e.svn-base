package com.enjoy.traffic.test;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Test {

	public static void main(String[] args) {
		ConcurrentLinkedQueue<String> con = new ConcurrentLinkedQueue<String>();
		ConcurrentLinkedQueue<String> consave = new ConcurrentLinkedQueue<String>();
		for(int i=0;i<3;i++){
			con.add("con:"+i);
			consave.add("consave:"+i);
		}
		while(true){
			if(con.size()>0){
				try {
					String str=con.poll();
					System.out.println(str);
					System.out.println(consave.poll());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
