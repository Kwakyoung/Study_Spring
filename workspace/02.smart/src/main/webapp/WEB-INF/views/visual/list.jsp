<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
#legend span { width:44px; height:17px; margin-right: 5px }
#legend li { display: flex; align-items: center; }
</style>
</head>
<body>
<h3 class="my-4">사원정보분석 시각화</h3>
<ul class="nav nav-tabs">
  <li class="nav-item">
    <a class="nav-link">부서원수</a>
  </li>
  <li class="nav-item">
    <a class="nav-link">채용인원수</a>
  </li>
  <li class="nav-item">
    <a class="nav-link">테스트1</a>
  </li>
  <li class="nav-item">
    <a class="nav-link">테스트2</a>
  </li>
</ul>
<div id='tab-content' class="m-md-2 m-lg-3" style='height:520px'>
	<!-- 부서원수에 대한 막대/도넛 그래프 선택 -->
	<div class="tab text-center mt-4">
		<div class="form-check form-check-inline">
		  <label>
			  <input class="form-check-input" type="radio" name="chart" value="bar" checked>막대그래프
		  </label>
		</div>
		<div class="form-check form-check-inline">
		  <label>
			  <input class="form-check-input" type="radio" name="chart" value="donut">도넛그래프
		  </label>
		</div>
	</div>
	<!-- 채용인원수에 대한 년도별/월별 선택 -->
	<div class="tab text-center mt-4">
		<div class="form-check form-check-inline">
		  <label>
			  <input class="form-check-input" type="checkbox" id="top3" >TOP3부서
		  </label>
		</div>
		
		<div class="form-check form-check-inline">
		  <label>
			  <input class="form-check-input" type="radio" name="unit" value="year" checked>년도별
		  </label>
		</div>
		<div class="form-check form-check-inline">
		  <label>
			  <input class="form-check-input" type="radio" name="unit" value="month">월별
		  </label>
		</div>
	</div>
	<canvas id="chart" class="h-100 m-auto"></canvas>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js/dist/chart.umd.js"></script> <!-- 차트라이브러리 -->
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels"></script> <!-- 데이터라벨 라이브러리 -->
<!-- <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-autocolors"></script> --> 
<!-- 색상자동생성 라이브러리 -->
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-autocolors@0.2.2/dist/chartjs-plugin-autocolors.min.js"></script>
<script>
Chart.defaults.font.size = 16;

Chart.defaults.set('plugins.legend', {
	position: 'bottom',  //범례위치 
})


Chart.register(ChartDataLabels);   // Register the plugin to all charts:
Chart.register( window['chartjs-plugin-autocolors'] ); //All charts autocolors

//데이터라벨 default 적용 지정
Chart.defaults.set('plugins.datalabels', {
	anchor: 'end', //데이터위치
	align: 'start', //앵커기준으로 한 위치
	offset: -20, //얼마나 떨어져있게 할 것인지
	color: '#000', //폰트색상
	font: { weight: 'bold' }, //폰트굵게
})


//그래프형태(막대/도넛) 선택시
$('[name=chart]').change(function(){
	department();
})
//채용인원수조회 단위(년도별/월별) 선택시, TOP3 체크선택/해제시
$('[name=unit], #top3').change( function(){
	hirement_info();
})

function hirement_info(){
	if( $('#top3').prop('checked') ) 	hirement_top3();
	else 								hirement();
}

//부서원수 상위 3위까지의 년도별/월별 채용인원수
function hirement_top3(){
	initCanvas();
	
	var unit = $('[name=unit]:checked').val(); //year/month
	$.ajax({
		url: 'hirement/top3/' + unit,
	}).done(function( response ){
		console.log( response )
		var info = {};
		info.type = unit=='year' ? 'bar' : 'line';   //년도별: 막대, 월별: 선
		info.title = `상위 3위 부서의 \${unit=='year' ? '년도별' : '월별'} 채용인원수`;
		info.category = response.unit;
		info.datas = [], info.label = [];
		$(response.list).each(function(idx, item){
			//item == this
			info.label.push( this.DEPARTMENT_NAME );
			//배열로 이루어진 category 의 값을 키로하여 새로운 배열을 뽑아내기
			var datas = info.category.map( function(category){
				return item[ category ];
			});
			info.datas.push( datas ); //4개의 정보
		})
		console.log( info )
		
		top3Chart( info );
	})
}

