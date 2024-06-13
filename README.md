# Spring boot jpa & swagger-ui
![image](https://github.com/m4y7cl6/springbootDemo-master/assets/17524282/32fa0b92-d104-4ac1-83fa-b114452ece8e)



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
├─java
│  └─thsrc
│      └─example
│          └─com
│              ├─controller
│              ├─dao
│              ├─entity
│              └─service
│                  └─impl
└─resources
    └─db
        └─changelog
```
## Pom.xml
```=java!
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>2.4.3</version>
		<relativePath /> <!-- lookup parent from repository -->
	</parent>
	<groupId>thscr.example</groupId>
	<artifactId>springbootDemo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>springbootDemo</name>
	<description>Spring Boot Spring Data JPA</description>
	<properties>
		<java.version>1.8</java.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>com.microsoft.sqlserver</groupId>
			<artifactId>mssql-jdbc</artifactId>
			<scope>runtime</scope>
			<version>9.4.0.jre8</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<!-- https://mvnrepository.com/artifact/com.google.code.gson/gson -->
		<dependency>
			<groupId>com.google.code.gson</groupId>
			<artifactId>gson</artifactId>
		</dependency>
		<!-- SpringDoc OpenAPI: export OpenAPI specification/document -->
		<dependency>
			<groupId>org.springdoc</groupId>
			<!-- with UI -->
			<artifactId>springdoc-openapi-ui</artifactId>
			<version>1.5.8</version>
		</dependency>
		<dependency>
			<groupId>org.liquibase</groupId>
			<artifactId>liquibase-core</artifactId>
			<version>3.10.3</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<scope>runtime</scope>
			<optional>true</optional>
		</dependency>
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
				<plugin>
					<groupId>org.liquibase</groupId>
					<artifactId>liquibase-maven-plugin</artifactId>
					<version>4.9.1</version>
					<configuration>
						<propertyFile>src/main/resources/liquibase.properties</propertyFile
				<changeLogFile>src/main/resources/db/changelog/outputChangeLog.xml</changeLogFile>
					</configuration>
				</plugin>
		</plugins>
	</build>
</project>

```
## application.properties
```=java!
# Liquibase DataSource
# 禁用Liquibase
spring.liquibase.enabled=false

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
## liquibase.properties
```=java!
outputChangeLogFile=src/main/resources/db/changelog/outputChangeLog.xml
url=jdbc:sqlserver://127.0.0.1:1433;databaseName=tempdb
username=sa
password=!QAZ2wsx
driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

1. 在entity層加入Demo class
# 註解Table相關，先不進行Table 創建
```=java!
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
```=java!
package thsrc.example.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import thsrc.example.com.entity.Demo;

@Repository
public interface DemoDao extends JpaRepository<Demo, Long>{

}

```
3. 在service層 加入DemoService interface
```=java!
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
```=java!
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
```=java!
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

6. mvn build
```
mvn clean install
```
```
mvn liquibase:generateChangeLog
```
#Bulid success 產生「outputChangeLog.xml」於db.changLog 中
```=outputChangeLog.xml
<?xml version="1.1" encoding="UTF-8" standalone="no"?>
<databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog" xmlns:ext="http://www.liquibase.org/xml/ns/dbchangelog-ext" xmlns:pro="http://www.liquibase.org/xml/ns/pro" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog-ext http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-ext.xsd http://www.liquibase.org/xml/ns/pro http://www.liquibase.org/xml/ns/pro/liquibase-pro-4.6.xsd http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-4.6.xsd">
    <changeSet author="m4y7c (generated)" id="1718281892458-1">
        <createTable tableName="hibernate_sequence">
            <column computed="false" name="next_val" type="numeric(19, 0)"/>
        </createTable>
    </changeSet>
    <changeSet author="m4y7c (generated)" id="1718281892458-2">
        <createTable tableName="demo">
            <column computed="false" name="id" type="numeric(19, 0)">
                <constraints nullable="false" primaryKey="true" primaryKeyName="PK__demo__3213E83F95629075"/>
            </column>
            <column computed="false" name="math_score" type="int">
                <constraints nullable="false"/>
            </column>
            <column computed="false" name="name" type="varchar(255)"/>
        </createTable>
    </changeSet>
</databaseChangeLog>
```
```
mvn liquibase:updateSQL
```
#Bulid success 產生「migrate.sql」於traget.liquibase 中
```=sql!
-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: src/main/resources/db/changelog/outputChangeLog.xml
-- Ran at: 2024/6/13 下午 8:33
-- Against: sa@jdbc:sqlserver://127.0.0.1:1433;connectRetryInterval=10;connectRetryCount=1;maxResultBuffer=-1;sendTemporalDataTypesAsStringForBulkCopy=true;delayLoadingLobs=true;useFmtOnly=false;useBulkCopyForBatchInsert=false;cancelQueryTimeout=-1;sslProtocol=TLS;jaasConfigurationName=SQLJDBCDriver;statementPoolingCacheSize=0;serverPreparedStatementDiscardThreshold=10;enablePrepareOnFirstPreparedStatementCall=false;fips=false;socketTimeout=0;authentication=NotSpecified;authenticationScheme=nativeAuthentication;xopenStates=false;sendTimeAsDatetime=true;replication=false;trustStoreType=JKS;trustServerCertificate=false;TransparentNetworkIPResolution=true;serverNameAsACE=false;sendStringParametersAsUnicode=true;selectMethod=direct;responseBuffering=adaptive;queryTimeout=-1;packetSize=8000;multiSubnetFailover=false;loginTimeout=15;lockTimeout=-1;lastUpdateCount=true;encrypt=false;disableStatementPooling=true;databaseName=tempdb;columnEncryptionSetting=Disabled;applicationName=Microsoft JDBC Driver for SQL Server;applicationIntent=readwrite;
-- Liquibase version: 4.9.1
-- *********************************************************************

USE tempdb;
GO

-- Create Database Lock Table
CREATE TABLE DATABASECHANGELOGLOCK (ID int NOT NULL, LOCKED bit NOT NULL, LOCKGRANTED datetime2(3), LOCKEDBY nvarchar(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID))
GO

-- Initialize Database Lock Table
DELETE FROM DATABASECHANGELOGLOCK
GO

INSERT INTO DATABASECHANGELOGLOCK (ID, LOCKED) VALUES (1, 0)
GO

-- Lock Database
UPDATE DATABASECHANGELOGLOCK SET LOCKED = 1, LOCKEDBY = 'USER (192.168.56.1)', LOCKGRANTED = GETDATE() WHERE ID = 1 AND LOCKED = 0
GO

-- Create Database Change Log Table
CREATE TABLE DATABASECHANGELOG (ID nvarchar(255) NOT NULL, AUTHOR nvarchar(255) NOT NULL, FILENAME nvarchar(255) NOT NULL, DATEEXECUTED datetime2(3) NOT NULL, ORDEREXECUTED int NOT NULL, EXECTYPE nvarchar(10) NOT NULL, MD5SUM nvarchar(35), DESCRIPTION nvarchar(255), COMMENTS nvarchar(255), TAG nvarchar(255), LIQUIBASE nvarchar(20), CONTEXTS nvarchar(255), LABELS nvarchar(255), DEPLOYMENT_ID nvarchar(10))
GO

-- Changeset src/main/resources/db/changelog/outputChangeLog.xml::1718281892458-1::m4y7c (generated)
CREATE TABLE hibernate_sequence (next_val numeric(19, 0))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('1718281892458-1', 'm4y7c (generated)', 'src/main/resources/db/changelog/outputChangeLog.xml', GETDATE(), 1, '8:0d6c6e543d68fcf70405b4839057b43e', 'createTable tableName=hibernate_sequence', '', 'EXECUTED', NULL, NULL, '4.9.1', '8282003712')
GO

-- Changeset src/main/resources/db/changelog/outputChangeLog.xml::1718281892458-2::m4y7c (generated)
CREATE TABLE demo (id numeric(19, 0) NOT NULL, math_score int NOT NULL, name varchar(255), CONSTRAINT PK__demo__3213E83F95629075 PRIMARY KEY (id))
GO

INSERT INTO DATABASECHANGELOG (ID, AUTHOR, FILENAME, DATEEXECUTED, ORDEREXECUTED, MD5SUM, DESCRIPTION, COMMENTS, EXECTYPE, CONTEXTS, LABELS, LIQUIBASE, DEPLOYMENT_ID) VALUES ('1718281892458-2', 'm4y7c (generated)', 'src/main/resources/db/changelog/outputChangeLog.xml', GETDATE(), 2, '8:42cd3fde2f63ecca20e1992687e6ecf8', 'createTable tableName=demo', '', 'EXECUTED', NULL, NULL, '4.9.1', '8282003712')
GO

-- Release Database Lock
UPDATE DATABASECHANGELOGLOCK SET LOCKED = 0, LOCKEDBY = NULL, LOCKGRANTED = NULL WHERE ID = 1
GO
```
7. run

![image](https://github.com/m4y7cl6/springbootDemo-master/assets/17524282/83f368f2-ae23-4969-80ac-af846c24c8de)


8. 至MSSQL查看table已建立

![image](https://github.com/m4y7cl6/springbootDemo-master/assets/17524282/fae6e2e1-18a3-4ede-bebe-379c53ba665d)



---

![image](https://github.com/m4y7cl6/springbootDemo-master/assets/17524282/63f1070b-5ca3-45a6-b219-80008b7b9ef6)


9. 
## swagger-ui
http://localhost:8080/swagger-ui
![image](https://github.com/m4y7cl6/springbootDemo-master/assets/17524282/5018c846-0059-4159-b0ae-08e20252a09f)

