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
		//assert étage.numéro() != cabine.étage.numéro();

		Etage e;
		Cabine oldCabine = immeuble.cabine;
		cabine.étage = étage;
		long dateouvrirporte =this.date + this.tempsPourOuvrirOuFermerLesPortes;

		/*if(étage==immeuble.étageLePlusHaut()){
			cabine.changerIntention('v');
		}*/
		//else if (étage==immeuble.étageLePlusBas()){
		//	cabine.changerIntention('^');
		//}

		boolean descend = (cabine.intention() == 'v');

		if (modeParfait && (cabine.passagersVeulentDescendre() ||
				(!cabine.aDesPassagers() && étage.aDesPassagers() &&  (   (cabine.intention() == '^' && !immeuble.passagerAuDessus(cabine.étage)) ||     (cabine.intention() == 'v' && !immeuble.passagerEnDessous(cabine.étage))   )   ) ||
				étage.aPassagersMemeSansCabine(cabine.intention()) ||
				((cabine.intention() == '^' && !immeuble.passagerAuDessus(cabine.étage) && étage.aPassagersMemeSansCabine(cabine.intention())) || (cabine.intention() == 'v' && !immeuble.passagerEnDessous(cabine.étage) && étage.aPassagersMemeSansCabine(cabine.intention()))))
		){

			EvenementOuverturePorteCabine evenementOuverturePorteCabine = new EvenementOuverturePorteCabine(dateouvrirporte);
			echeancier.ajouter(evenementOuverturePorteCabine);
		}else if (!modeParfait && (cabine.passagersVeulentDescendre() || étage.aDesPassagers())) {
			EvenementOuverturePorteCabine evenementOuverturePorteCabine = new EvenementOuverturePorteCabine(dateouvrirporte);
			echeancier.ajouter(evenementOuverturePorteCabine);
		} else {
			if (cabine.intention()=='v' && !immeuble.passagerEnDessous(étage) && !cabine.aDesPassagers() && immeuble.passagerAuDessus(étage)){
				cabine.changerIntention('^');
				e = immeuble.étage(étage.numéro() + 1);
			} else if (cabine.intention()=='^' && !immeuble.passagerAuDessus(étage) && !cabine.aDesPassagers() && immeuble.passagerEnDessous(étage)){
				cabine.changerIntention('v');
				e = immeuble.étage(étage.numéro() - 1);
			} else {
				if (!descend){
					if (oldCabine.étage == immeuble.étageLePlusHaut()){
						//e = oldCabine.étage;
						e = immeuble.étage(oldCabine.étage.numéro()-1);
					}else {
						e = immeuble.étage(étage.numéro() + 1);
					}
				}else if (descend){
					if (oldCabine.étage == immeuble.étageLePlusBas()){
						//e = oldCabine.étage;
						e = immeuble.étage(oldCabine.étage.numéro()+1);
					}else {
						e = immeuble.étage(étage.numéro() - 1);
					}
				}else{
					e = immeuble.étage(étage.numéro());
				}
			}
			EvenementPassageCabinePalier evenementPassageCabinePalier = new EvenementPassageCabinePalier((this.date + this.tempsPourBougerLaCabineDUnEtage),e);
			echeancier.ajouter(evenementPassageCabinePalier);
		}
		if (modeParfait && !cabine.aDesPassagers()){
			if (étage == immeuble.étageLePlusBas() && !étage.aDesPassagers()){
				cabine.changerIntention('^');
			}
		}
	}
}
