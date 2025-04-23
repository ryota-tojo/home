document.addEventListener('DOMContentLoaded', function () {
    const todayBtn = document.querySelector('button[name="today-btn"]');
    const dateInput = document.querySelector('input[name="date"]');

    if (todayBtn && dateInput) {
        todayBtn.addEventListener('click', function () {
            const today = new Date();
            const yyyy = today.getFullYear();
            const mm = String(today.getMonth() + 1).padStart(2, '0');
            const dd = String(today.getDate()).padStart(2, '0');
            dateInput.value = `${yyyy}-${mm}-${dd}`;
        });
    }
});