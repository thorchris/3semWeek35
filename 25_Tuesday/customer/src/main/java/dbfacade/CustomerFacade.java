package dbfacade;

import entity.Customer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class CustomerFacade {

    private static EntityManagerFactory emf;
    private static CustomerFacade instance;

    private CustomerFacade() {
    }

    public static CustomerFacade getCustomerFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CustomerFacade();
        }
        return instance;
    }

    public Customer addCustomer(String fName, String lName) {
        Customer cus = new Customer(fName, lName);
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(cus);
            em.getTransaction().commit();
            return cus;
        } finally {
            em.close();
        }
    }

    public Customer findByID(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            Customer cus = em.find(Customer.class, id);
            return cus;
        } finally {
            em.close();
        }
    }

    public List<Customer> allCustomer() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Customer> query
                    = em.createQuery("Select customer from Customer customer", Customer.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
        public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        CustomerFacade cf = CustomerFacade.getCustomerFacade(emf);
        
        Customer c1 = cf.addCustomer("Henning", "Petersen");
        Customer c2 = cf.addCustomer("Peter", "Henningsen");
        
        //Find customer by ID
        System.out.println("Customer 1: " + cf.findByID(c1.getId()).getFirstName());
        System.out.println("Customer 2: " + cf.findByID(c2.getId()).getFirstName());
        
        //Find all customers
        System.out.println("Number of customers: " + cf.allCustomer().size());

    }

}