function top3Chart( info ){
	$('#tab-content').css('height', 560);
	var datas = [];
	for(var idx=0; idx<info.label.length; idx++){
		var department = {};
		department.label = info.label[idx];
		department.data = info.datas[idx];
		department.backgroundColor = colors[idx];
		department.borderColor = colors[idx];
		datas.push( department );
	}
	
	visual = new Chart( $('#chart'), {
		type: info.type,
		data: {
			labels: info.category,
			datasets:  datas,
		},
		options:{
			layout: { padding:{ top:30 }},
			plugins: {
				datalabels :{
					formatter: function(v){
						return v==0 ? '' : `\${v}명`;
					}
				},
				legend: {
					display: true,
					labels: { font:{size:14} },
				}
			},
			responsive: false,
			maintainAspectRatio : false,
			scales: {
				y: {
					title: { text:info.title, display:true }
				}
			}
		}
	} );
	
}

function initCanvas(){
	$('#legend').remove();
	$('canvas#chart').remove();
	$('#tab-content').append( `<canvas id="chart" class="h-100 m-auto"></canvas>` );
}

$('ul.nav-tabs li').on({
	'click': function(){
		$('ul.nav-tabs li a').removeClass('active');
		$(this).children('a').addClass('active');
		
		var idx = $(this).index();
		$('#tab-content .tab').addClass('d-none');
		$('#tab-content .tab').eq(idx).removeClass('d-none');
		
		if(idx==0) 				department(); 	//부서원수 조회
		else if(idx==1) 		hirement_info();		//채용인원수 조회
		else initCanvas();
	},
	
	'mouseover': function(){
		$(this).addClass('shadow');
	},
	
	'mouseleave': function(){
		$(this).removeClass('shadow');
	},
})


//부서원수 조회
function department(){
	initCanvas();
	
	$.ajax({
		url: 'department',
	}).done(function( response ){
		console.log( response )
		var info = {};
		info.category = [], info.datas = [], info.colors = [];
		$(response).each(function(){
			info.category.push( this.DEPARTMENT_NAME );
			info.datas.push( this.COUNT );
			//부서원수에 따라 색상 적용: 0:10미만(0~9), 1:20미만(10~19), ... 12/10:1.2
			info.colors.push( colors[ Math.floor(this.COUNT/10) ] );
		})
		console.log( info );
		//lineChart( info );
		if( $('[name=chart]:checked').val()=='bar' )
			barChart( info );
		else 
			donutChart( info );
	})
	
	//sampleChart();
} 	

var visual ;
//도선/파이그래프
function donutChart( info ){
	$('#tab-content').css('height', '550');
	
	//각 수치데이터에 대한 백분율 구하기
	var sum = 0;
	$(info.datas).each( function(){
		sum += this;
	})
	//배열정보로 새로운 배열정보를 만들어주는 함수: map
	info.pct = info.datas.map( function( data ){ 
		return Math.round(data / sum * 10000)/100 ;   // 20.54
	}) 
	
	visual = new Chart( $('#chart') , {
		type: 'doughnut',
		data: {
			labels: info.category,
			datasets: [{
		        label: '부서원수',
		        data: info.datas,
		        hoverOffset: 5, //마우수 올렸을때 데이터조각이 offset되는 정도
	      	}]
		},
		options: {
			cutout: '50%', //내부원을 몇% 잘라낼것인지, 0:파이,
			maintainAspectRatio: false, //크기조정시 캔버스 가로세로비율 유지X(기본O)
	    	responsive: true, 	//컨테이너 크기 변경시 캔버스 크기 조정X(기본O)
			plugins:{
				autocolors: { mode: 'data '},
				datalabels: {
					anchor: 'middle', //도넛조각 내부에 데이터위치하게, 
					formatter: function(value, item){
						return `\${value}명(\${ info.pct[item.dataIndex] }%)`;
					}
				}
			}
		}
	});
} 

//선그래프
function lineChart( info ){
	$('#tab-content').css('height', '540');
	visual = new Chart( $('#chart') , {
		type: 'line',
		data: {
			labels: info.category,
			datasets: [{
		        label: '부서원수',
		        data: info.datas,
		        borderColor: '#0000ff',  //그래프선, point테두리에 적용
		        tension: 0, //0:완전꺽은선, 1:곡선
		        pointRadius: 5, //point반지름
		        pointBackgroundColor: '#ff0000', 
	      	}]
		},
	    options: {
	    	maintainAspectRatio: false, //크기조정시 캔버스 가로세로비율 유지X(기본O)
	    	responsive: false, //컨테이너 크기 변경시 캔버스 크기 조정X(기본O)
	    	layout: {
	    		padding: { top: 30  }
	    	},
	    	plugins: {
	    		legend: { display: false }, //기본 범례 안 보이게
	    		datalabels: {
	    			formatter: function( value ){
	    				//return value + '명';
	    				return `\${value}명`;
	    			}
	    		},
	    	},
	    	
	      	scales: {
		        y: {
		          beginAtZero: true,
		          title: { text: '부서별 사원수', display: true }
		        }
		    },
	    }
	});
}

