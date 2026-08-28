(function () {
    var timer = document.getElementById('sessionTimer');
    if (!timer) return;

    var totalSeconds = 30 * 60;

    function render() {
        var minutes = Math.floor(totalSeconds / 60);
        var seconds = totalSeconds % 60;
        timer.textContent = (minutes < 10 ? '0' : '') + minutes + ':' + (seconds < 10 ? '0' : '') + seconds;

        if (totalSeconds <= 300 && totalSeconds > 0) {
            timer.style.fontWeight = '700';
        }
        if (totalSeconds <= 0) {
            window.location.href = '/login?sessionExpired=true';
            return;
        }
        totalSeconds -= 1;
    }

    render();
    window.setInterval(render, 1000);
})();
