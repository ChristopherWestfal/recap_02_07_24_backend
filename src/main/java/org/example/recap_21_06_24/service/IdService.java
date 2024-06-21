package org.example.recap_21_06_24.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class IdService {
    public String idGenerator(){
        return UUID.randomUUID().toString();
    }
}
