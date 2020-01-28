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
            c.changerIntention(p.sens());
            if (c.faireMonterPassager(p)) {
                long ancienneDate = this.date;
                this.date = this.date +étage.arrivéeSuivante();
                echeancier.ajouter(this);
                EvenementFermeturePorteCabine fpc = new EvenementFermeturePorteCabine(ancienneDate+Global.tempsPourEntrerOuSortirDeLaCabine+Global.tempsPourOuvrirOuFermerLesPortes);
                echeancier.ajouter(fpc);
            }
        }else {
		    notYetImplemented();
        }
    }

}
