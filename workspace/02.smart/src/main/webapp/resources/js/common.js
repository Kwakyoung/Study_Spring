/**
 * 공통 함수 선언
 */


function modalAlert( type, title, message ){
	$('#modal-alert .modal-title').html( title ); 	//모달창 제목
	$('#modal-alert .modal-body').html( message ); 	//모달창 내용

	//모달창 type에 따라 아이콘모양,색 지정을 위해 모든 클래스 삭제
  	$('.modal-icon').removeClass(
	'text-info text-warning text-danger text-primary text-success fa-circle-question fa-circle-exclamation')
	// 아니오/확인 으로 사용되는 버튼의 색상 초기화
	$('.modal-footer .btn-ok').removeClass(
		'btn-info btn-warning btn-danger btn-primary btn-success');
	  	
  	if( type=='danger' ){//confirm에 해당
  		//아니오, 예 버튼 
  		$('.modal-footer .btn-ok').addClass('btn-secondary').text('아니오')
		$('.modal-footer .btn-danger').removeClass('d-none')
		
  		$('.modal-icon').addClass('fa-circle-question') //아이콘 물음표
		
	}else{
		//확인 버튼, confirm일때만 사용할 btn-danger 는 필요없음
		$('.modal-footer .btn-ok').addClass('btn-' + type).text('확인')
		$('.modal-footer .btn-danger').addClass('d-none')
		
		$('.modal-icon').addClass('fa-circle-exclamation')  //아이콘 느낌표
	}
	$('.modal-icon').addClass('text-'+ type);
}


// 동적으로 만들어진 요소에 대해서는 document에 이벤트를 등록해야 한다
$(document).on('click','.date + .date-delete', function(){
	$(this).css('display', 'none'); // 삭제버튼 안보이게 처리
	$(this).prev('.date').val(''); // 날짜태그의 값을 초기화
})


// 파일이 이미지파일인지 확인
function isImage( filename ){
	// abc.png, abc.jpg, abc.txt, abc.hwp
	var ext = filename.substr( filename.lastIndexOf('.')+1 ).toLowerCase();
	var imgs = [ "png", "jpg", "bmp", "gif", "jpeg", "webp" ];
	return imgs.indexOf( ext ) == -1 ? false : true ;
}



$(function(){
	
	// 프로필 이미지 선택처리
	$('input#file-single').change(function(){
		console.log( $(this));
		console.log(this.files);
		
		var _preview = $('#file-attach .file-preview');
		var _delete = $('#file-attach .file-delete');
		
		var attached = this.files[0];
		console.log( attached )
		if( attached ){
			// 이미지 파일인지 확인
			if ( isImage ( attached.name)) {
				_delete.removeClass('d-none');  //이미지 선택시 삭제버튼 안보이게 막아둔걸 없앰
				// 미리보기 태그가 있을때만
				if( _preview.length > 0 ){
					_preview.html( "<img>" );
					
					var reader = new FileReader();
					reader.readAsDataURL( attached );
					reader.onload = function(e){
						_preview.children("img").attr("src", this.result);
					} 
				}
			}else {
				// 이전 선택했떤 이미지파일처리
				_preview.empty();
				$(this).val(''); // 실제 file 태그의 정보 초기화
				_delete.addClass('d-none');
			}
		}
	})
	
	
	
	
	$('.date').change(function(){
		$(this).next('.date-delete').css('display','inline')
	})
	
	$('[name=phone]').keyup(function(){
	toPhone( $(this) );  
})
	
	var today = new Date();
	var range = today.getFullYear()-100 + ':' + today.getFullYear();
	$.datepicker.setDefaults({
		dateFormat: "yy-mm-dd",
		changeYear: true,
		changeMonth: true,
		yearRange: range,
		showMonthAfterYear: true,
		monthNamesShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월"
					, "9월", "10월", "11월", "12월"],
		dayNamesMin: [ "일", "월" , "화", "수", "목", "금", "토"],
		maxDate: today, 
	})
	
	$( ".date" ).datepicker();
	$( ".date" ).attr('readonly', true)
}) 


function toPhone( tag ){
// 02-1234-5678(10자리)   010-1234-5678(11자리)
	var phone = tag.val().replace(/[^0-9]/g,'').replace(/[-]/g,''); // 0~9 숫자만 입력되게 처리  --> 순수 전화번호 숫자만01012345678
					  // 0~9숫자아닌거 /g (전체를) '' 로 변환	
	if( phone.length >1 ){  // 2자리 이상 입력하면 : 02, 062, 010
		var start = phone.substr(0, 2) == "02" ? 2 : 3,
			middle = start + 4; // 두번째 항목 : 1234 - 무조건 4자리
			
			// - 만들어서 넣기
			if ( phone.length > middle ){
				 phone = phone.substr(0, start) + "-" + phone.substr(start, 4) + "-" + phone.substr(middle,4);
			}else if( phone.length > start  ){
				 phone = phone.substr(0, start) + "-" + phone.substr(start, 4) + "-";
			}
	}		
	//  substr(시작, 길이), substring(시작, 끝위치)
	
	tag.val( phone );
}
