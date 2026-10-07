package microbank.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeOperation type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    private LocalDateTime dateOperation = LocalDateTime.now();

    private String description;

    // solde du compte après l'opération (pour la colonne "Solde" de l'historique)
    @Column(precision = 15, scale = 2)
    private BigDecimal soldeApres;

    @ManyToOne(optional = false)
    @JoinColumn(name = "compte_id")
    private Account compte;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public TypeOperation getType() {
        return type;
    }

    public void setType(TypeOperation type) {
        this.type = type;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public LocalDateTime getDateOperation() {
        return dateOperation;
    }

    public void setDateOperation(LocalDateTime dateOperation) {
        this.dateOperation = dateOperation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSoldeApres() {
        return soldeApres;
    }

    public void setSoldeApres(BigDecimal soldeApres) {
        this.soldeApres = soldeApres;
    }

    public Account getCompte() {
        return compte;
    }

    public void setCompte(Account compte) {
        this.compte = compte;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getDateFormatee() {
        return dateOperation.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}