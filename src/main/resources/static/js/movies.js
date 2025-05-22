// Betölti az összes filmet
function loadMovies() {
    fetch('/api/movies')
        .then(res => res.json())
        .then(movies => {
            const list = document.getElementById('movieList');
            list.innerHTML = '';
            movies.forEach(m => {
                const li = document.createElement('li');
                li.textContent = `${m.id}: ${m.title} (${m.releaseDate}) - ${m.genre} - ${m.available ? 'Elérhető' : 'Nem elérhető'}`;
                list.appendChild(li);
            });
        });
}

// Új film hozzáadása
function addMovie() {
    const title = document.getElementById('title').value;
    const genre = document.getElementById('genre').value;
    const releaseDate = document.getElementById('releaseDate').value; // teljes dátum
    const available = document.getElementById('available').checked;

    fetch('/api/movies', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, genre, releaseDate, available })
    }).then(() => {
        // Űrlap törlése
        document.getElementById('title').value = '';
        document.getElementById('genre').value = '';
        document.getElementById('releaseDate').value = '';
        document.getElementById('available').checked = true;
        loadMovies();
    });
}

// Film törlése ID alapján
function deleteMovie() {
    const id = document.getElementById('deleteMovieId').value;
    fetch(`/api/movies/${id}`, {
        method: 'DELETE'
    }).then(() => {
        document.getElementById('deleteMovieId').value = '';
        loadMovies();
    });
}

// Betöltéskor meghívja a listázást
document.addEventListener('DOMContentLoaded', loadMovies);
