package com.hanul.middle;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class TestClass {

	@Autowired @Qualifier("tt") TestVO vo ;
	
	// 할수가 없다. 이유는 스프링 객체가 아니기때문.
}
