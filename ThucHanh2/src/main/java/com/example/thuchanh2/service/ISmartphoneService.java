package com.example.thuchanh2.service;

import com.example.thuchanh2.model.Smartphone;

import java.util.Optional;

public interface ISmartphoneService {
    Iterable<Smartphone> findAll();
    Optional<Smartphone> findById(Long id);
    Smartphone save(Smartphone smartphone);
    void deleteById(Long id);
}
