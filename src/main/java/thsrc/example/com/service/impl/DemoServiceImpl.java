package thsrc.example.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thsrc.example.com.dao.DemoDao;
import thsrc.example.com.entity.Demo;
import thsrc.example.com.service.DemoService;

import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {

	@Autowired
	private DemoDao DemoDao;

	private String[] firstNames = {"Camiol","Joyce","Lucy","Ruth","Louis"};

	private String[] lastNames = {"Chuang","Lin","Wang","Chen","Zhang"};

	@Override
	public void addDemo() {
		Demo s = new Demo();
		String firstName = firstNames[(int) (Math.random()*firstNames.length)];
		String lastName = lastNames[(int) (Math.random()*lastNames.length)];
		int score = (int)(Math.random()*100)+1;

		s.setName(firstName+" "+lastName);
		s.setMathScore(score);
		System.out.println(s);
		DemoDao.save(s);
	}

	@Override
	public void addDemo(Demo s) {

	}

	@Override
	public Demo getDemo(long id) {
		Demo s = DemoDao.findById(id).orElse(new Demo());
		return s;
	}

	@Override
	public List<Demo> getAll() {
		return DemoDao.findAll();
	}

}
