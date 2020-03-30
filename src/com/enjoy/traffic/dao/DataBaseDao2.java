package com.enjoy.traffic.dao;

import java.util.LinkedList;
import java.util.List;

public interface DataBaseDao2 {
	public List executeQuery(String sql);
	public int save(String sql);
	public boolean create(String sql);
}
