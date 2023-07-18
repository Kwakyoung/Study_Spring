package smart.visual;

import java.util.HashMap;
import java.util.List;

public interface VisualService {
	//영업부: 10, 총무부: 20
	List<HashMap<String, Object>> department(); // 부서별 사원수 조회
	List<HashMap<String, Object>> hirement_year(); // 년도별 채용인원수 조회
	List<HashMap<String, Object>> hirement_month(); // 월별 채용인원수 조회
}
