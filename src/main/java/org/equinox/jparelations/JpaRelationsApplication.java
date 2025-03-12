package org.equinox.jparelations;

import org.equinox.jparelations.entities.*;
import org.equinox.jparelations.repositories.*;
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

    @Autowired
    private IClientDetailsRepository clientDetailsRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private ICourseRepository  courseRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaRelationsApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        bidireccionalRemove();
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

    @Transactional
    public void removeBidirectionalFindById() {
        Optional<Client> client = clientRepository.findWithRelations(1L);

        client.ifPresent(c -> {
            Invoice invoice = new Invoice(null, "Compras de tienda", 2000L, null);
            Invoice invoice2 = new Invoice(null, "Compras de Oficina", 2000L, null);
            invoice2.setClient(c);
            invoice.setClient(c);
            c.setInvoices(new HashSet<>(Arrays.asList(invoice, invoice2)));
            System.out.println(clientRepository.save(c));
        });

        Optional<Client> clientDB = clientRepository.findWithRelations(1L);
        clientDB.ifPresent(c -> {
           Optional<Invoice> invoiceDB = invoiceRepository.findById(2L);
           invoiceDB.ifPresent(c2 -> {
               c.getInvoices().remove(c2);
               c2.setClient(null);
               clientRepository.save(c);
               System.out.println(c);
           });
        });
    }

    @Transactional
    public void removeBidirectionalCreated() {
        Client client = new Client(null, "Pipo", "Gorosito", null, null);

        Invoice invoice = new Invoice(null, "Compras de tienda", 2000L, null);
        Invoice invoice2 = new Invoice(null, "Compras de Oficina", 2000L, null);
        invoice2.setClient(client);
        invoice.setClient(client);
        client.setInvoices(new HashSet<>(Arrays.asList(invoice, invoice2)));
        Client cliDB = clientRepository.save(client);
        System.out.println(cliDB);

        Optional<Client> clientDB = clientRepository.findWithRelations(cliDB.getId());
        clientDB.ifPresent(c -> {
            Optional<Invoice> invoiceDB = invoiceRepository.findById(2L);
            invoiceDB.ifPresent(c2 -> {
                c.getInvoices().remove(c2);
                c2.setClient(null);
                clientRepository.save(c);
                System.out.println(c);
            });
        });
    }

    @Transactional
    public void oneToOne() {
        ClientDetail clientDetail = new ClientDetail(true, 5000);
        clientDetailsRepository.save(clientDetail);
        Client client = new Client("EMANUEL", "LEZCANO");
        client.setClientDetail(clientDetail);
        clientRepository.save(client);
    }

    @Transactional
    public void oneToOneFindById() {
        ClientDetail clientDetail = new ClientDetail(true, 5000);
        clientDetailsRepository.save(clientDetail);
        Optional<Client> client = clientRepository.findWithRelations(1L);
        client.ifPresent(c -> {
            c.setClientDetail(clientDetail);
            clientRepository.save(c);
            System.out.println(c);
        });
    }

    @Transactional
    public void oneToOneBidirectionalFindById() {
        Client client = new Client("EMANUEL", "LEZCANO");
        ClientDetail clientDetail = new ClientDetail(true, 5000);
        client.setClientDetail(clientDetail);
        clientDetail.setClient(client);
        System.out.println(clientRepository.save(client));
    }

    @Transactional
    public void findByIdClientDetail() {
        Optional<Client> client = clientRepository.findWithRelations(1L);
        client.ifPresent(c -> {
            ClientDetail clientDetail = new ClientDetail(true, 5000);
            c.setClientDetail(clientDetail);
            clientDetail.setClient(c);
            System.out.println(clientRepository.save(c));
        });
    }

    @Transactional
    public void manyToMany() {
        Course course1 = new Course("Java desde 0 hasta experto", "JuazPy Tutoriales");
        Course course2 = new Course("PHP desde 0 hasta experto", "JuazPy Tutoriales");

        Student student1 = new Student("EMANUEL", "LEZCANO");
        Student student2 = new Student("JOSH", "BARZALA");

        student1.setCourses(Set.of(course2));
        student2.setCourses(Set.of(course1, course2));
        System.out.println(studentRepository.saveAll(List.of(student1, student2)));;

    }

    @Transactional
    public void manyToManyFindById() {
        Course java = courseRepository.findById(1L).get();
        Course php = courseRepository.findById(2L).get();

        studentRepository.findById(1L).ifPresent(student -> {
            student.setCourses(Set.of(java, php));
            System.out.println(studentRepository.save(student));
        });

        studentRepository.findById(2L).ifPresent(student -> {
            student.setCourses(Set.of(php));
            System.out.println(studentRepository.save(student));
        });
    }

    @Transactional
    public void manyToManyDelete() {
        Course java = courseRepository.findById(1L).get();
        Course php = courseRepository.findById(2L).get();

        studentRepository.findById(1L).ifPresent(student -> {
            student.setCourses(Set.of(java, php));
            System.out.println(studentRepository.save(student));
        });

        studentRepository.findById(2L).ifPresent(student -> {
            student.setCourses(Set.of(php));
            System.out.println(studentRepository.save(student));
        });

        Optional<Student> studentOptional =  studentRepository.findById(1L);
        studentOptional.ifPresent(student -> {
            Optional<Course> courseOptional =  courseRepository.findById(1L);
            courseOptional.ifPresent(course -> {
               student.getCourses().remove(course);
               System.out.println(studentRepository.save(student));
            });
        });

/*
        Optional<Student> studentOptional =  studentRepository.findByRelations(1L);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Optional<Course> courseOptional =  courseRepository.findById(1L);
            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();
                student.getCourses().remove(course);
                System.out.println(studentRepository.save(student));
            }
        }
*/
    }

    @Transactional
    public void manyToManyDeleteBidireccional() {
        Course course1 = new Course("Java desde 0 hasta experto", "JuazPy Tutoriales");
        Course course2 = new Course("PHP desde 0 hasta experto", "JuazPy Tutoriales");

        Student student1 = new Student("EMANUEL", "LEZCANO");
        Student student2 = new Student("JOSH", "BARZALA");

        student1.addCourse(course1);
        student1.addCourse(course2);
        student2.addCourse(course2);
        System.out.println(studentRepository.saveAll(List.of(student1, student2)));;
    }

    @Transactional
    public void bidireccionalRemove() {
        Course java = courseRepository.findById(1L).get();
        Course php = courseRepository.findById(2L).get();

        studentRepository.findById(1L).ifPresent(student -> {
            student.setCourses(Set.of(java, php));
            System.out.println(studentRepository.save(student));
        });

        studentRepository.findById(2L).ifPresent(student -> {
            student.setCourses(Set.of(php));
            System.out.println(studentRepository.save(student));
        });

        Optional<Student> studentOptional = studentRepository.findByRelations(1L);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Optional<Course> courseOptional =  courseRepository.findWithStudents(1L);
            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();
                student.removeCourse(course);
                System.out.println(studentRepository.save(student));
            }
        }
    }
}
