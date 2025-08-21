Paging = function(totalCnt, dataSize, pageSize, pageNo){ 
	totalCnt = parseInt(totalCnt);	// 전체레코드수 
	dataSize = parseInt(dataSize); 	// 페이지당 보여줄 데이타수 
	pageSize = parseInt(pageSize); 	// 페이지 그룹 범위 1 2 3 5 6 7 8 9 10 
	pageNo = parseInt(pageNo); 		// 현재페이지 
	
	var html = new Array(); 
	if(totalCnt == 0){
		return ""; 
	} 
	
	// 페이지 카운트 
	var pageCnt = totalCnt % dataSize; 
	
	if(pageCnt == 0){ 
		pageCnt = parseInt(totalCnt / dataSize); 
	}
	else{ 
		pageCnt = parseInt(totalCnt / dataSize) + 1; 
	} 
	
	var pRCnt = parseInt(pageNo / pageSize); 
	
	if(pageNo % pageSize == 0){ 
		pRCnt = parseInt(pageNo / pageSize) - 1; 
	} 
	
	//이전 화살표 
	/*alert('pageNo : ' + pageNo + '/ pageSize : ' + pageSize + '/ pRCnt : ' + pRCnt  + '/ pageCnt : ' + pageCnt);
	html.push('<ul>');

	if(pageNo > 1) {
		html.push('<li><a href="javascript:fn_callPage(\'' + 1 + '\')">FIRST</a></li>');
		html.push('<li><a href="javascript:fn_callPage(\'' + (pageNo-1) + '\')" class="prev">PREV</a></li>');
	} */

	html.push('<ul>');
	if(pageNo > 1) {
		html.push('<li><a href="javascript:fn_callPage(\'' + 1 + '\')">FIRST</a></li>');
	}
	if(pageNo > pageSize){ 
		var s2; 
		
		if(pageNo % pageSize == 0){ 
			s2 = pageNo - pageSize; 
		}
		else{ 
			s2 = pageNo - pageNo % pageSize; 
		} 
		html.push('<li><a href="javascript:fn_callPage(\'' + s2 + '\')" class="prev">PREV</a></li>');

	}
	else{
		//html.push('<li><a href="#" class="prev">PREV</a></li>');
	}
	
	//paging Bar 
	for(var index=pRCnt * pageSize + 1; index<(pRCnt + 1) * pageSize + 1; index++){ 
		if(index == pageNo){ 
			// 화면에 표시된 페이지
			html.push('<li><a href="#" onclick="return false;" class="num active">'); 
			html.push(index); 
			html.push('</a></li>');
		}
		else{ 
			// 화면에 표시되지 않은 페이지
			html.push('<li><a href="javascript:fn_callPage(\'' + index + '\')" class="num">');
			html.push(index);
			html.push('</a></li>'); 
		} 
		
		if(index == pageCnt){ 
			break; 
		}
		/*else{
			html.push('|'); 
		}*/
	} 
	
	//다음 화살표
	/*html.push('<li><a href="javascript:fn_callPage(\'' +  (pRCnt + 1) * pageSize + 1 + '\')" class="next">NEXT</a></li>');
	html.push('<li><a href="javascript:fn_callPage(\'' +  pageCnt + '\')">LAST</a></li>');
	html.push('</ul>');*/

	if(pageCnt > (pRCnt + 1) * pageSize){
		html.push('<li><a href="javascript:fn_callPage(\'' +  (pRCnt + 1) * (pageSize + 1) + '\')" class="next">NEXT</a></li>');
	}
	else{ 
		//html.push('<li><a href="#" class="next">NEXT</a></li>');
	}
	
	if(pageNo < pageCnt) {
		html.push('<li><a href="javascript:fn_callPage(\'' +  pageCnt + '\')">LAST</a></li>');
	}
	html.push('</ul>');
	
	return html.join(""); 
}

// 페이징 UI set
function gfn_setPageInfo(totalRecordCount, recordCountPerPage, pageSize, currentPageNo){
	// 페이지 정보 data.pageInfo
	// 페이징 처리 [전체 레코드수, 페이지당 보여줄 데이터수, 페이지 그룹 범위, 현재 페이지]
	// var pageHtml = Paging(101, 10, 10, 2);
	var pageHtml = Paging(totalRecordCount, recordCountPerPage, pageSize, currentPageNo);
	$('.pagingWrap').empty().html(pageHtml);
	$('.pagingWrap').show();
}
