package com.product.star.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/")
public class ContactController {

    private final Map<Long, Contact> contacts = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @GetMapping("/contacts")
    public ArrayList<Contact> getAllContacts() {
        return new ArrayList<>(contacts.values());
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<Contact> getContact(@PathVariable Long contactId) {
        Contact contact = contacts.get(contactId);
        return contact != null ? ResponseEntity.ok(contact) : ResponseEntity.notFound().build();
    }

    @PostMapping("/contacts")
    public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {
        long id = idGenerator.getAndIncrement();
        contact.setId(id);
        contacts.put(id, contact);
        return ResponseEntity.status(HttpStatus.CREATED).body(contact);
    }

    @PutMapping("/contacts/{contactId}")
    public ResponseEntity<Contact> updateContact(@PathVariable Long contactId, @RequestBody Contact contact) {
        if (!contacts.containsKey(contactId)) {
            return ResponseEntity.notFound().build();
        }
        contact.setId(contactId);
        contacts.put(contactId, contact);
        return ResponseEntity.ok(contact);
    }
}