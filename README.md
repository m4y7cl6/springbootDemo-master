# Spring boot jpa & swagger-ui
![image](https://github.com/m4y7cl6/springbootDemo-master/assets/17524282/32fa0b92-d104-4ac1-83fa-b114452ece8e)


## Pom.xml
```!
<dependency>
    <groupId>org.springdoc</groupId>
	<!-- with UI -->
	<artifactId>springdoc-openapi-ui</artifactId>
	<version>1.5.8</version>
</dependency>
<dependency>
	<groupId>com.microsoft.sqlserver</groupId>
	<artifactId>mssql-jdbc</artifactId>
	<scope>runtime</scope>
</dependency>
```
## application.properties
```java=!
# DataSource 配置
spring.datasource.url=jdbc:sqlserver://127.0.0.1:1433;databaseName=tempdb
spring.datasource.username=sa
spring.datasource.password=!QAZ2wsx
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# SQL Server 的 JPA 配置
spring.jpa.database-platform=org.hibernate.dialect.SQLServerDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.main.lazy-initialization=true

# OpenAPI基本信息配置
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui
```
## mssql_docker
```dockerfile=
version: '3'

services:
  mssql:
    image: mcr.microsoft.com/mssql/server:2019-latest
    container_name: mssql
    environment:
      ACCEPT_EULA: Y
      SA_PASSWORD: "!QAZ2wsx"
    ports:
      - "1433:1433"
    volumes:
      - ./data/mssql:/var/opt/mssql/data
```
## spring-boot 專案
```
├─controller
├─dao
├─entity
└─service
    └─impl
```
1. 在entity層加入Demo class
```java!
package thsrc.example.com.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Entity
@Table(name = "Demo")
public class Demo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column(name="Name")
	private String name;
	@Column(name="Score")
	private int Score;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScore() {
		return Score;
	}
	public void setScore(int Score) {
		this.Score = Score;
	}
	
	public String toString() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(this);
	}
	
}


```
2. 在dao層加DemoDao interface
```java!
package thsrc.example.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thsrc.example.com.entity.Demo;

@Repository
public interface DemoDao extends JpaRepository<Demo, Long>{

}

```
3. 在service層 加入DemoService interface
```java!
package thsrc.example.com.service;

import java.util.List;

import thsrc.example.com.entity.Demo;

public interface DemoService {
	void addTest();
	
	void addTest(Demo s);
	
	Demo getTest(long id);
	
	List<Demo> getAll();
}

```
4. 在service.impl層 加入DemoServiceImpl class
```java!
package thsrc.example.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import thsrc.example.com.dao.DemoDao;
import thsrc.example.com.entity.Demo;
import thsrc.example.com.service.DemoService;

@Service
public class DemoServiceImpl implements DemoService {
	
	@Autowired
	private DemoDao demoDao;
	
	private String[] firstNames = {"Camiol","Joyce","Lucy","Ruth","Louis"};
	
	private String[] lastNames = {"Chuang","Lin","Wang","Chen","Zhang"};
	
	@Override
	public void addTest() {
		Demo s = new Demo();
		String firstName = firstNames[(int) (Math.random()*firstNames.length)];
		String lastName = lastNames[(int) (Math.random()*lastNames.length)];
		int score = (int)(Math.random()*100)+1;
		
		s.setName(firstName+" "+lastName);
		s.setMathScore(score);
		System.out.println(s);
		demoDao.save(s);
	}

	@Override
	public void addTest(Demo s) {
		demoDao.save(s);
	}

	@Override
	public Demo getTest(long id) {
		Demo s = demoDao.findById(id).orElse(new Demo());
		return s;

	}

	@Override
	public List<Demo> getAll() {
		
		return demoDao.findAll();
	}

}


```
5. 在controller層 加入DemoController class
```java!
package thsrc.example.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import thsrc.example.com.entity.Demo;
import thsrc.example.com.service.DemoService;

@RestController
@RequestMapping("/api")
public class DemoController {
	@Autowired
	private DemoService service;
	
	@GetMapping("/addRandom")
	public String addRandomTest() {
		for(int i=0;i<10;i++) {
			service.addTest();
		}
		
		return service.getAll().toString();
	}
	
	@GetMapping("/get")
	public String getTest(@RequestParam("id") long id) {
		return service.getTest(id).toString();
	}
	
	@GetMapping("/add")
	public String addTest(@RequestParam("name") String name,@RequestParam("score") int score) {
		Demo s = new Demo();
		s.setName(name);
		s.setScore(score);
		
		service.addTest(s);
		
		List<Demo> list = service.getAll();
		
		return list.get(list.size()-1).toString();
	}

}
```
6. run

![image](https://github.com/m4y7cl6/springbootDemo-master/assets/17524282/83f368f2-ae23-4969-80ac-af846c24c8de)


7. 至MSSQL查看table已建立

![image](https://github.com/m4y7cl6/springbootDemo-master/assets/17524282/fae6e2e1-18a3-4ede-bebe-379c53ba665d)



---

![image](https://github.com/m4y7cl6/springbootDemo-master/assets/17524282/63f1070b-5ca3-45a6-b219-80008b7b9ef6)


8. 
## swagger-ui
http://localhost:8080/swagger-ui
![image](https://github.com/m4y7cl6/springbootDemo-master/assets/17524282/5018c846-0059-4159-b0ae-08e20252a09f)

