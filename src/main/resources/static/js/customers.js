// GET customers and list them
function loadCustomers() {
  fetch('/api/customers')
    .then(res => {
      if (!res.ok) throw new Error('Nem sikerült lekérni az ügyfeleket');
      return res.json();
    })
    .then(customers => {
      const list = document.getElementById('customerList');
      list.innerHTML = '';
      customers.forEach(c => {
        const li = document.createElement('li');
        li.textContent = `${c.id}: ${c.name} (${c.email})`;
        list.appendChild(li);
      });
    })
    .catch(err => console.error('Hiba:', err));
}

// POST new customer
function addCustomer() {
  const name = document.getElementById('name').value;
  const email = document.getElementById('email').value;

  if (!name || !email) {
    alert('Név és email megadása kötelező');
    return;
  }

  fetch('/api/customers', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name, email })
  })
    .then(res => {
      if (!res.ok) throw new Error('Nem sikerült hozzáadni az ügyfelet');
      document.getElementById('name').value = '';
      document.getElementById('email').value = '';
      loadCustomers();
    })
    .catch(err => console.error('Hiba:', err));
}

// DELETE customer
function deleteCustomer() {
  const id = document.getElementById('deleteId').value;

  if (!id) {
    alert('ID megadása kötelező');
    return;
  }

  fetch(`/api/customers/${id}`, {
    method: 'DELETE'
  })
    .then(res => {
      if (!res.ok) throw new Error('Nem sikerült törölni az ügyfelet');
      document.getElementById('deleteId').value = '';
      loadCustomers();
    })
    .catch(err => console.error('Hiba:', err));
}

// Eseménykezelők bekötése
document.addEventListener('DOMContentLoaded', () => {
  loadCustomers();

  document.getElementById('addCustomerBtn').addEventListener('click', addCustomer);
  document.getElementById('deleteCustomerBtn').addEventListener('click', deleteCustomer);
});
