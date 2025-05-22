function loadRentals() {
    fetch('/api/rentals')
        .then(res => res.json())
        .then(rentals => {
            const list = document.getElementById('rentalList');
            list.innerHTML = '';
            rentals.forEach(r => {
                const li = document.createElement('li');
                li.textContent = `ID: ${r.id} | Customer: ${r.customerId} | Movie: ${r.movieId} | Start: ${r.rentalDate}` +
                    (r.returnDate ? ` | Returned: ${r.returnDate}` : '');
                list.appendChild(li);
            });
        });
}

function loadCustomersForRental() {
    fetch('/api/customers')
        .then(res => res.json())
        .then(customers => {
            const select = document.getElementById('customerSelect');
            select.innerHTML = '';
            customers.forEach(c => {
                const option = document.createElement('option');
                option.value = c.id;
                option.textContent = `${c.name} (${c.email})`;
                select.appendChild(option);
            });
        });
}

function loadMoviesForRental() {
    fetch('/api/movies')
        .then(res => res.json())
        .then(movies => {
            const select = document.getElementById('movieSelect');
            select.innerHTML = '';
            movies.forEach(m => {
                const option = document.createElement('option');
                option.value = m.id;
                option.textContent = `${m.title} (${m.releaseYear})`;
                select.appendChild(option);
            });
        });
}

function rentMovie() {
    const customerId = document.getElementById('customerSelect').value;
    const movieId = document.getElementById('movieSelect').value;

    fetch(`/api/rentals/rent?customerId=${customerId}&movieId=${movieId}`, {
        method: 'POST'
    }).then(() => loadRentals());
}

function returnMovie() {
    const rentalId = document.getElementById('returnRentalId').value;

    fetch(`/api/rentals/return?rentalId=${rentalId}`, {
        method: 'POST'
    }).then(() => {
        document.getElementById('returnRentalId').value = '';
        loadRentals();
    });
}

document.addEventListener('DOMContentLoaded', () => {
    loadRentals();
    loadCustomersForRental();
    loadMoviesForRental();
});
