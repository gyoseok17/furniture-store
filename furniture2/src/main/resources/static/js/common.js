window.onload = function(){

}

// Ajax Call
// 2022-10-28 isAsync 추가(파라미터가 없거나 isAsync true이면 비동기[기본값] , 파라미터값이 있고 값이 flase이면 동기)
function gfn_callAjax(type, url, param, callback, isAsync) {
	$('#loading').show();
	console.log('###gfn_callAjax param : ' + param);
	if(gfn_isEmpty(isAsync)) {
		isAsync = true;	// 기본값 비동기
	}
	let result;
	$.ajax({
		type : type
	  ,	url : url
	  ,	data : param
	  , dataType : 'json'
	  , contentType : 'application/json;charset=UTF-8'
	  , timeout : 30000
	  , async : isAsync
	  , beforeSend : function(xhr){
			xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
            xhr.setRequestHeader('_csrf', csrf_token);
        }
	  ,	success : function (res){
			// 로딩바 없애기	
			$('#loading').hide();
			if(isAsync == true) {
				// 비동기로 호출시 콜백 리턴
				callback(res);
			}else {				
				// 동기로 결과 호출시
				result = res;
			}
			
		}
	  ,	error : function(req, stat) {
			console.log('###gfn_callAjax error req : ' + JSON.stringify(req));
			console.log('###gfn_callAjax error stat : ' + stat);
			$('#loading').hide();
			if(req.status == '999'){
				gfn_modalAlert('error','오류','인증되지 않은 사용자 또는 세션이 만료 되었습니다.\n[로그인] 후 사용하시기 바랍니다.');
				// location.href="/login.do";
				
				location.href='/login/login.do';
			}
			else{
				alert(errorMsg1);
			}
			
			// 로딩바 없애기	
		}
	});
	if(isAsync == false) {
		return result;
	}
}

// Ajax Call(첨부파일 포함)
function gfn_callAjaxFile(url, formData, callback) {
	console.log('###gfn_callAjaxFile url : ' + url);
	console.log('###gfn_callAjaxFile formData : ' + formData);
	$('#loading').show();
	$.ajax({
		  url : url
		, type : 'POST'
	  	, data : formData
		, enctype: 'multipart/form-data'	
	  	, timeout : 30000
	  	, processData : false // true : get방식, false: post 방식
	  	, contentType : false // true : application/x-www-form-urlencoded, false : multipart/form-data
	    , cache : false
		, success : function (res) {
			$('#loading').hide();
	  		callback(res);
	  	}
        , error : function (e){
			$('#loading').hide();
	  		console.log('###gfn_callAjaxFile error : ' + JSON.stringify(e));
			alert(errorMsg1);
		}
	});
}

// page call
function gfn_callPage(type, url, param, tempCsrf) {
	csrfToken = csrf_token;
	if(/*gfn_isEmpty(csrf_token) &&*/ !gfn_isEmpty(tempCsrf)) {
		csrfToken = tempCsrf;
	}
	// get 방식 페이지 호출
	if(type.toUpperCase() == 'GET') {
		/*if(param.indexOf('=') > 0){
			param += '&_csrf=' + csrf_token;
		}
		else{
			param = '_csrf=' + csrf_token;
		}*/
		if(Object.keys(param).length > 0){
			location.href = url + '?' + param + '&_csrf=' + csrfToken;
		}else {
			location.href = url + '?' + '_csrf=' + csrfToken;
		}
	
	}
	// post 방식 페이지 호출
	else if(type.toUpperCase() == 'POST') {
		// post 전송위한 form 생성
		let tempForm = $('<form></form>');
		tempForm.attr('name', 'tempForm');
		tempForm.attr('method', type);
		tempForm.attr('action', url);
		// tempForm.attr('target', '_blank');
		
		// CSRF 정보 추가
		tempForm.append($('<input/>', {type : 'hidden', name : '_csrf', value : csrf_token}));
		
		// 파라메터 정보가 있을경우만 파라메터 추가
		if(Object.keys(param).length > 0){
			let key = Object.keys(param);
		
			// 파라메터 hidden input 생성
			for(let i = 0; i < key.length; i++){
				tempForm.append($('<input/>', {type : 'hidden', name : key[i], value : param[key[i]]}));
			}
		}
		tempForm.appendTo('body');
		
		tempForm.submit();
		
		tempForm.remove();
	}
}


