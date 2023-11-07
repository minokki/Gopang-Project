$(document).ready(function () {
    // 폼 제출 이벤트 리스너 추가
    $('form.needs-validation').on('submit', function (event) {
        // 폼 유효성 검사 실행
        if (this.checkValidity() === false) {
            event.preventDefault();
            event.stopPropagation();
        }
        $(this).addClass('was-validated');
    });
});