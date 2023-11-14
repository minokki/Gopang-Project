document.addEventListener("DOMContentLoaded", function() {

    /**
     * slick 불러오기
     */
    $('.visual').slick({
        autoplay: true,
        autoplaySpeed: 4000,
        arrows: false,
        dots:true,
    });

    function ActiveOnVisible__init() {
        $(window).resize(ActiveOnVisible__initOffset);
        ActiveOnVisible__initOffset();

        $(window).scroll(ActiveOnVisible__checkAndActive);
        ActiveOnVisible__checkAndActive();
    }

    function ActiveOnVisible__initOffset() {
        $('.active-on-visible').each(function(index, node) {
            var $node = $(node);

            var offsetTop = $node.offset().top;
            $node.attr('data-active-on-visible-offsetTop', offsetTop);

            if ( !$node.attr('data-active-on-visible-diff-y') ) {
                $node.attr('data-active-on-visible-diff-y', '0');
            }

            if ( !$node.attr('data-active-on-visible-delay') ) {
                $node.attr('data-active-on-visible-delay', '0');
            }
        });

        ActiveOnVisible__checkAndActive();
    }

    function ActiveOnVisible__checkAndActive() {
        $('.active-on-visible:not(.active)').each(function(index, node) {
            var $node = $(node);

            var offsetTop = $node.attr('data-active-on-visible-offsetTop') * 1;
            var diffY = parseInt($node.attr('data-active-on-visible-diff-y'));
            var delay = parseInt($node.attr('data-active-on-visible-delay'));

            if ( $(window).scrollTop() + $(window).height() + diffY > offsetTop ) {
                setTimeout(function() {
                    $node.addClass('active');
                }, delay);
            }
        });
    }

    ActiveOnVisible__init();


    AOS.init();

});