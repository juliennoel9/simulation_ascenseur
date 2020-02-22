public class EvenementOuverturePorteCabine extends Evenement {
    /* OPC: Ouverture Porte Cabine
       L'instant précis ou la porte termine de s'ouvrir.
    */

    public EvenementOuverturePorteCabine(long d) {
	super(d);
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
	buffer.append("OPC");
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        Cabine cabine = immeuble.cabine;
        Etage étage = cabine.étage;

        //notYetImplemented();
        cabine.porteOuverte = true;

        assert cabine.porteOuverte;

        cabine.changerIntention('-');


        long nbDescendent= cabine.faireDescendrePassagers(immeuble,this.date);

        long nbMontent = 0;

        if (étage.aDesPassagers()){
            Passager newPass = étage.getPassagers().get(0);
            cabine.changerIntention(newPass.sens());
            cabine.faireMonterPassager(newPass);
            echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
            nbMontent ++;
            étage.getPassagers().remove(newPass);
            for (Passager p : étage.getPassagers()) {
                if (p.sens()==cabine.intention()){
                    if (cabine.faireMonterPassager(p)){
                        nbMontent ++;
                        étage.getPassagers().remove(p);
                        echeancier.supprimePAP(p.getEvenementPietonArrivePalier());
                    }
                }
            }
        }
        if (cabine.intention()!='-'){
            EvenementFermeturePorteCabine evenementFermeturePorteCabine = new EvenementFermeturePorteCabine(this.date+nbDescendent*tempsPourEntrerOuSortirDeLaCabine+nbMontent*tempsPourEntrerOuSortirDeLaCabine+this.tempsPourOuvrirOuFermerLesPortes);
            echeancier.ajouter(evenementFermeturePorteCabine);
        }
    }

}