//막대그래프
function barChart( info ){
	$('#tab-content').css('height', '520');
	visual = new Chart( $('#chart') , {
	    type: 'bar',
	    data: {
	      labels: info.category,
	      datasets: [{
	        label: '부서원수',
	        data: info.datas,
	        //borderWidth: 2,
	        barPercentage: 0.5,
	        backgroundColor: info.colors,
	      }]
	    },
	    options: {
	    	maintainAspectRatio: false, //크기조정시 캔버스 가로세로비율 유지X(기본O)
	    	responsive: false, //컨테이너 크기 변경시 캔버스 크기 조정X(기본O)
	    	layout: {
	    		padding: { top: 30, bottom: 20 }
	    	},
	    	plugins: {
	    		legend: { display: false }, //기본 범례 안 보이게
	    		datalabels: {
	    			formatter: function( value ){
	    				//return value + '명';
	    				return `\${value}명`;
	    			}
	    		},
	    		/*  
	    		autocolors: {  //자동색상 적용하는 경우
	    	        mode: 'data'
    	        },
	    		*/
	    	},
	    	
	      	scales: {
		        y: {
		          beginAtZero: true,
		          title: { text: '부서별 사원수', display: true }
		        }
		    },
	    }
	});
	makeLegend();
}

//데이터수치범위에 해당하는 범례 만들기
function makeLegend(){
	var tag = 
	`<ul class="row d-flex justify-content-center m-0 mt-4 p-0 small" id='legend'>`;
	
	for(var no=0; no<=6; no++){
		tag +=
			`<li class="col-auto"><span></span><font>\${no*10}~\${no*10+9}명</font></li>`;
	}
	tag +=
	`<li class="col-auto"><span></span><font>\${no*10}명이상</font></li>
	</ul>`;
	$('#tab-content').after( tag );
	
	$('#legend span').each(function(idx, item){
		$(this).css('background-color', colors[idx] );
	})
}

//데이터수치 범위에 따라 지정할 색상
var colors = [ '#075be3', '#07e324','#e31207',  '#b307e3'
				, '#f0c402', '#eb7b05', '#05e0eb', '#5d05eb', '#752302' , '#d14f9f'];


//브라우저크기 변경시 차트크기 처리
$(window).resize(function(){
	var small = $('#tab-content').width() < 1000 ? true : false;
	
	if( visual.config.type=='doughnut'){
		//if( small ){
			//$('#chart').css( 'width', $('#tab-content').width() );
			$('#chart').css( 'width', '550' );
		//}		
	}else{
		visual.options.responsive = small;  //컨테이너 크기 변경시 캔버스 크기 조정할건지 적용
		$('#chart').css( 'width', small ? $('#tab-content').width() : 1000 );
	}
	
})

function sampleChart(){
	new Chart( $('#chart') , {
	    type: 'bar',
	    data: {
	      labels: ['Red', 'Blue', 'Yellow', 'Green', 'Purple', 'Orange'],
	      datasets: [{
	        label: '# of Votes',
	        data: [12, 19, 3, 5, 2, 3],
	        borderWidth: 1
	      }]
	    },
	    options: {
	    	plugins: {
	            legend: {
	                labels: {
	                    font: {
	                        size: 14
	                    }
	                }
	            }
	        } ,
	        
	      scales: {
	        y: {
	          beginAtZero: true
	        }
	      }
	    }
	  });
	
}

//채용인원수 조회
function hirement(){
	initCanvas();
	var unit = $('[name=unit]:checked').val(); 
	$.ajax({
		url: 'hirement/' + unit,
	}).done(function( response ){
		console.log(response)
		var info = new Object();
		info.datas = new Array(), info.category = [], info.colors = [];
		$(response).each(function(){
			info.datas.push( this.COUNT );
			info.category.push( this.UNIT );
			info.colors.push( colors[ Math.floor(this.COUNT/10) ] ); //데이터수치값 범위에 맞는 색상 지정
		})
		info.title = `\${unit == 'year' ? "년도별" : "월별"} 채용인원수`;
		unitChart( info );
	})
}

//단위별(년도별/월별) 채용인원수 그래프
function unitChart( info ){
	$('#tab-content').css('height', 540);
	visual = new Chart( $('#chart'), {
		type: 'bar',
		data: {
			labels: info.category,
			datasets: [
				{ 
					data: info.datas,
					barPercentage: 0.5,
					backgroundColor: info.colors,
				}
			]
		},
		options:{
			layout: { padding:{top:30, bottom:20} },
			plugins:{
				datalabels: {
					formatter: function(value){
						return `\${value}명`;
					}
				},
				legend: { display: false }, 
			},
			responsive: false,
			maintainAspectRatio: false,
			scales:{
				y: {
					title: { text: info.title, display: true }
				}
			}
		}
	} );
	
	makeLegend();
}


$(function(){
	$('ul.nav-tabs li').eq(1).trigger( 'click' );
})

</script>
</body>
</html>



