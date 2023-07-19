package smart.visual;

import java.util.HashMap;
import java.util.List;

public interface VisualService {
	//영업부: 10, 총무부: 20
	List<HashMap<String, Object>> department(); // 부서별 사원수 조회
	List<HashMap<String, Object>> hirement_year(); // 년도별 채용인원수 조회
	List<HashMap<String, Object>> hirement_month(); // 월별 채용인원수 조회
	List<HashMap<String, Object>> hirement_top3_year(); //부서원수 상위3위의 년도별 채용인원수 조회
	List<HashMap<String, Object>> hirement_top3_month(); // 부서원수 상위3위의 월별 채용인원수 조회
}
