public class EvenementPassageCabinePalier extends Evenement {
    /* PCP: Passage Cabine Palier
       L'instant précis où la cabine passe juste en face d'un étage précis.
       Vous pouvez modifier cette classe comme vous voulez (ajouter/modifier des méthodes etc.).
    */
    
    private Etage étage;
    
    public EvenementPassageCabinePalier(long d, Etage e) {
	super(d);
	étage = e;
    }
    
    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
	buffer.append("PCP ");
	buffer.append(étage.numéro());
    }
    
    public void traiter(Immeuble immeuble, Echeancier echeancier) {
		Cabine cabine = immeuble.cabine;
		assert ! cabine.porteOuverte;
		assert étage.numéro() != cabine.étage.numéro();

		Etage e;
		cabine.étage = étage;
		long dateouvrirporte =this.date + this.tempsPourOuvrirOuFermerLesPortes;

		if(étage==immeuble.étageLePlusHaut()){
			cabine.changerIntention('v');
		}else if (étage==immeuble.étageLePlusBas()){
			cabine.changerIntention('^');
		}

		boolean descend = (cabine.intention() == 'v');

		if (cabine.passagersVeulentDescendre() || étage.aDesPassagers() ){
			EvenementOuverturePorteCabine evenementOuverturePorteCabine = new EvenementOuverturePorteCabine(dateouvrirporte);
			echeancier.ajouter(evenementOuverturePorteCabine);
		}else {
			if (!descend){
				e = immeuble.étage(étage.numéro() + 1);
			}else if (descend){
				e = immeuble.étage(étage.numéro() - 1);
			}else{
				e = immeuble.étage(étage.numéro());
			}
			EvenementPassageCabinePalier evenementPassageCabinePalier = new EvenementPassageCabinePalier((this.date + this.tempsPourBougerLaCabineDUnEtage),e);
			echeancier.ajouter(evenementPassageCabinePalier);
		}
		if (!modeParfait){
			cabine.recalculerIntentionInfernale();
		}
	}
}
