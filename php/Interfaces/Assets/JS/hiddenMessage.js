document.addEventListener('DOMContentLoaded', () => {
    const messageElement = document.querySelector('.message-fields');
    if (messageElement) {
        messageElement.addEventListener('animationend', () => {
            messageElement.style.display = 'none';
        });
    }
});