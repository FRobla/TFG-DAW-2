package com.proyecto3d.backend.apirest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto3d.backend.apirest.model.entity.Material;
import com.proyecto3d.backend.apirest.model.service.MaterialService;


@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class MaterialController {
    
    @Autowired
    private MaterialService materialService;

    @GetMapping("/materiales")
    public List<Material> index() {
        return materialService.findAll();
    }

    @GetMapping("/material/{id}")
    public Material show(@PathVariable(name = "id") Long id) {
        return materialService.findById(id);
    }

    @PostMapping("/material")
    public Material create(@RequestBody Material material) {
        return materialService.save(material);
    }
    
    @PutMapping("/material/{id}")
    public Material update(@PathVariable(name = "id") Long id, @RequestBody Material material) {
        Material materialActual = materialService.findById(id);
        materialActual.setNombre(material.getNombre());
        materialActual.setFabricante(material.getFabricante());
        materialActual.setColor(material.getColor());
        materialActual.setTipo(material.getTipo());
        materialActual.setPrecioKg(material.getPrecioKg());
        return materialService.save(materialActual);
    }
    
    @DeleteMapping("/material/{id}")
    public void delete(@PathVariable(name = "id") Long id) {
        materialService.deleteById(id);
    }

    @GetMapping("/material/nombre/{nombre}")
    public Material findByNombre(@PathVariable(name = "nombre") String nombre) {
        return materialService.findByNombre(nombre);
    }
}
