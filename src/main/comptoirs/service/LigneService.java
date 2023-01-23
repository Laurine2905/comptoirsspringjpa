package comptoirs.service;

import comptoirs.dao.CommandeRepository;
import comptoirs.dao.LigneRepository;
import comptoirs.dao.ProduitRepository;
import comptoirs.entity.Ligne;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated // Les contraintes de validatipn des méthodes sont vérifiées
public class LigneService {
    // La couche "Service" utilise la couche "Accès aux données" pour effectuer les traitements
    private final CommandeRepository commandeDao;
    private final LigneRepository ligneDao;
    private final ProduitRepository produitDao;

    // @Autowired
    // La couche "Service" utilise la couche "Accès aux données" pour effectuer les traitements
    public LigneService(CommandeRepository commandeDao, LigneRepository ligneDao, ProduitRepository produitDao) {
        this.commandeDao = commandeDao;
        this.ligneDao = ligneDao;
        this.produitDao = produitDao;
    }

    /**
     * <pre>
     * Service métier : 
     *     Enregistre une nouvelle ligne de commande pour une commande connue par sa clé,
     *     Incrémente la quantité totale commandée (Produit.unitesCommandees) avec la quantite à commander
     * Règles métier :
     *     - le produit référencé doit exister
     *     - la commande doit exister
     *     - la commande ne doit pas être déjà envoyée (le champ 'envoyeele' doit être null)
     *     - la quantité doit être positive
     *     - On doit avoir une quantite en stock du produit suffisante
     * <pre>
     * 
     *  @param commandeNum la clé de la commande
     *  @param produitRef la clé du produit
     *  @param quantite la quantité commandée (positive)
     *  @return la ligne de commande créée
     */
    @Transactional
    Ligne ajouterLigne(Integer commandeNum, Integer produitRef, @Positive int quantite) {
        // le produit et la commande doivent exister
        var produit = produitDao.findById(produitRef).orElseThrow();
        var commande = commandeDao.findById(commandeNum).orElseThrow();
        // Vérification que la commande n'est pas déjà envoyé
        if(commande.getEnvoyeele() != null) {
            throw new IllegalArgumentException("La commande a délà été envoyée");
        }
        // Vérification que la commande est positive
        if(quantite <= 0){
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        // Vérification que la quantité en stock est suffisante
        if(prod.getUnitesEnStock() < quantite){
            throw new IllegalArgumentException("La quantité en stock pour ce produit n'est pas suffisante");
        }

        Ligne nouvelleLigne = new Ligne(commande, produit, quantite);
        prod.setUnitesCommandees(prod.getUnitesCommandees() + quantite);
        ligneDao.save(nouvelleLigne);

        return nouvelleLigne;
    }
    // A tester : vérification produit et commande, commande positive, quantité stock suffisante
}
