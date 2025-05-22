let customers = {};
let movies = {};

function loadRentals() {
    Promise.all([
        fetch('/api/customers').then(res => res.json()),
        fetch('/api/movies').then(res => res.json()),
        fetch('/api/rentals').then(res => res.json())
    ]).then(([customerList, movieList, rentals]) => {
        // ID → Név/email map
        customers = {};
        customerList.forEach(c => {
            customers[c.id] = `${c.name} (${c.email})`;
        });

        // ID → Cím/dátum map
        movies = {};
        movieList.forEach(m => {
            movies[m.id] = `${m.title} (${m.releaseDate})`;
        });

        const list = document.getElementById('rentalList');
        list.innerHTML = '';

        rentals.forEach(r => {
            // Rugalmasság: ID vagy objektum alapján is kezeljük
            const customerId = r.customerId || (r.customer && r.customer.id);
            const movieId = r.movieId || (r.movie && r.movie.id);

            const customerName = customers[customerId] || `ID: ${customerId}`;
            const movieTitle = movies[movieId] || `ID: ${movieId}`;

            const li = document.createElement('li');
            li.textContent =
                `ID: ${r.id} | Customer: ${customerName} | Movie: ${movieTitle} | Start: ${r.rentalDate}` +
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
                option.textContent = `${m.title} (${m.releaseDate})`;
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
