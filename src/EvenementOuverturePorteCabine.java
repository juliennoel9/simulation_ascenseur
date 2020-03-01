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

        if (immeuble.passagerAuDessus(étage)){
            cabine.changerIntention('^');
        }else if (immeuble.passagerEnDessous(étage)){
            cabine.changerIntention('v');
        }else {
            cabine.changerIntention('-');
        }

        long nbDescendent= cabine.faireDescendrePassagers(immeuble,this.date);

        long nbMontent = 0;

        if (étage.aDesPassagers()){
            Passager newPass = étage.getPassagers().get(0);
            if (modeParfait){
                newPass = cabine.choisirQuiMonte();
            }
            if (newPass != null){
                cabine.changerIntention(newPass.sens());
                cabine.faireMonterPassager(newPass);
                echeancier.supprimePAP(newPass.getEvenementPietonArrivePalier());
                nbMontent ++;
                étage.getPassagers().remove(newPass);
            }
            if(!modeParfait){
                int i = 0;
                while(étage.getPassagers().size()!=0 && i < étage.getPassagers().size()){
                    cabine.faireMonterPassager(étage.getPassagers().get(i));
                    nbMontent ++;
                    echeancier.supprimePAP(étage.getPassagers().get(i).getEvenementPietonArrivePalier());
                    étage.getPassagers().remove(i);
                    i++;
                }
                cabine.recalculerIntentionInfernale();
            }else {
                int j = 0;
                while(j<étage.getPassagers().size()){
                    if (étage.getPassagers().get(j).sens() == cabine.intention()) {
                        if (cabine.faireMonterPassager(étage.getPassagers().get(j))) {
                            nbMontent++;
                            echeancier.supprimePAP(étage.getPassagers().get(j).getEvenementPietonArrivePalier());
                            étage.getPassagers().remove(étage.getPassagers().get(j));
                        }
                    }
                    j++;
                }
            }
        }
        if (cabine.intention()!='-'){
            EvenementFermeturePorteCabine evenementFermeturePorteCabine = new EvenementFermeturePorteCabine(this.date+nbDescendent*tempsPourEntrerOuSortirDeLaCabine+nbMontent*tempsPourEntrerOuSortirDeLaCabine+this.tempsPourOuvrirOuFermerLesPortes);
            echeancier.ajouter(evenementFermeturePorteCabine);
        }
        if(étage==immeuble.étageLePlusHaut()){
            cabine.changerIntention('v');
        }
    }

}
