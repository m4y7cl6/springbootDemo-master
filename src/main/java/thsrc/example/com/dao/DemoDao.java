package thsrc.example.com.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import thsrc.example.com.entity.Demo;

@Repository
public interface DemoDao extends JpaRepository<Demo, Long>{

}
