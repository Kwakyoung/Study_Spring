package com.hanul.middle;

import org.springframework.stereotype.Component;

// null x => 메모리에 올리는 방법. (==> Spring annotation )

@Component(value= "bean")  // Bean으로 만들거야 라는 뜻
public class TestBean {
	public TestBean() {
		System.out.println("@Autowried가 되었다. 나는 null 아니다.");
	}
}
