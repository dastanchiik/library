(function() {
    function applyTheme() {
        const theme = localStorage.getItem('theme');
        if (theme === 'dark') {
            document.documentElement.classList.add('dark');
        } else {
            document.documentElement.classList.remove('dark');
        }
    }

    applyTheme();

    window.toggleTheme = function() {
        const html = document.documentElement;
        const icon = document.getElementById('themeIcon');
        
        if (html.classList.contains('dark')) {
            html.classList.remove('dark');
            if (icon) icon.innerText = '☀️';
            localStorage.setItem('theme', 'light');
        } else {
            html.classList.add('dark');
            if (icon) icon.innerText = '🌙';
            localStorage.setItem('theme', 'dark');
        }
    };

    window.initThemeIcon = function() {
        const icon = document.getElementById('themeIcon');
        if (icon) {
            icon.innerText = localStorage.getItem('theme') === 'dark' ? '🌙' : '☀️';
        }
    };

    window.safeLogout = function() {
        const theme = localStorage.getItem('theme');
        localStorage.clear();
        if (theme) localStorage.setItem('theme', theme);
        location.href = 'index.html';
    };
})();