// 특수문자 변환
function txtConvert(txt){
	if (txt != null) {
		txt = txt.replace(/;ampersand;/gi, "&");
		txt = txt.replace(/&amp;/gi, "&");
		txt = txt.replace(/;percent;/gi, "%");
		txt = txt.replace(/&#41;/gi, "\)");
		txt = txt.replace(/&#40;/gi, "\(");
		txt = txt.replace(/&lt;/gi, "<");
		txt = txt.replace(/&gt;/gi, ">");
		txt = txt.replace(/&#39;/gi, "'");
		txt = txt.replace(/&nbsp;/gi, " ");
		txt = txt.replace(/&quot;/gi, "\"");
	}
	return txt;
}

/* ie10, ie11 textarea error */
function textareaError($area) {
	let verNum = getVersionIE();
	
	if( verNum == 10 || verNum == 11 ) {
		let $textarea = $area.find("textarea[placeholder]");
		let errorIndex = [];
		
		$textarea.each(function(index) { 
			if ( $(this).attr("placeholder") == $(this).val() ) {
				errorIndex.push( $(this) );
			} 
		});
		
		if(errorIndex.length > 0) {
			for(var i=0; i <= errorIndex.length - 1; i++) {
				errorIndex[i].val("");
			}
		}
	}
}

/**
 * 문자열이 빈 문자열인지 체크하여 결과값을 리턴한다. 
 * @param str		: 체크할 문자열
 */
function gfn_isEmpty(value) {
    if (value === undefined || value === null) {
        // undefined 또는 null인 경우 비어있는 것으로 간주
        return true;
    }
    if (typeof value === 'string') {
      // 문자열인 경우 trim()을 사용하여 양 끝의 공백 제거 후 확인
        return !value.trim();
    }
    if (typeof value === 'function' || typeof value === 'object') {
      // 함수 또는 객체인 경우 비어있지 않은 것으로 간주
        return false;
    }

    // 그 외의 경우는 비어있는 것으로 간주
    return true;
}



// val1 undefined 이거나 null이면 val2 문자로 변환
function gfn_nullToValue(val1, val2){
	if(typeof val1 == 'undefined' || val1 == 'undefined'  || val1 == undefined  || val1 == null){
		return val2;
	}
	
	return val1;
}

// 전화번호 변환
function gfn_autoHypenTel(str) {
  str = str.replace(/[^0-9]/g, '');
  let tmp = '';

  if (str.substring(0, 2) == '02') {
    // 서울 전화번호일 경우 10자리까지만 나타나고 그 이상의 자리수는 자동삭제
    if (str.length < 3) {
      return str;
    } else if (str.length < 6) {
      tmp += str.substr(0, 2);
      tmp += '-';
      tmp += str.substr(2);
      return tmp;
    } else if (str.length < 10) {
      tmp += str.substr(0, 2);
      tmp += '-';
      tmp += str.substr(2, 3);
      tmp += '-';
      tmp += str.substr(5);
      return tmp;
    } else {
      tmp += str.substr(0, 2);
      tmp += '-';
      tmp += str.substr(2, 4);
      tmp += '-';
      tmp += str.substr(6, 4);
      return tmp;
    }
  } else {
    // 핸드폰 및 다른 지역 전화번호 일 경우
    if (str.length < 4) {
      return str;
    } else if (str.length < 7) {
      tmp += str.substr(0, 3);
      tmp += '-';
      tmp += str.substr(3);
      return tmp;
    } else if (str.length < 11) {
      tmp += str.substr(0, 3);
      tmp += '-';
      tmp += str.substr(3, 3);
      tmp += '-';
      tmp += str.substr(6);
      return tmp;
    } else {
      tmp += str.substr(0, 3);
      tmp += '-';
      tmp += str.substr(3, 4);
      tmp += '-';
      tmp += str.substr(7);
      return tmp;
    }
  }

  return str;
}

// 날짜 포맷
function gfn_YMDFormatter(num, format){
 	if(!num) return '';
 	if(!format) {
		format = '.';
	}
 	let formatNum = '';

 	// 공백제거
 	num=num.replace(/\s/gi, '');

 	try{
		if(num.length == 8) {
       		formatNum = num.replace(/(\d{4})(\d{2})(\d{2})/, '$1' + format + '$2' + format + '$3');
      	}
 	} catch(e) {
      	formatNum = num;
 	}
 	
	return formatNum;
}

function getTodayYyyymmdd(){
    let date = new Date();
    let year = date.getFullYear();
    let month = ("0" + (1 + date.getMonth())).slice(-2);
    let day = ("0" + date.getDate()).slice(-2);

    return year + month + day;
}

// 3자리마다 콤마 추가
function gfn_numberComma(str){
	str += '';		// 문자열이 아닐수도 있기 때문에 ''을 붙여서 문자열로 변경
	return str.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

// 쿠키 set
function gfn_setCookie(name, value, expiredays) {
    let todayDate = new Date();
	if(expiredays > 0) {
		todayDate.setDate(todayDate.getDate() + expiredays);
	}else {
		todayDate.setDate(todayDate.getDate() - 1);
	}
    document.cookie = name + "=" + escape(value) + "; path=/; expires=" + todayDate.toGMTString() + ";";
}

// 쿠키 get
function gfn_getCookie(name) {
	const cookieName = name + "=";
	const cookies = document.cookie.split(';');

	for (let i = 0; i < cookies.length; i++) {
		let cookie = cookies[i].trim();
		if (cookie.indexOf(cookieName) === 0) {
			return decodeURIComponent(cookie.substring(cookieName.length, cookie.length));
		}
	}

	return "";
}

/* 쿠키 delete */
function deleteCookie(cookieName) {
  	document.cookie = cookieName + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
}
/**
 * yyyy mm dd hh mm을 쿠키에 저장하는 함수
 */
function gfn_setSavingTime(){
	// 현재 날짜와 시간을 포함하는 Date 객체 생성
	const currentDate = new Date();

	// 년, 월, 일, 시, 분, 초를 추출
	const year = currentDate.getFullYear();
	const month = currentDate.getMonth(); // 월은 0부터 시작하므로 1을 더해줌
	const day = currentDate.getDate();
	const hours = currentDate.getHours();
	const minutes = currentDate.getMinutes();
	gfn_setCookie('saveTime', year + ' ' + month + ' ' + day + ' ' + hours + ' ' + minutes, 1);


}

/**
 * 쿠키에 저장된 시간과 현재시간이 20분을 초과하면 OUT
 */
function gfn_comparisonNowtime(){
	if(gfn_isEmpty(gfn_getCookie('saveTime'))){
		return false;
	}

	const saveTime = gfn_getCookie('saveTime').split(' ');
	const givenDate = new Date(saveTime[0], saveTime[1], saveTime[2], saveTime[3], saveTime[4]);
	const nowDate = new Date();
	const timeDifference = nowDate - givenDate;
	// 시간관련수정
	const twentyMinutesInMilliseconds = limit_minute * minute_1;
	return timeDifference >= twentyMinutesInMilliseconds;

}

/**********************************************************************************************
 *  validation
 *******************************************************************************************/

/**
 * @note 문자열과 검사할 항목을 입력받아 옵션 항목이외의 값이 존재하는지 판단한다.
 * @param str - 검사할 문자열
 * @param option  ( type : json )
 * {
			ckor: 'Y'					// 완성형 한글 					/가-힣/
			kor : 'Y',					//한글						/ㄱ-ㅎㅏ-ㅣ가-힣/
			eng : 'Y ,					//영문						/a-zA-z/
			digit : 'Y', 			    //0~9숫자						/0-9/
			underScore : 'Y',			//underScore(_)		/_/
			hyphen : 'Y'				//hyphen(-)				/-/
			dot : 'Y', 					//dot(.)					/./
			comma : 'Y', 				//comma(,)  			/,/
			whiteSpace : 'Y' 			//whiteSpace(,)  			/,/
			colon : 'Y' 				//colon(:)						/:/
			space : 'Y'					// space( )
			정규 표현식 ^이 [ . . . ]안에 들어가있으면 ! 와 같은 기능을 합니다.
	}
 * @returns boolean
 * 		true : 옵션항목 중  값이 'Y' 항목 이외의 값이 존재할 때,
 *     false : 옵션항목 중  값이 'Y' 항목의  값만 존재할 때
 * @author PDW
 */
function gfn_isOnlyValueOption(str, option){

	this.exp_ckor = '가-힣';
	this.exp_kor = 'ㄱ-ㅎㅏ-ㅣ가-힣';
	this.exp_eng = 'a-zA-Z';
	this.exp_digit = '0-9';
	this.exp_underScore = '_';
	this.exp_hyphen = '\\-';
	this.exp_dot = '\\.';
	this.exp_comma = ',';
	this.exp_whiteSpace = "\\s";
	this.exp_colon = "\\:";
	this.exp_space = '\\s+';

	let regExpStr = '';

	let tempOption =  JSON.stringify(option);

	if(!gfn_isJson(tempOption)){//json만 허용
		return;
	}

	if( !gfn_isBlank(option.ckor) && option.ckor == 'Y')  regExpStr += this.exp_ckor;
	if( !gfn_isBlank(option.kor) && option.kor == 'Y')  regExpStr += this.exp_kor;
	if( !gfn_isBlank(option.eng) && option.eng == 'Y')  regExpStr += this.exp_eng;
	if( !gfn_isBlank(option.digit) && option.digit == 'Y')  regExpStr += this.exp_digit;
	if( !gfn_isBlank(option.underScore) && option.underScore == 'Y')  regExpStr += this.exp_underScore;
	if( !gfn_isBlank(option.hyphen) && option.hyphen == 'Y')  regExpStr += this.exp_hyphen;
	if( !gfn_isBlank(option.dot) && option.dot == 'Y')  regExpStr += this.exp_dot;
	if( !gfn_isBlank(option.comma) && option.comma == 'Y')  regExpStr += this.exp_comma;
	if( !gfn_isBlank(option.whiteSpace) && option.whiteSpace == 'Y')  regExpStr += this.exp_whiteSpace;
	if( !gfn_isBlank(option.colon) && option.colon == 'Y')  regExpStr += this.exp_colon;
	if( !gfn_isBlank(option.space) && option.space == 'Y')  regExpStr += this.exp_space;
	let regExp = new RegExp('\[^' + regExpStr + '\]', 'g');
	return regExp.test(str);
}


/**
 * @note 입력받은 객체를 json형태의 값인지 판단한다.
 * @param obj
 * @returns boolean
 * @author PDW
 */
function gfn_isJson(obj){
	try {
	  newObj = JSON.parse(obj);
	} catch (e) {
	  return false;
	}
	return true;
}


/**
 * @note 입력받은 문자열의 byte크기를 리턴한다. 한글 2바이트
 * @param s : 문자열
 * @returns int
 * @author PDW
 */
function gfn_getByteLength(s,b,i,c){
	for(b=i=0;c=s.charCodeAt(i++);b+=(c==10)?2:((c>>7)?2:1));
	return b;
};

/**
 * @note 입력받은 값이 blank(null, 빈문자열, undefined, size 0) 인지 판단한다
 * @param obj
 * @returns boolean
 * @author PDW
 */
function gfn_isBlank(obj){
	if(obj == null || obj == '' || obj == 'undefined' || obj.length == 0){
		return true;
	}else{
		return false;
	}
}

/**
 * @note 입력받은 값이 boolean type인지 판단한다.
 * @param obj
 * @returns
 * @author PDW
 */
function gfn_isBoolean(obj){
	return (typeof obj != 'boolean');
}

/**
 * @note 값을 입력받아 맨앞과 맨뒤에 공백이 제거된 값을 리턴 한다. 값 중간에 있는 공백은 제거하지 않는다.
 * @param obj
 * @returns String
 * @author PDW
 */
function gfn_whiteSpaceRemove(obj){
	return obj.replace(/^\s+|\s+$/g, '');
}

/**
 * @note 값과 삭제할 옵션을 입력받아 값에서 옵션을 삭제하고 리턴한다.
 * @param str
 *   - 문자열
 * @param option
 *   - 삭제할 옵션
 *   - { hyphen : 'Y', underScore : 'Y', dot : 'Y', comma : 'Y', slash : 'Y', colon : 'Y'}
 * @returns String
 *   - 옵션값이 삭제된 문자열
 * @author PDW
 */
function gfn_removeSelectedOptionFromString(str, option){

	this.exp_underScore = '_';
	this.exp_hyphen = '\\-';
	this.exp_dot = '\\.';
	this.exp_comma = ',';
	this.exp_slash = '\\/';
	this.exp_colon = '\\:';

	let regExpStr = '';

	let tempOption =  JSON.stringify(option);

	if(!gfn_isJson(tempOption)){//json만 허용
		return;
	}

	if( !gfn_isBlank(option.underScore) && option.underScore == 'Y')  regExpStr += this.exp_underScore;
	if( !gfn_isBlank(option.hyphen) && option.hyphen == 'Y')  regExpStr += this.exp_hyphen;
	if( !gfn_isBlank(option.dot) && option.dot == 'Y')  regExpStr += this.exp_dot;
	if( !gfn_isBlank(option.comma) && option.comma == 'Y')  regExpStr += this.exp_comma;
	if( !gfn_isBlank(option.slash) && option.slash == 'Y')  regExpStr += this.exp_slash;
	if( !gfn_isBlank(option.colon) && option.colon == 'Y')  regExpStr += this.exp_colon;

	let regExp = new RegExp('\[' + regExpStr + '\]*',"g");

	return str.replace(regExp, '');
}


/**
 * @note 문자열을 입력받아 0-9 (digit) 만 남기고 삭제후 리턴한다
 * @param str
 *   - 문자열
 * @returns String
 *   - digit 값
 * @author PDW
 */
function gfn_returnOnlyDigit(str){
	return str.replace(/[^0-9]*/g, '');
}

/**
 * @note fromDate와 toDate를 입력받아 fromDate가 더 크면 true를 리턴한다. (걸리면 트루)
 * @param fromDate 시작일 (yyyy-mm-dd , yyyy/mm/dd, yyyymmdd)
 * @param toDate 마지막일 (yyyy-mm-dd , yyyy/mm/dd, yyyymmdd)
 * @returns
 * @author PDW
 */
function gfn_compareDateFromTo(fromDate, toDate){

	if(gfn_isBlank(pFromDate) || gfn_isBlank(pFromDate)){
		console.log("value is Blank.");
		return;
	}

	let tempFromDate = gfn_removeSelectedOptionFromString(fromDate, { hyphen : 'Y', underScore : 'Y', dot : 'Y', comma : 'Y', slash : 'Y'});
	let tempToDate = gfn_removeSelectedOptionFromString(toDate, { hyphen : 'Y', underScore : 'Y', dot : 'Y', comma : 'Y', slash : 'Y'});

	//year, month, day[, hour, min, secent, milsec]
	let fDate = new Date();
	let tDate = new Date();
	try{
		fDate = new Date( tempFromDate.substr(0,4) + "-" + tempFromDate.substr(4,2) + "-" + tempFromDate.substr(6,2));
		tDate = new Date( tempToDate.substr(0,4) + "-" + tempToDate.substr(4,2) + "-" +  tempToDate.substr(6,2) );
	} catch (e) {
		console.log("올바른 형식의 날짜 형태가 아닙니다.", e);
	}

	if(fDate > tDate){
		console.log(fDate + "가 " + tDate + "보다 큽니다.");
		return true;
	}else{
		return false;
	}
}

/**
 * @note 문자열을 입력받아 이메일 형식인지 체크하여 결과를  반환한다. 이메일형식인가? yes(true), no(false)
 * @param str
 * @returns boolean
 * @author PDW
 */
function gfn_isEmailForm(str){
	//let _regExp = new RegExp(/(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])/,'g');
	let _regExp = new RegExp(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/);
	return _regExp.test(str);
}

/**
 * @note 문자열을 입력받아 phone number 형식인지 체크하여 결과를 반환한다. 폰넘버형식인가? yes(true), no(false)
 * @param str
 * @returns
 */
function gfn_isPhoneNumForm(str){
	//let _regExp = new RegExp(/^\d{3}\d{3,4}\d{4}$/,"g");
	let _regExp = new RegExp(/^(010)[0-9]{4}[0-9]{4}$/,"g");
	return _regExp.test(str);
}

/**
 * @note 문자열을 입력받아 비밀번호 형식인지 체크하여 결과를 반환한다. 비밀번호형식인가? yes(true), no(false)
 * @param str
 * @returns
 */
function gfn_isPwdForm1(str) {
	let patternVal = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,12}/; // 영문,숫자 조합, 8-12자리
	return patternVal.test(str);
}


/**
 * @note 문자열을 입력받아 비밀번호 형식인지 체크하여 결과를 반환한다. 비밀번호형식인가? yes(true), no(false)
 * @param str
 * @returns
 */
function gfn_isPwdForm2(str) {
	let patternVal = /(?=.*\d{1,50})(?=.*[~`!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/; // 영문2개이상, 숫자, 특수문자 포함 8자리 이상
	return patternVal.test(str);
}

// object 빈값인지 체크
function gfn_isEmptyObject(param) {
 	return Object.keys(param).length === 0 && param.constructor === Object;
}

// 시분(4자리 숫자)을 해당 포맷적용하여 표시 (EX : str이 0140, format이 ':' 이면 01:40 리턴) 
function gfn_setHHmmFormat(str, format) {
	if(gfn_isEmpty(str) || str.length != 4) {
		return '';
	}
	if(gfn_isEmpty(format)) {
		format = ':';	// default format
	}
	return str.substr(0,2) + format + str.substr(2,4);
}

/** @param YYMMDD
	@param YYYYMMDD 
	를 받아서
	YYYY.MM.DD(월) 형식의 데이터를 리턴해줍니다.
 */
function gfn_getDateFullName(str){
	let result = str.replace(/(\d{4})(\d{2})(\d{2})/, "$1.$2.$3");
	result += '('+gfn_getDayOfWeek(str)+')';
	return result;
}

// 요일코드를 받아서 요일명 첫글자만 리턴
function gfn_getDayofWeekName(str) {
	let rtnStr = '';
	//0~6(일요일~토요일)
	if(gfn_isEmpty(str) || str.length != 1) {
		return rtnStr;	
	}

	switch(str) {
		case '0' :
			rtnStr = '일';
		break;
		case '1' :
			rtnStr = '월';
		break;
		case '2' :
			rtnStr = '화';
		break;
		case '3' :
			rtnStr = '수';
		break;
		case '4' :
			rtnStr = '목';
		break;
		case '5' :
			rtnStr = '금';
		break;
		case '6' :
			rtnStr = '토';
		break;
	}
	
	return rtnStr;	
}
// YYYYMMDD CHAR(8)문자열을 입력받아서 요일을 리턴해줍니다. 
function gfn_getDayOfWeek(str){
	let result = gfn_YMDFormatter(str,'-');
	const week = ['일', '월', '화', '수', '목', '금', '토'];
	return week[new Date(result).getDay()];
}

// obj의 value 중 숫자만 남기고 모두 제거
function onlyInputNumber(obj) {
	let tagName = $(obj).prop('tagName');
	if(tagName != 'INPUT') {
		return '';
	}
	return $(obj).val($(obj).val().replace(/[^0-9]/g, ''));
}

/**	전화번호 중간자리와 끝자리를 잘라줍니다. */
function gfn_getTelnoCutting(telNo){
	let regex = /[^0-9]/g;
	telNo = telNo.replace(regex, '');

	return [telNo.slice(0,3), telNo.slice(3,7), telNo.slice(7,11)];
}
/** 생일YYYY MM DD로 잘라줍니다. */
function gfn_getBrdtCutting(brdt){
	let regex = /[^0-9]/g;
	brdt = brdt.replace(regex, '');
	return [brdt.slice(0,4), brdt.slice(4,6), brdt.slice(6,8)];	
}

/**	유저의 이메일을 @ 기준으로 잘릅니다. */
function gfn_getEmlCutting(eml){
	let marker = eml.indexOf('@');
	return [eml.slice(0, marker), eml.slice(marker+1, eml.length)];
}


/**
 * @note 문자열 일괄 변경
 * @param str : 대상 문자열 전체
 * @param searchStr : 변경 대상 문자
 * @param replaceStr : 변경할 문자
 * @returns
 */
function gfn_replaceAll(str, searchStr, replaceStr) {
   return str.split(searchStr).join(replaceStr);
}

/**
 * @note 특정일자(yyyyymmdd) 가 무슨요일인지 구한다.
 * @returns
 */
function gfn_getDateStr(dateStr) {
	let yyyyMMdd = String(dateStr);
	let sYear = yyyyMMdd.substring(0,4);
	let sMonth = yyyyMMdd.substring(4,6);
	let sDate = yyyyMMdd.substring(6,8);
	let date = new Date(Number(sYear), Number(sMonth)-1, Number(sDate));
	let week = ['일', '월', '화', '수', '목', '금', '토'];
	return  week[date.getDay()];
}

/**
* 생년월일이 2022년 기준) 현재연도를 구해서 22년보다 크면 
* 앞에 1976 더 작을경우 2019 방식으로 숫자를 추가해줍니다.
* @param  : YYMMDD
* @return : YYYYMMDD
*/
function gfn_getYear(targetDate){
	
	let tToday = new Date();
	let tYear = tToday.getFullYear().toString().substr(-2);
	let tMonth = (tToday.getMonth() + 1).toString().padStart(2, '0');
	let tDate = tToday.getDate().toString().padStart(2, '0');
	let tFormattedDate = tYear + tMonth + tDate;
	
	console.log(' formattedDate = ' + tFormattedDate);
	if( targetDate.substr(0,2) == tYear ){
		return targetDate > tFormattedDate ? '19' + targetDate : '20' + targetDate;
	}
	const date = new Date().getFullYear();
	if( Number(targetDate.substr(0,2)) > Number(String(date).substr(2,4)) ){
		return String(Number(String(date).substr(0,2))-1) + targetDate;
	} else {
		return String(Number(String(date).substr(0,2))) + targetDate;
	}
}

// 두날짜의 차이를 일수를 구한다.(  
// ex : betweenDay('20210201','20210207') 호출 -> 결과 6
function gfn_betweenDay(firstDate, secondDate) {
    let firstDateObj = new Date(firstDate.substring(0, 4), firstDate.substring(4, 6) - 1, firstDate.substring(6, 8));
    let secondDateObj = new Date(secondDate.substring(0, 4), secondDate.substring(4, 6) - 1, secondDate.substring(6, 8));
    let betweenTime = Math.abs(secondDateObj.getTime() - firstDateObj.getTime());
    return Math.floor(betweenTime / (1000 * 60 * 60 * 24));
}
function gfn_headerImgChange(newYn){
	if(newYn == 'Y'){
		$('#headerNtcImg').attr('src', "/images/egovframework/main/quick-Group97.png");
	} else {
		$('#headerNtcImg').attr('src', "/images/egovframework/main/quick-news.png");
	}
}


// 현재일자를 yyyymmddhhss 형식으로 리턴
function getCurrentDateYyyymmddhhmmss(){
    let date = new Date(); // Data 객체 생성
    let year = date.getFullYear().toString(); // 년도 구하기
    let month = date.getMonth() + 1; // 월 구하기
    month = month < 10 ? '0' + month.toString() : month.toString(); // 10월 미만 0 추가
    let day = date.getDate(); // 날짜 구하기
    day = day < 10 ? '0' + day.toString() : day.toString(); // 10일 미만 0 추가
    let hour = date.getHours(); // 시간 구하기
    hour = hour < 10 ? '0' + hour.toString() : hour.toString(); // 10시 미만 0 추가
    let minites = date.getMinutes(); // 분 구하기
    minites = minites < 10 ? '0' + minites.toString() : minites.toString(); // 10분 미만 0 추가
    let seconds = date.getSeconds(); // 초 구하기
    seconds = seconds < 10 ? '0' + seconds.toString() : seconds.toString(); // 10초 미만 0 추가
    return year + month + day + hour + minites + seconds; // yyyymmddhhmmss 형식으로 리턴
}

/** 몇일전/몇일후 날짜를 구하는 함수 */
/** @param 'yyyymmdd' */
function gfn_aFewDaysLater(date, day, symbol){
	let targetDate = new Date(date.substring(0,4) + '-' + date.substring(4,6) + '-' + date.substring(6,8));
	let aFewDaysLater; 
	if(gfn_isEmpty(symbol) || symbol == '-'){
		aFewDaysLater = new Date(targetDate.getTime() - (day * 24 * 60 * 60 * 1000));
	} else {
		aFewDaysLater = new Date(targetDate.getTime() + (day * 24 * 60 * 60 * 1000));
	}
	
	return aFewDaysLater;
}


// 생년월일을 입력받아 만나이 계산 후 리턴
function fn_getAge(yyyymmdd) {
	if(gfn_isEmpty(yyyymmdd) || yyyymmdd.length < 8) {
		return '';
	}
	const today = new Date();
	console.log(yyyymmdd.substr(0,4));
	const birthDay = new Date(yyyymmdd.substr(0,4) + "-" + yyyymmdd.substr(4,2) + "-" + yyyymmdd.substr(6,2));
	let age = 0;
	age = today.getFullYear() - birthDay.getFullYear();	
	birthDay.setFullYear(today.getFullYear());
  	if (today.getTime() < birthDay.getTime()) {
    	age--;
  	}
	return age;
}

/**
	유효한 생년월일 유효성검사
	1. 글자가 섞여있으면 false
	2. 8글자가 아니면 false
	3. 현재 올해보다 높은숫자가 들어오면 false
	4. 월에 맞지않은 일자가 들어오면 false
 */
function gfn_validationCheckBrdt(brdtValue){
    if( isNaN(Number(brdtValue)) ){
        return false;
    }
    if(brdtValue.length != 8){
        return false;
    }

    let validYear =  brdtValue.slice(0, 4);   // 인덱스 0부터 3까지 슬라이스 연도
    let validMonth = brdtValue.slice(4, 6);   // 인덱스 4부터 5까지 슬라이스 월
    let validDay = 	 brdtValue.slice(6);      // 인덱스 6부터 끝까지 슬라이스 일
    const toYear = new Date().getFullYear();  // 현재연도
    // 현재 연도보다 크거나 너무작으면 false
    if(validYear > toYear || validYear < (toYear - 150) ){
        console.log(+ validYear < (toYear - 150));
        
        return false;
    }
    // 현재 월이 0보다 크거나 12보다 크면 false
    if(validMonth < 1 || validMonth > 12){
        return false;
    }
    // 현재 일수가 맞지않을경우
    if(validDay < 1 || validDay > gfn_getFullDay(validYear, validMonth)){
        return false;
    }
    return true;
    
}

// 연, 월에 해당하는 일수를 가져옵니다. 함수안에서 유효성검사가 없으니 '1999', '06'형태로 넣어주셔야합니다.
function gfn_getFullDay(year, month){
    const leapYear = year % 4;
    let fullDays = [31, 28, 31, 30, 31, 30 ,31, 31, 30, 31, 30, 31];
    
    // 윤년이면 월 28 => 29
    fullDays[1] = leapYear == 0 ? fullDays[1] + 1 : fullDays[1];
    
    return fullDays[month-1];
        
}

// 태어난 개월수로 나이를 계산합니다.
function gfn_getTrueYear(yyyymmdd, targetYmd){	
	// 내가 태어난 연도를 기준으로 현재까지의 일자를 구합니다
	const birthDate = new Date(yyyymmdd.substr(0,4), yyyymmdd.substr(4,2) - 1, yyyymmdd.substr(6,2));
	let currentDate = new Date();
	if(!gfn_isEmpty(targetYmd) && $.trim(targetYmd).length == 8) {
		// 출항일이 있는경우 출항일 기준 계산
		currentDate = new Date(targetYmd.substr(0,4), targetYmd.substr(4,2) - 1, targetYmd.substr(6,2));
	}
	const diffTime = currentDate - birthDate;
	const totalDays = Math.floor(diffTime / (1000 * 60 * 60 * 24));
	
	
	return totalDays;
}

function gfn_getLeapMonthCount(birthYear, targetYmd){
	let currentYear = new Date().getFullYear();
	if(!gfn_isEmpty(targetYmd) && $.trim(targetYmd).length == 8) {
		// 출항일이 있는경우 출항일 기준 계산
		targetYmd = targetYmd.substr(0,4);
	}
	let leapYears = 0;
	for (let year = birthYear; year <= currentYear; year++) {
	  if ( year == birthYear && 2 < birthYear.substr(4,6) ){
		continue;
	  }
	  if (new Date(year, 1, 29).getMonth() == 1) {
	    leapYears++;
	  }
	}
	return leapYears; 
}

// FetchApi
function fn_callFetch(url, type, param, callback){
    fetch(url,{
        method: type,
        headers:{ 'Content-Type': 'application/json' },
        body: JSON.stringify(param) })
    .then(response => response.json())
    .then(data=>callback(data))
    .catch(error=>console.error('Error', error))
}

// FetchApi 비동기처리
async function call_asyncFetchApi(url, type, param) {
    // call_fetchApi 함수를 async 함수로 변경
    try {
        const response = await fetch(url, {
            method: type,
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(param)
        });

        if (!response.ok) throw new Error('Network response was not ok');

        const data = await response.json();
        return data; // 데이터를 반환
    } catch (error) {
        console.error('There was a problem with the fetch operation: ', error);
        throw error;
    }
}