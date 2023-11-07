//  캐러셀이 마우스 오버 시 일시 중지되는 동작을 제어
$(document).ready(function(){
    var myCarousel = document.querySelector('.carouselExampleIndicators')
    var carousel = new bootstrap.Carousel(myCarousel, {
        pause: false
    });
});


