// Tienda Web Express - Interactividad del Cliente

// Mensaje flotante animado (Toast)
function showToast(msg) {
  const t = document.getElementById('toast');
  if (!t) return alert(msg);
  t.textContent = msg;
  t.classList.add('show');
  clearTimeout(showToast._t);
  showToast._t = setTimeout(() => t.classList.remove('show'), 2200);
}

// 1) Animación visual al presionar Agregar
document.querySelectorAll('.btn-add').forEach(btn => {
  btn.addEventListener('click', e => {
    if (btn.disabled) {
      e.preventDefault();
      return;
    }
    // NOTA: Ya NO usamos e.preventDefault() para el flujo normal.
    // Dejamos que el formulario haga el POST real a Spring Boot para guardar en la sesión de Java.
    const nombre = btn.dataset.nombre || 'Producto';
    showToast(`Agregado: ${nombre}`);
  });
});

// 2) Mostrar/ocultar descripción dinámica en la vista de detalle
const toggle = document.getElementById('toggle-desc');
const desc = document.getElementById('descripcion');
if (toggle && desc) {
  toggle.addEventListener('click', () => {
    desc.classList.toggle('oculto');
    toggle.textContent = desc.classList.contains('oculto') ? 'Ver descripcion' : 'Ocultar descripcion';
  });
}

// NOTA ELIMINADA: Se quitó el filtro por categoría manual de JS (Sección 3)
// y la validación duplicada de pedidos (Sección 5) porque ahora Spring Boot,
// ProductoRepository y Jakarta Validation (@Valid) se encargan de todo de forma segura en el servidor.