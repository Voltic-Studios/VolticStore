document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('searchInput').addEventListener('keyup', function() {
        let searchValue = this.value.toLowerCase();
        let tableRows = document.getElementById('tableBody').getElementsByTagName('tr');
        for (let i = 0; i < tableRows.length; i++) {
            let rowText = tableRows[i].textContent.toLowerCase();
            tableRows[i].style.display = rowText.includes(searchValue) ? '' : 'none';
        }
    });
});