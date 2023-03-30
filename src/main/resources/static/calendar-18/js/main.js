$(function () {

    // rome(inline_cal, { time: false });

    // rome 라이브러리를 사용하여 인라인 달력을 구현
    rome(inline_cal_from, {

        // 시간 선택기를 비활성화
        time: false,

        // 입력 값이 사용할 날짜 형식을 지정 ("년도-월-일")
        inputFormat: 'YYYY-MM-DD',

        // 입력된 날짜가 inline_cal_to 변수에 설정된 날짜보다 이후이거나 같은지를 확인하는 유효성 검사
        dateValidator: rome.val.beforeEq(inline_cal_to)}).on('data', function (value) {

        // result_from 변수에 값을 할당
        result_from.value = value;

        document.getElementById('result_from').value = value;

    });

    rome(inline_cal_to, {

        // 시간 선택기를 비활성화
        time: false,

        // 입력 값이 사용할 날짜 형식을 지정 ("년도-월-일")
        inputFormat: 'YYYY-MM-DD',

        // 입력된 날짜가 inline_cal_from 변수에 설정된 날짜보다 이전이거나 같은지를 확인하는 유효성 검사
        dateValidator: rome.val.afterEq(inline_cal_from)}).on('data', function (value) {

        // result_to 변수에 값을 할당
        result_to.value = value;
    });


});