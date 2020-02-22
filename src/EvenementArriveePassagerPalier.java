public class EvenementArriveePassagerPalier extends Evenement {
    /* APP: Arrivée Passager Palier
       L'instant précis ou un nouveau passager arrive sur un palier donné.
    */
    
    private Etage étage;

    public EvenementArriveePassagerPalier(long d, Etage edd) {
	super(d);
	étage = edd;
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
	buffer.append("APP ");
	buffer.append(étage.numéro());
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
		assert étage != null;
		assert immeuble.étage(étage.numéro()) == étage;
		Passager p = new Passager(date, étage, immeuble);

		Cabine c = immeuble.cabine;

		if(c.intention()=='-'){
		    if (c.étage.numéro() > étage.numéro()) {
                c.changerIntention('v');
            }else {
                c.changerIntention('^');
            }
            long ancienneDate = this.date;
		    EvenementPietonArrivePalier epap = new EvenementPietonArrivePalier(ancienneDate+Global.délaiDePatienceAvantSportif, étage, p);
		    p.setEvenementPietonArrivePalier(epap);
		    echeancier.ajouter(epap);
            if (c.étage == étage && c.porteOuverte) {
                c.faireMonterPassager(p);
                echeancier.supprimePAP(p.getEvenementPietonArrivePalier());
                EvenementFermeturePorteCabine fpc = new EvenementFermeturePorteCabine(ancienneDate+Global.tempsPourEntrerOuSortirDeLaCabine+Global.tempsPourOuvrirOuFermerLesPortes);
                echeancier.ajouter(fpc);
            }else {
                étage.ajouter(p);
                EvenementFermeturePorteCabine fpc = new EvenementFermeturePorteCabine(ancienneDate+Global.tempsPourOuvrirOuFermerLesPortes);
                echeancier.ajouter(fpc);
            }
            this.date = this.date +étage.arrivéeSuivante();
            echeancier.ajouter(this);
        }else {
            étage.ajouter(p);
            EvenementPietonArrivePalier epap = new EvenementPietonArrivePalier(this.date+Global.délaiDePatienceAvantSportif, étage, p);
            p.setEvenementPietonArrivePalier(epap);
            echeancier.ajouter(epap);
            this.date = this.date +étage.arrivéeSuivante();
            echeancier.ajouter(this);
        }
    }

}
