package customer;

import java.util.List;

public interface CustomerService {
	
	// DAO (Data Access Object)
	// DTO (Data Transfer Object), VO (Value Object)
	
	// CRUD(Creat, Read, Update, Delete)
	void customer_insert(CustomerVO vo);	// 신규 고객정보 저장
	List<CustomerVO> customer_list();		// 고객 목록 조회
	List<CustomerVO> customer_list(String name);		// 이름검색 고객 목록 조회
	CustomerVO customer_info(int id);		// 고객 상세정보 조회
	void customer_update(CustomerVO vo);	// 고객정보 수정 저장
	void customer_delete(int id);			// 고객정보 삭제
	
	
	
	
	
	
	
}
