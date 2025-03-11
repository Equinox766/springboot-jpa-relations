package org.equinox.jparelations;

import org.equinox.jparelations.entities.Address;
import org.equinox.jparelations.entities.Client;
import org.equinox.jparelations.entities.Invoice;
import org.equinox.jparelations.repositories.IClientRepository;
import org.equinox.jparelations.repositories.IInvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@SpringBootApplication
public class JpaRelationsApplication implements CommandLineRunner {

    @Autowired
    private IClientRepository clientRepository;

    @Autowired
    private IInvoiceRepository invoiceRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaRelationsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        oneToManyBidirectionalFindById();
    }

    @Transactional
    public void  manyToOneCreate() {
        Client client = new Client(null,"EMANUEL", "LEZCANO", null, null);
        clientRepository.save(client);

        Invoice  invoice = new Invoice(null, "Compras de tienda", 2000L, client);
        Invoice InvoiceDB = invoiceRepository.save(invoice);
        System.out.println("Invoice ID: " + InvoiceDB);
    }

    @Transactional
    public void  manyToOneFindById() {
        clientRepository.findById(1L).ifPresent(client -> {
            Invoice  invoice = new Invoice(null, "Compras de tienda", 2000L, client);
            Invoice InvoiceDB = invoiceRepository.save(invoice);
            System.out.println("Invoice ID: " + InvoiceDB);
        });
    }

    @Transactional
    public void oneToManyCreate() {
        Set<Address> addresses = new HashSet<>();
        addresses.add(new Address(null, "Coronel Oviedo", 54687));
        addresses.add(new Address(null, "San Bernardino", 458777));

        Client client = new Client(null,"EMANUEL", "LEZCANO", addresses, null);
        Client clientDB = clientRepository.save(client);
        System.out.println(clientDB);

    }

    @Transactional
    public void oneToManyFindById() {
        Set<Address> addresses = new HashSet<>();
        addresses.add(new Address(null, "Coronel Oviedo", 54687));
        addresses.add(new Address(null, "San Bernardino", 458777));
        clientRepository.findById(1L).ifPresent(client -> {
            client.setAddresses(addresses);
            System.out.println(clientRepository.save(client));
        });
/*
        Client client = clientRepository.findById(1L).orElseThrow();
        List<Address> addresses = new ArrayList<>();
        addresses.add(new Address(null, "Coronel Oviedo", 54687));
        addresses.add(new Address(null, "San Bernardino", 458777));
        client.setAddresses(addresses);
        System.out.println(clientRepository.save(client));
*/
    }

    @Transactional
    public void oneToManyDeleteById() {
        Set<Address> addresses = new HashSet<>();
        addresses.add(new Address(null, "Coronel Oviedo", 54687));
        addresses.add(new Address(null, "San Bernardino", 458777));

        Client client = clientRepository.save(new Client(null,"EMANUEL", "LEZCANO", addresses, null));
        System.out.println(client);
        clientRepository.findById(client.getId()).ifPresent(c -> {
//            c.getAddresses().remove(address);
            System.out.println(clientRepository.save(c));
        });

/*
        clientRepository.findById(1L).ifPresent( c -> {
            System.out.println(clientRepository.save(c));

        });
*/

    }

    @Transactional
    public void oneToManyDelete() {
/*        Address address = new Address(null, "Av. siempre viva", 54687);
        Address address1  = new Address(null, "Av. Aviadores del chaco", 458777);
        List<Address> addresses = new ArrayList<>();
        addresses.add(address);
        addresses.add(address1);*/

        Set<Address> addresses = new HashSet<>();
        addresses.add(new Address(null, "Coronel Oviedo", 54687));
        addresses.add(new Address(null, "San Bernardino", 458777));

        Client client = clientRepository.save(new Client(null,"EMANUEL", "LEZCANO", addresses, null));
        System.out.println(client);
        clientRepository.findOne(client.getId()).ifPresent(c -> {
//            c.getAddresses().remove(address);
            System.out.println(clientRepository.save(c));
        });

/*
        clientRepository.findById(1L).ifPresent( c -> {
            System.out.println(clientRepository.save(c));

        });
*/

    }

    @Transactional
    public void oneToManyBidirectionalCreate() {
        Client client = new Client(null,"EMANUEL", "LEZCANO", null, null);
        Set<Invoice> invoices = new HashSet<>();
        invoices.add(new Invoice(null, "Compras de tienda", 2000L, client));
        invoices.add( new Invoice(null, "Compras de Oficina", 2000L, client));
        client.setInvoices(invoices);

        System.out.println(clientRepository.save(client));
    }

    @Transactional
    public void oneToManyBidirectionalFindById() {
        Optional<Client> client = clientRepository.findWithRelations(1L);

        client.ifPresent(c -> {
            Invoice invoice = new Invoice(null, "Compras de tienda", 2000L, null);
            Invoice invoice2 = new Invoice(null, "Compras de Oficina", 2000L, null);
            invoice2.setClient(c);
            invoice.setClient(c);
            c.setInvoices(new HashSet<>(Arrays.asList(invoice, invoice2)));
            System.out.println(clientRepository.save(c));
//            invoiceRepository.saveAll(new HashSet<>(Arrays.asList(invoice, invoice2)));

//            System.out.println(invoiceRepository.saveAll(new HashSet<>(Arrays.asList(invoice, invoice2))));
        });

    }
}
