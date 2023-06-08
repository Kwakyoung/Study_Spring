package smart.hr;

import java.util.List;

public interface HrService {
	// CRUD
	int employee_insert(EmployeeVO vo);			// 신규사원 등록
	List<EmployeeVO> employee_list();			// 사원목록 조회
	EmployeeVO employee_info(int employee_id);	// 사원정보 조회
	int employee_update(EmployeeVO vo);			// 사원정보 수정
	int employee_delete(int employee_id);		// 사원정보 삭제
}
