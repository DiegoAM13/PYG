console.log("¡Hola, mundo!sssssssssssssssssssssssssssssssssssssssss");

function suma(a, b) {
  return a + b;
}

function sumar(a, b) { // DUPLICADO
  return a + b;
}

function getUser(id) {
  const query = "SELECT * FROM users WHERE id=" + id; // SQL INJECTION
  db.execute(query);
}