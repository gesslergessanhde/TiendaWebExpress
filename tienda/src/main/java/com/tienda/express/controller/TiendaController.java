package com.tienda.express.controller;

import com.tienda.express.model.CarritoItem;
import com.tienda.express.model.PedidoForm;
import com.tienda.express.model.Producto;
import com.tienda.express.repository.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TiendaController {

    @Autowired
    private ProductoRepository productoRepository;

    // --- HELPERS PARA EL CARRITO ---

    private List<CarritoItem> obtenerCarrito(HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }
        return carrito;
    }

    private int calcularTotalItems(List<CarritoItem> carrito) {
        return carrito.stream().mapToInt(CarritoItem::getCantidad).sum();
    }

    // Helper necesario para calcular el nuevo total al quitar un item asíncronamente
    private double calcularTotalPagar(List<CarritoItem> carrito) {
        double total = 0.0;
        for (CarritoItem item : carrito) {
            Producto p = productoRepository.findById(item.getProductoId()).orElse(null);
            if (p != null) {
                total += p.getPrecio() * item.getCantidad();
            }
        }
        return total;
    }

    @ModelAttribute
    public void agregarContadorCarrito(HttpSession session, Model model) {
        List<CarritoItem> carrito = obtenerCarrito(session);
        model.addAttribute("totalCarrito", calcularTotalItems(carrito));
    }

    // --- ENDPOINTS DE LA APLICACIÓN ---

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("destacados", productoRepository.findByDestacadoTrue());
        return "index";
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) String nombre,
                           @RequestParam(required = false) String categoria,
                           Model model) {
        List<Producto> productos = productoRepository.buscarProductos(nombre, categoria);
        model.addAttribute("productos", productos);

        model.addAttribute("categorias",
                productoRepository.findAll().stream()
                        .map(Producto::getCategoria)
                        .filter(c -> c != null && !c.isBlank())
                        .distinct()
                        .sorted()
                        .toList());

        model.addAttribute("nombreBuscar", nombre);
        model.addAttribute("categoriaSeleccionada", categoria);
        return "catalogo";
    }

    @GetMapping("/producto/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Producto p = productoRepository.findById(id).orElse(null);
        if (p == null) return "redirect:/catalogo";
        model.addAttribute("producto", p);
        return "detalle";
    }

    // --- MÓDULO DE CARRITO ACTUALIZADO ---

    @GetMapping("/carrito")
    public String verCarrito(HttpSession session, Model model) {
        List<CarritoItem> carrito = obtenerCarrito(session);

        // Creamos una estructura temporal combinada que Thymeleaf pueda leer sin problemas de Hibernate
        List<Map<String, Object>> itemsParaVista = new ArrayList<>();
        double totalPagar = 0.0;

        for (CarritoItem item : carrito) {
            Producto p = productoRepository.findById(item.getProductoId()).orElse(null);
            if (p != null) {
                double subtotal = p.getPrecio() * item.getCantidad();
                totalPagar += subtotal;

                Map<String, Object> dto = new HashMap<>();
                dto.put("id", p.getId());
                dto.put("nombre", p.getNombre());
                dto.put("imagen", p.getImagen());
                dto.put("categoria", p.getCategoria());
                dto.put("precio", p.getPrecio());
                dto.put("cantidad", item.getCantidad());
                dto.put("subtotal", subtotal);

                itemsParaVista.add(dto);
            }
        }

        model.addAttribute("items", itemsParaVista);
        model.addAttribute("totalPagar", totalPagar);
        return "carrito";
    }

    // MODIFICADO: Devuelve JSON con el total actualizado en lugar de redirigir
    @PostMapping("/carrito/agregar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarAlCarrito(@PathVariable Long id, HttpSession session) {
        Producto p = productoRepository.findById(id).orElse(null);
        Map<String, Object> respuesta = new HashMap<>();

        if (p != null && p.getDisponible()) {
            List<CarritoItem> carrito = obtenerCarrito(session);
            boolean existe = false;

            for (CarritoItem item : carrito) {
                if (item.getProductoId().equals(id)) {
                    item.setCantidad(item.getCantidad() + 1);
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                carrito.add(new CarritoItem(id, 1));
            }

            session.setAttribute("carrito", carrito);

            respuesta.put("status", "success");
            respuesta.put("totalItems", calcularTotalItems(carrito));
            return ResponseEntity.ok(respuesta);
        }

        respuesta.put("status", "error");
        respuesta.put("message", "Producto no disponible");
        return ResponseEntity.badRequest().body(respuesta);
    }

    // MODIFICADO: Devuelve JSON con el total de items y el nuevo total a pagar en vez de redirigir
    @PostMapping("/carrito/quitar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> quitarDelCarrito(@PathVariable Long id, HttpSession session) {
        List<CarritoItem> carrito = obtenerCarrito(session);
        carrito.removeIf(item -> item.getProductoId().equals(id));
        session.setAttribute("carrito", carrito);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("status", "success");
        respuesta.put("totalItems", calcularTotalItems(carrito));
        respuesta.put("nuevoTotalPagar", calcularTotalPagar(carrito));

        return ResponseEntity.ok(respuesta);
    }

    // --- MÓDULO DE PEDIDOS ---

    @GetMapping("/pedido")
    public String pedidoForm(@RequestParam(required = false) String producto, Model model) {
        PedidoForm form = new PedidoForm();
        if (producto != null) form.setProducto(producto);
        model.addAttribute("pedidoForm", form);
        model.addAttribute("productos", productoRepository.findAll());
        return "pedido";
    }

    @PostMapping("/pedido")
    public String enviarPedido(@Valid @ModelAttribute("pedidoForm") PedidoForm pedidoForm,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productos", productoRepository.findAll());
            return "pedido";
        }
        model.addAttribute("pedido", pedidoForm);
        return "confirmacion";
    }
}