document.addEventListener('DOMContentLoaded', function() {
    if (window.location.hash) {
        var id = window.location.hash.substring(1);
        var el = document.getElementById(id);
        if (el) {
            setTimeout(function() {
                var offset = 80; // Adjust header height
                var elementPosition = el.getBoundingClientRect().top + window.pageYOffset;
                var maxScroll = document.body.scrollHeight - window.innerHeight;
                var scrollTo = Math.min(elementPosition - offset, maxScroll);
                window.scrollTo({ top: scrollTo, behavior: 'auto' });
            }, 100);
        }
    }
});
