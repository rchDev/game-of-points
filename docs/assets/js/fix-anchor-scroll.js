window.onload = function() {
    if (!window.location.hash) return;

    const id = window.location.hash.slice(1);
    const el = document.getElementById(id);
    if (!el) return;

    const offset = 80; // Header height
    const elementPosition = el.getBoundingClientRect().top + window.pageYOffset;
    const maxScroll = document.body.scrollHeight - window.innerHeight;
    const scrollTo = Math.min(elementPosition - offset, maxScroll);

    window.scrollTo({ top: scrollTo, behavior: 'auto' });
};