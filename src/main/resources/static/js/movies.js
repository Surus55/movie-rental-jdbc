// GET movies and list them
function loadMovies() {
    fetch('/api/movies')
        .then(res => res.json())
        .then(movies => {
            const list = document.getElementById('movieList');
            list.innerHTML = '';
            movies.forEach(m => {
                const li = document.createElement('li');
                li.textContent = `${m.id}: ${m.title} (${m.releaseYear})`;
                list.appendChild(li);
            });
        });
}

// POST new movie
function addMovie() {
    const title = document.getElementById('title').value;
    const releaseYear = document.getElementById('year').value;

    fetch('/api/movies', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ title, releaseYear })
    }).then(() => {
        document.getElementById('title').value = '';
        document.getElementById('year').value = '';
        loadMovies();
    });
}

// DELETE movie
function deleteMovie() {
    const id = document.getElementById('deleteMovieId').value;
    fetch(`/api/movies/${id}`, {
        method: 'DELETE'
    }).then(() => {
        document.getElementById('deleteMovieId').value = '';
        loadMovies();
    });
}

document.addEventListener('DOMContentLoaded', loadMovies);
