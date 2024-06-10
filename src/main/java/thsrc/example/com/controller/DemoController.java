package thsrc.example.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import thsrc.example.com.entity.Demo;
import thsrc.example.com.service.DemoService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DemoController {
	@Autowired
	private DemoService service;
	
	@GetMapping("/addRandom")
	public String addRandomStudent() {
		for(int i=0;i<10;i++) {
			service.addDemo();
		}
		
		return service.getAll().toString();
	}
	
	@GetMapping("/get")
	public String getStudent(@RequestParam("id") long id) {
		return service.getDemo(id).toString();
	}
	
	@GetMapping("/add")
	public String addStudent(@RequestParam("name") String name,@RequestParam("score") int score) {
		Demo s = new Demo();
		s.setName(name);
		s.setMathScore(score);
		
		service.addDemo(s);
		
		List<Demo> list = service.getAll();
		
		return list.get(list.size()-1).toString();
	}

}
