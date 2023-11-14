document.addEventListener('DOMContentLoaded', () => {
    /**
     * 모바일 햄버거 x표시
     */
    var burger = $('.menu-trigger');

    burger.each(function(index){
        var $this = $(this);

        $this.on('click', function(e){
            e.preventDefault();
            $(this).toggleClass('active-' + (index+1));
        })
    });

    /**
     * 모바일 햄버거 버튼 click
     */
    document.querySelectorAll('.mobile-nav-toggle').forEach(el => {
        el.addEventListener('click', function(event) {
            event.preventDefault();
            mobileNavToggle();
        })
    });
    function mobileNavToggle() {
        document.querySelector('body').classList.toggle('mobile-nav-active');
    }

    /**
     * 드롭다운
     */
    const navDropdowns = document.querySelectorAll('.custom_navbar .dropdown > a');

    navDropdowns.forEach(el => {
        el.addEventListener('click', function(event) {
            if (document.querySelector('.mobile-nav-active')) {
                event.preventDefault();
                this.classList.toggle('active');
                this.nextElementSibling.classList.toggle('dropdown-active');
            }
        })
    });

    /**
     * 스크롤시 header sicked 추가
     */
    const selectHeader = document.querySelector('#header');
    if (selectHeader) {
        document.addEventListener('scroll', () => {
            if (window.scrollY > 80) {
                selectHeader.classList.add('sticked');
            } else {
                selectHeader.classList.remove('sticked');
            }
        });
    }

});
