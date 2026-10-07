package microbank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroCompte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCompte type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal solde = BigDecimal.ZERO;

    private LocalDate dateOuverture = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCompte statut = StatutCompte.ACTIF;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "compte")
    private List<Operation> operations = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCompte() {
        return numeroCompte;
    }

    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    public TypeCompte getType() {
        return type;
    }

    public void setType(TypeCompte type) {
        this.type = type;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public LocalDate getDateOuverture() {
        return dateOuverture;
    }

    public void setDateOuverture(LocalDate dateOuverture) {
        this.dateOuverture = dateOuverture;
    }

    public StatutCompte getStatut() {
        return statut;
    }

    public void setStatut(StatutCompte statut) {
        this.statut = statut;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }
}