package thsrc.example.com.service;

import thsrc.example.com.entity.Demo;

import java.util.List;

public interface DemoService {
	void addDemo();

	void addDemo(Demo s);

	Demo getDemo(long id);

	List<Demo> getAll();
}
