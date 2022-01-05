package com.example.musicat.service.etc;

import java.util.ArrayList;

import com.example.musicat.domain.etc.DailyStatisticsVO;
import com.example.musicat.repository.etc.DailyStatisticsDao;
import com.example.musicat.repository.etc.DailyStatisticsDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("dailyService")
public class DailyStatisticsServiceImpl implements DailyStatisticsService {

	@Autowired
	private DailyStatisticsDaoImpl dailyDao;
	
	@Override
	public DailyStatisticsVO retrieve() {
		return this.dailyDao.selectDaily();
	}

	@Override
	public ArrayList<DailyStatisticsVO> retrieveList() {
		return this.dailyDao.selectList();
	}

	@Override
	public void registerAndmodifyDaily() {
		
		this.dailyDao.insertAndupdateDaily();

	}


}
